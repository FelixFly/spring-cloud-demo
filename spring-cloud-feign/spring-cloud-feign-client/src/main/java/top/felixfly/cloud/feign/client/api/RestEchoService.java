package top.felixfly.cloud.feign.client.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.felixfly.cloud.feign.client.annotation.RestFeignClient;

/**
 * 服务提供echo服务
 *
 * @author FelixFly <chenglinxu@yeah.net>
 * @date 2020/2/7
 */
@RestFeignClient("feign-server") //标注是Feign Client
public interface RestEchoService {

    @GetMapping("/echo")
    String echo(@RequestParam("message") String message);
}
