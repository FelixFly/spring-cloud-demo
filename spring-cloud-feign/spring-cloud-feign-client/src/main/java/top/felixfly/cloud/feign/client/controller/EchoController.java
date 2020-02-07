package top.felixfly.cloud.feign.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.felixfly.cloud.feign.client.api.EchoService;
import top.felixfly.cloud.feign.client.api.RestEchoService;

/**
 * 演示服务端点
 *
 * @author FelixFly <chenglinxu@yeah.net>
 * @date 2020/2/7
 */
@RestController
public class EchoController {

    @Autowired
    private EchoService echoService;

    @Autowired
    private RestEchoService restEchoService;

    @GetMapping("/echo")
    public String echo(String message) {
        return this.echoService.echo(message);
    }

    @GetMapping("/rest/echo")
    public String restEcho(String message) {
        return this.restEchoService.echo(message);
    }
}
