package top.felixfly.cloud.circuit.breaker;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;

/**
 * 注解演示服务端点
 *
 * @author FelixFly <chenglinxu@yeah.net>
 * @date 2020/2/5
 */
@RestController
@RequestMapping("/annotation")
public class AnnotationDemoController {


    @HystrixCollapser(batchMethod = "batchMethod", collapserProperties = {
            @HystrixProperty(name = "timerDelayInMilliseconds", value = "50")
    })
    @GetMapping("/collapser/{message}")
    public Future<String> collapser(@PathVariable String message) {
        await();
        return null;
    }


    @HystrixCommand
    public List<String> batchMethod(List<String> messages) {
        await();
        return messages.stream().map(message ->"Thread:" + message).collect(toList());
    }

    public List<String> batchFallBack(List<String> messages) {
        return messages.stream().map(message ->"Fallback:" + message).collect(toList());
    }

    @HystrixCommand(fallbackMethod = "fallBack", commandProperties = {
            @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "50")
    })
    @GetMapping("/thread/{message}")
    public String thread(@PathVariable String message) {
        await();
        return "Thread:" + message;
    }

    @HystrixCommand(fallbackMethod = "fallBack", commandProperties = {
            @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"),
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "50"),
            @HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests", value = "1")
    })
    @GetMapping("/semaphore/{message}")
    public String hello(@PathVariable String message) {
        await();
        return "Semaphore:" + message;
    }

    public String fallBack(String message) {
        return "Fallback:" + message;
    }

    public void await() {
        int waitTime = new Random().nextInt(100);
        try {
            System.out.printf("线程【%s】执行时间%d毫秒\n", Thread.currentThread().getName(), waitTime);
            TimeUnit.MILLISECONDS.sleep(waitTime);
        } catch (InterruptedException e) {
            throw new RuntimeException("线程中断");
        }
    }

}
