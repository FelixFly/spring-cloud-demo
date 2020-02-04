package top.felixfly.cloud.load.balance.client.loadbalance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Map;

/**
 * {@link Qualifier}应用
 *
 * @author xcl <xcl@winning.com.cn>
 * @date 2020/2/4
 */
@SpringBootApplication
public class QualifierApplication  implements ApplicationRunner {

    @Bean
    public String a(){
        return "bean-a";
    }

    @Bean
    @Qualifier
    public String b(){
        return "bean-b";
    }

    @Bean
    @Qualifier
    public String c(){
        return "bean-c";
    }

    @Autowired
    private Map<String,String> beans;

    @Autowired
    @Qualifier
    private Map<String, String> qualifierBeans;

    public static void main(String[] args) {
        new SpringApplicationBuilder(QualifierApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(beans);
        System.out.println(qualifierBeans);
    }
}
