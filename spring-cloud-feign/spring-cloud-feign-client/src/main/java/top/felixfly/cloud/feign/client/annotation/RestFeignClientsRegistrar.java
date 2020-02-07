package top.felixfly.cloud.feign.client.annotation;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * rest 客户端注册
 *
 * @author FelixFly <chenglinxu@yeah.net>
 * @date 2020/2/7
 */
public class RestFeignClientsRegistrar
        implements ImportBeanDefinitionRegistrar, BeanFactoryAware {

    private BeanFactory beanFactory;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        // 类加载类
        ClassLoader classLoader = metadata.getClass().getClassLoader();
        // 获取EnableRestFeignClients注解的内容
        Map<String, Object> annotationAttributes = metadata
                .getAnnotationAttributes(EnableRestFeignClients.class.getName());
        // 获取clients
        Class<?>[] clients = (Class<?>[]) annotationAttributes.get("clients");
        Stream.of(clients)
                .filter(Class::isInterface)
                .filter(interfaceClass ->
                        Objects.nonNull(AnnotationUtils.findAnnotation(interfaceClass, RestFeignClient.class)))
                .forEach(restClientClass -> {
                    // 获取服务名称
                    RestFeignClient restFeignClient = AnnotationUtils
                            .findAnnotation(restClientClass, RestFeignClient.class);
                    String serviceName = restFeignClient.value();


                    Object proxyInstance = Proxy.newProxyInstance(classLoader, new Class<?>[]{restClientClass},
                            new RestFeignClientInvocationHandler(serviceName, this.beanFactory));

                    // 注册Bean
                    BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                            .genericBeanDefinition(RestFeignClientFactoryBean.class);
                    beanDefinitionBuilder.addConstructorArgValue(proxyInstance);
                    beanDefinitionBuilder.addConstructorArgValue(restClientClass);
                    AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
                    String beanName = serviceName + restClientClass.getName();
                    beanDefinition.setFactoryBeanName("&" + beanName);
                    registry.registerBeanDefinition(beanName, beanDefinition);

                });
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }


    private static class RestFeignClientFactoryBean implements FactoryBean<Object> {

        private final Object object;
        private final Class<?> objectType;

        private RestFeignClientFactoryBean(Object object, Class<?> objectType) {
            this.object = object;
            this.objectType = objectType;
        }


        @Override
        public Object getObject() throws Exception {
            return this.object;
        }

        @Override
        public Class<?> getObjectType() {
            return this.objectType;
        }
    }
}
