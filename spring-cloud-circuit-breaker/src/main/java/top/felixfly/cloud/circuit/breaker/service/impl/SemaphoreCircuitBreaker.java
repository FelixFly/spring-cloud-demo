package top.felixfly.cloud.circuit.breaker.service.impl;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Service;
import top.felixfly.cloud.circuit.breaker.annotation.CircuitBreakerCommand;
import top.felixfly.cloud.circuit.breaker.constant.CircuitBreakerStrategy;
import top.felixfly.cloud.circuit.breaker.service.AbstractCircuitBreaker;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeoutException;

/**
 * {@link CircuitBreakerStrategy#SEMAPHORE} 限流熔断器
 *
 * @author FelixFly <chenglinxu@yeah.net>
 * @date 2020/2/5
 */
@Service
public class SemaphoreCircuitBreaker extends AbstractCircuitBreaker {

    private Map<Method, Semaphore> methodSemaphoreMap = new ConcurrentHashMap<>(128);

    @Override
    public boolean isSupport(CircuitBreakerStrategy strategy) {
        return CircuitBreakerStrategy.SEMAPHORE.equals(strategy);
    }

    @Override
    public Object internalExecute(Method method, ProceedingJoinPoint joinPoint,
                                  CircuitBreakerCommand circuitBreakerCommand) throws Exception {
        Semaphore semaphore = initSemaphore(method, circuitBreakerCommand.semaphore());
        boolean tryAcquire = semaphore.tryAcquire(circuitBreakerCommand.timeout(), circuitBreakerCommand.timeUnit());
        if (!tryAcquire) {
            throw new TimeoutException("获取信号量超时");
        }
        try {
            return joinPoint.proceed(joinPoint.getArgs());
        } catch (Throwable throwable) {
            throw new RuntimeException("调用目标方法错误");
        } finally {
            semaphore.release();
        }
    }

    private Semaphore initSemaphore(Method method, int semaphore) {
        return methodSemaphoreMap.computeIfAbsent(method,
                value -> new Semaphore(semaphore));
    }
}
