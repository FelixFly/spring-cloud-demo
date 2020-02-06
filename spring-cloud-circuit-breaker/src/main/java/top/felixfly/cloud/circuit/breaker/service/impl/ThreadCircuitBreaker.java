package top.felixfly.cloud.circuit.breaker.service.impl;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Service;
import top.felixfly.cloud.circuit.breaker.annotation.CircuitBreakerCommand;
import top.felixfly.cloud.circuit.breaker.constant.CircuitBreakerStrategy;
import top.felixfly.cloud.circuit.breaker.service.AbstractCircuitBreaker;

import javax.annotation.PreDestroy;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

/**
 * {@link CircuitBreakerStrategy#THREAD} 超时熔断器
 *
 * @author FelixFly <chenglinxu@yeah.net>
 * @date 2020/2/5
 */
@Service
public class ThreadCircuitBreaker extends AbstractCircuitBreaker {

    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Override
    public boolean isSupport(CircuitBreakerStrategy strategy) {
        return CircuitBreakerStrategy.THREAD.equals(strategy);
    }

    @Override
    public Object internalExecute(Method method, ProceedingJoinPoint joinPoint,
                                  CircuitBreakerCommand circuitBreakerCommand) throws Exception {
        // 执行方法参数
        Object[] args = joinPoint.getArgs();

        // 进行异步调用
        Future<Object> future = executorService.submit(() -> {
            try {
                return joinPoint.proceed(args);
            } catch (Throwable throwable) {
                throw new RuntimeException("调用目标方法错误");
            }
        });
        try {
            return future.get(circuitBreakerCommand.timeout(), circuitBreakerCommand.timeUnit());
        } catch (TimeoutException timeoutException) {
            future.cancel(true);//取消执行
            throw timeoutException;
        }

        // 使用CompletableFuture进行异步调用,超时异常没有办法取消调用
        /*CompletableFuture<Object> completableFuture = CompletableFuture.supplyAsync(() -> {

            try {
                return joinPoint.proceed(args);
            } catch (Throwable throwable) {
                throw new RuntimeException("调用目标方法错误");
            }
        });
        try {
            return completableFuture.get(circuitBreakerCommand.timeout(), circuitBreakerCommand.timeUnit());
        } catch (TimeoutException timeoutException) {
            // this value has no effect in this implementation because interrupts are not used to control processing.
            completableFuture.cancel(true);//取消执行，无法进行取消
            throw timeoutException;
        }*/
    }

    @PreDestroy
    public void destroy(){
        executorService.shutdown();
    }
}
