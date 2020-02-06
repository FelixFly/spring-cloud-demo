package top.felixfly.cloud.circuit.breaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

/**
 * 服务熔断应用
 *
 * @author FelixFly <chenglinxu@yeah.net>
 * @date 2020/2/5
 */
@SpringBootApplication
@EnableCircuitBreaker
public class CircuitBreakerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CircuitBreakerApplication.class, args);
    }
}
