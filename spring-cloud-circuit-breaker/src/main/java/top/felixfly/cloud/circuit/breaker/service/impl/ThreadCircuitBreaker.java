package top.felixfly.cloud.circuit.breaker.service.impl;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Service;
import top.felixfly.cloud.circuit.breaker.annotation.CircuitBreakerCommand;
import top.felixfly.cloud.circuit.breaker.constant.CircuitBreakerStrategy;
import top.felixfly.cloud.circuit.breaker.service.AbstractCircuitBreaker;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

/**
 * {@link CircuitBreakerStrategy#THREAD} 超时熔断器
 *
 * @author FelixFly <chenglinxu@yeah.net>
 * @date 2020/2/5
 */
@Service
public class ThreadCircuitBreaker extends AbstractCircuitBreaker {

    @Override
    public boolean isSupport(CircuitBreakerStrategy strategy) {
        return CircuitBreakerStrategy.THREAD.equals(strategy);
    }

    @Override
    public Object internalExecute(Method method, ProceedingJoinPoint joinPoint,
                                  CircuitBreakerCommand circuitBreakerCommand) throws Exception {
        // 执行方法参数
        Object[] args = joinPoint.getArgs();
        // 使用CompletableFuture进行异步调用
        CompletableFuture<Object> completableFuture = CompletableFuture.supplyAsync(() -> {

            try {
                return joinPoint.proceed(args);
            } catch (Throwable throwable) {
                throw new RuntimeException("调用目标方法错误");
            }
        });
        return completableFuture.get(circuitBreakerCommand.timeout(),circuitBreakerCommand.timeUnit());
    }
}
