package top.felixfly.cloud.service.discovery.server.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 服务发现客户端程序
 *
 * @author FelixFly <chenglinxu@yeah.net>
 * @date 2020/2/1
 */
@SpringBootApplication
public class DiscoveryClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscoveryClientApplication.class, args);
    }
}
