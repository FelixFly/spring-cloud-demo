package top.felixfly.cloud.feign.client.annotation;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Objects;

/**
 * Rest 客户端调用
 *
 * @author xcl <xcl@winning.com.cn>
 * @date 2020/2/7
 */
public class RestFeignClientInvocationHandler implements InvocationHandler {

    private final String serverName;

    private final BeanFactory beanFactory;

    public RestFeignClientInvocationHandler(String serverName, BeanFactory beanFactory) {
        this.serverName = serverName;
        this.beanFactory = beanFactory;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 带有requestMapping的方法
        RequestMapping requestMapping =
                AnnotatedElementUtils
                        .findMergedAnnotation(method, RequestMapping.class);
        // 没有RequestMapping的方法调用原来的方法
        if (Objects.isNull(requestMapping)) {
            return method.invoke(proxy, args);
        }
        // 路径，默认只取第一个
        String path = requestMapping.path()[0];
        // beanFactory的默认实现是DefaultListableBeanFactory,可以直接强制转成ListableBeanFactory
        ListableBeanFactory listableBeanFactory = (ListableBeanFactory) this.beanFactory;
        // 获取所有的RestTemplate的Bean
        Map<String, RestTemplate> restTemplateMap = listableBeanFactory
                .getBeansOfType(RestTemplate.class);
        // 获取标注有的@LoadBalanced的RestTemplate的BeanName
        String restTemplateName = restTemplateMap.keySet()
                .stream()
                .filter(beanName -> Objects
                        .nonNull(listableBeanFactory.findAnnotationOnBean(beanName,
                                LoadBalanced.class)))
                .findAny()
                .orElseThrow(() -> new RuntimeException("没有@LoadBalanced标注的RestTemplate"));
        // 获取标注有的@LoadBalanced的RestTemplate
        RestTemplate restTemplate = restTemplateMap.get(restTemplateName);
        StringBuilder paramBuilder = new StringBuilder("http://").append(serverName).append(path).append("?");
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[0];
            RequestParam requestParam = AnnotationUtils.getAnnotation(parameter, RequestParam.class);
            if (Objects.isNull(requestParam)) {
                continue;
            }
            String paramName = requestParam.value();
            String paramValue = String.valueOf(args[i]);
            paramBuilder.append("&").append(paramName).append("=").append(paramValue);
        }
        String url = paramBuilder.toString();
        RequestMethod[] requestMethods = requestMapping.method();
        // 默认取第一个
        HttpMethod httpMethod = HttpMethod.resolve(requestMethods[0].name());
        ResponseEntity<String> exchange = restTemplate.exchange(url, httpMethod, null,
                new ParameterizedTypeReference<String>() {
                });
        return exchange.getBody();
    }
}
