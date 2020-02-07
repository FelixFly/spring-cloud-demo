package top.felixfly.cloud.feign.client.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 服务提供echo服务
 *
 * @author FelixFly <chenglinxu@yeah.net>
 * @date 2020/2/7
 */
@FeignClient("feign-server") //标注是Feign Client
public interface EchoService {

    @GetMapping("/echo")
    String echo(@RequestParam("message") String message);
}
