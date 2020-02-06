package top.felixfly.cloud.circuit.breaker.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import top.felixfly.cloud.circuit.breaker.annotation.CircuitBreakerCommand;
import top.felixfly.cloud.circuit.breaker.constant.CircuitBreakerStrategy;
import top.felixfly.cloud.circuit.breaker.service.CircuitBreaker;

import java.lang.reflect.Method;
import java.util.List;

/**
 * {@link CircuitBreakerCommand} 服务熔断切面
 *
 * @author FelixFly <chenglinxu@yeah.net>
 * @date 2020/2/5
 */
@Configuration
@Aspect
public class CircuitBreakerAop {

    @Autowired
    private List<CircuitBreaker> circuitBreakers;

    @Around("@annotation(top.felixfly.cloud.circuit.breaker.annotation.CircuitBreakerCommand)")
    public Object circuitBreaker(ProceedingJoinPoint joinPoint) throws Exception {
        // @CircuitBreaker直接注解在方法上，肯定是直接返回
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        // 执行方法
        Method method = methodSignature.getMethod();
        CircuitBreakerCommand circuitBreakerCommand = method.getAnnotation(CircuitBreakerCommand.class);

        CircuitBreakerStrategy strategy = circuitBreakerCommand.strategy();
        CircuitBreaker circuitBreaker = circuitBreakers.stream().filter(item -> item.isSupport(strategy))
                .findAny().orElseThrow(() -> new RuntimeException("无此服务策略[" + strategy + "]"));
        return circuitBreaker.execute(joinPoint,method, circuitBreakerCommand);
    }
}
