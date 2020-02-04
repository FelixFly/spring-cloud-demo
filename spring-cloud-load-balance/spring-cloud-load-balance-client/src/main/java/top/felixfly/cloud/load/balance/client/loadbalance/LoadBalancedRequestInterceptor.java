package top.felixfly.cloud.load.balance.client.loadbalance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Random;

/**
 * 自定义实现负载均衡拦截器
 *
 * @author FelixFly <chenglinxu@yeah.net>
 * @date 2020/2/4
 */
@Component
public class LoadBalancedRequestInterceptor implements ClientHttpRequestInterceptor {

    @Autowired
    private DiscoveryClient discoveryClient;


    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        URI uri = request.getURI();
        // 主机名
        String host = uri.getHost();
        List<ServiceInstance> instances = this.discoveryClient.getInstances(host);
        // 暂不考虑没有服务的情况
        int size = instances.size();
        // 随机
        int index = new Random().nextInt(size);
        ServiceInstance instance = instances.get(index);
        String url = (instance.isSecure() ? "https://" : "http://") +
                instance.getHost() + ":" + instance.getPort()+uri.getPath()+"?"+uri.getQuery();
        // 客户端调用
        ClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        ClientHttpRequest clientHttpRequest = requestFactory.createRequest(URI.create(url), request.getMethod());
        return clientHttpRequest.execute();
    }
}
