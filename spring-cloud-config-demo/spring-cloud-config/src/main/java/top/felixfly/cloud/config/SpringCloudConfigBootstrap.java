package top.felixfly.cloud.config;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 配置中心服务端引导类
 *
 * @author FelixFly 2019/9/22
 */
@SpringBootApplication
public class SpringCloudConfigBootstrap {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(SpringCloudConfigBootstrap.class);
        springApplication.setBannerMode(Banner.Mode.OFF);
        springApplication.run(args);
    }
}
