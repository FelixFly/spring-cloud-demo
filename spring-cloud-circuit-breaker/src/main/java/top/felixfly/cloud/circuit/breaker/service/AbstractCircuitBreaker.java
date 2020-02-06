package top.felixfly.cloud.circuit.breaker.service;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import top.felixfly.cloud.circuit.breaker.annotation.CircuitBreakerCommand;

import java.lang.reflect.Method;
import java.util.concurrent.TimeoutException;

/**
 * 服务熔断器抽象实现
 *
 * @author FelixFly <chenglinxu@yeah.net>
 * @date 2020/2/5
 */
public abstract class AbstractCircuitBreaker implements CircuitBreaker {

    @Override
    public Object execute(ProceedingJoinPoint joinPoint, Method method, CircuitBreakerCommand circuitBreakerCommand)
            throws Exception {
        // 目标对象
        Object target = joinPoint.getTarget();
        // 执行方法参数
        Object[] args = joinPoint.getArgs();
        try {
            return internalExecute(method, joinPoint, circuitBreakerCommand);
        } catch (TimeoutException exception) {
            // 对TimeoutException异常进行回调方法
            String fallBack = circuitBreakerCommand.fallBack();
            if (StringUtils.isNotBlank(fallBack)) {
                return invokeFallBackMethod(method, target, circuitBreakerCommand, args);
            }
            throw exception;
        }
    }


    /**
     * 内部执行方法
     *
     * @param method         执行方法
     * @param joinPoint         切面执行
     * @param circuitBreakerCommand 服务熔断注解信息
     * @return 执行结果
     * @throws TimeoutException 超时异常
     */
    public abstract Object internalExecute(Method method, ProceedingJoinPoint joinPoint,
                                           CircuitBreakerCommand circuitBreakerCommand)
            throws Exception;

    /**
     * 执行服务熔断方法
     *
     * @param method         执行方法
     * @param target         目前对象
     * @param circuitBreakerCommand 服务熔断注解信息
     * @param args           方法参数
     * @return 执行结果
     */
    protected Object invokeFallBackMethod(Method method, Object target,
                                          CircuitBreakerCommand circuitBreakerCommand, Object[] args) throws Exception {
        Method fallBackMethod = fallBackMethod(method, target, circuitBreakerCommand);
        return fallBackMethod.invoke(target, args);
    }


    /**
     * 获取回调方法
     *
     * @param method         执行方法
     * @param target         目前对象
     * @param circuitBreakerCommand 服务熔断注解信息
     * @return 目标方法
     * @throws NoSuchMethodException 无此目标方法
     */
    private Method fallBackMethod(Method method, Object target,
                                  CircuitBreakerCommand circuitBreakerCommand) throws NoSuchMethodException {
        String fallBackMethodName = circuitBreakerCommand.fallBack();
        Class<?> beanClass = target.getClass();
        return beanClass.getMethod(fallBackMethodName, method.getParameterTypes());
    }
}
