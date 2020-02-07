package top.felixfly.cloud.feign.client.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用Rest 客户端
 *
 * @author FelixFly <chenglinxu@yeah.net>
 * @date 2020/2/7
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(RestFeignClientsRegistrar.class)
public @interface EnableRestFeignClients {

    /**
     * {@link RestFeignClient}接口列表
     */
    Class<?>[] clients() default {};
}
