package top.felixfly.cloud.feign.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import top.felixfly.cloud.feign.client.annotation.EnableRestFeignClients;
import top.felixfly.cloud.feign.client.api.EchoService;
import top.felixfly.cloud.feign.client.api.RestEchoService;

/**
 * Feign 客户端服务
 *
 * @author FelixFly <chenglinxu@yeah.net>
 * @date 2020/2/7
 */
@SpringBootApplication
@EnableFeignClients(clients = EchoService.class) //启用Feign Client
@EnableRestFeignClients(clients = RestEchoService.class) // 启动Rest Client
public class FeignClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeignClientApplication.class, args);
    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
