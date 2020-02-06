package top.felixfly.cloud.circuit.breaker.constant;

/**
 * 服务熔断策略
 *
 * @author FelixFly <chenglinxu@yeah.net>
 * @date 2020/2/5
 */
public enum CircuitBreakerStrategy {
    /**
     * 超时
     */
    THREAD,
    /**
     * 限流
     */
    SEMAPHORE;
}
