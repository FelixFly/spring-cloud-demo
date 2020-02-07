package top.felixfly.cloud.feign.client.annotation;

import java.lang.annotation.*;

/**
 * rest 客户端调用
 *
 * @author FelixFly <chenglinxu@yeah.net>
 * @date 2020/2/7
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestFeignClient {

    /**
     * 服务名称
     *
     */
    String value() default "";
}
