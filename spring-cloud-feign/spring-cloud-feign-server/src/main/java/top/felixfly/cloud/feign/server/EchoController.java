package top.felixfly.cloud.feign.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 演示服务端点
 *
 * @author FelixFly <chenglinxu@yeah.net>
 * @date 2020/2/7
 */
@RestController
public class EchoController {

    @Autowired
    private Environment environment;


    @GetMapping("/echo")
    public String echo(@RequestParam String message) {
        // 由于采用的是随机端口，这地方必须采用这个方式获取端口
        String port = environment.getProperty("local.server.port");
        return "ECHO(" + port + "):" + message;
    }
}
