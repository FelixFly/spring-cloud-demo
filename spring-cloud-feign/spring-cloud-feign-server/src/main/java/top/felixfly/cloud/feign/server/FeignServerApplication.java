package top.felixfly.cloud.feign.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Feign 服务端应用
 *
 * @author FelixFly <chenglinxu@yeah.net>
 * @date 2020/2/7
 */
@SpringBootApplication
public class FeignServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeignServerApplication.class, args);
    }
}
