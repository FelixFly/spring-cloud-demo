package top.felixfly.cloud.circuit.breaker;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.felixfly.cloud.circuit.breaker.annotation.CircuitBreakerCommand;
import top.felixfly.cloud.circuit.breaker.constant.CircuitBreakerStrategy;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 自定义演示服务端点
 *
 * @author FelixFly <chenglinxu@yeah.net>
 * @date 2020/2/5
 */
@RestController
@RequestMapping("custom")
public class CustomDemoController {

    @CircuitBreakerCommand(strategy = CircuitBreakerStrategy.THREAD, fallBack = "fallBack")
    @GetMapping("/thread/{message}")
    public String thread(@PathVariable String message) {
        await();
        System.out.println("执行Thread");
        return "Thread:" + message;
    }

    @CircuitBreakerCommand(strategy = CircuitBreakerStrategy.SEMAPHORE, semaphore = 1, fallBack = "fallBack")
    @GetMapping("/semaphore/{message}")
    public String hello(@PathVariable String message) {
        await();
        System.out.println("执行Semaphore");
        return "Semaphore:" + message;
    }

    public String fallBack(String message) {
        return "Fallback:" + message;
    }

    public void await() {
        // 100的时候限流熔断不太好演示，改成300进行演示
        int waitTime = new Random().nextInt(300);
        try {
            System.out.printf("线程【%s】执行时间%d毫秒\n", Thread.currentThread().getName(), waitTime);
            TimeUnit.MILLISECONDS.sleep(waitTime);
        } catch (InterruptedException e) {
            throw new RuntimeException("线程中断");
        }
    }

}
