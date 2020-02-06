package top.felixfly.cloud.circuit.breaker.annotation;

import top.felixfly.cloud.circuit.breaker.constant.CircuitBreakerStrategy;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 服务熔断注解
 *
 * @author FelixFly <chenglinxu@yeah.net>
 * @date 2020/2/5
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CircuitBreakerCommand {

    /**
     * 服务熔断策略,默认为{@link CircuitBreakerStrategy#SEMAPHORE}
     */
    CircuitBreakerStrategy strategy() default CircuitBreakerStrategy.SEMAPHORE;

    /**
     * 信号量
     */
    int semaphore() default 10;

    /**
     * 超时时间
     */
    int timeout() default 50;

    /**
     * 超时时间单位
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    /**
     * 回调方法
     */
    String fallBack() default "";
}
