package top.felixfly.cloud.circuit.breaker.service;

import org.aspectj.lang.ProceedingJoinPoint;
import top.felixfly.cloud.circuit.breaker.annotation.CircuitBreakerCommand;
import top.felixfly.cloud.circuit.breaker.constant.CircuitBreakerStrategy;

import java.lang.reflect.Method;

/**
 * 服务熔断器
 *
 * @author FelixFly <chenglinxu@yeah.net>
 * @date 2020/2/5
 */
public interface CircuitBreaker {

    /**
     * 支持的策略
     *
     * @param strategy 策略
     * @return true 支持  false 不支持
     */
    boolean isSupport(CircuitBreakerStrategy strategy);


    /**
     * 执行方法
     *
     * @param joinPoint      切面执行
     * @param method         执行方法
     * @param circuitBreakerCommand 注解信息
     * @return 执行结果
     */
    Object execute(ProceedingJoinPoint joinPoint, Method method, CircuitBreakerCommand circuitBreakerCommand) throws Exception;
}
