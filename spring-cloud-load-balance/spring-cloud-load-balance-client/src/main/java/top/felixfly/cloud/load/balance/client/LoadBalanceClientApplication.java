package top.felixfly.cloud.load.balance.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import top.felixfly.cloud.load.balance.client.loadbalance.LoadBalancedRequestInterceptor;

import java.util.Collections;

/**
 * 负载均衡客户端启动程序
 *
 * @author FelixFly <chenglinxu@yeah.net>
 * @date 2020/2/4
 */
@SpringBootApplication
public class LoadBalanceClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoadBalanceClientApplication.class, args);
    }

    @Bean
    @Autowired
    public RestTemplate restTemplate(LoadBalancedRequestInterceptor interceptor){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(interceptor));
        return restTemplate;
    }


    @Bean
    @LoadBalanced
    public RestTemplate lbRestTemplate(){
        return new RestTemplate();
    }

}
