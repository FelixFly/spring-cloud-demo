package top.felixfly.cloud.load.balance.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 负载均衡服务端启动程序
 *
 * @author FelixFly <chenglinxu@yeah.net>
 * @date 2020/2/4
 */
@SpringBootApplication
public class LoadBalanceServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoadBalanceServerApplication.class, args);
    }
}
