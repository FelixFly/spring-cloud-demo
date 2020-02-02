package top.felixfly.springcloud.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置服务启动类
 *
 * @author FelixFly <chenglinxu@yeah.net>
 * @date 2020/1/29
 */
@SpringBootApplication
@EnableConfigServer
public class SpringCloudConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudConfigServerApplication.class, args);
    }


    @Bean
    public EnvironmentRepository defaultEnvironmentRepository(){
        return (application, profile, label) -> {
            Environment environment = new Environment("custom-default");
            List<PropertySource> propertySources = environment.getPropertySources();
            Map<String, String> sourceMap = new HashMap<>();
            sourceMap.put("application", "custom-application");
            PropertySource propertySource = new PropertySource("map", sourceMap);
            propertySources.add(propertySource);
            return environment;
        };
    }
}
