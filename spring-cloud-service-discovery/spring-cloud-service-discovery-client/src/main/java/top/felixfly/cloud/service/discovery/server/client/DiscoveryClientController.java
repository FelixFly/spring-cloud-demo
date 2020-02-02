package top.felixfly.cloud.service.discovery.server.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * {@link DiscoveryClient} 端点
 *
 * @author xcl <xcl@winning.com.cn>
 * @date 2020/2/1
 */
@RestController
public class DiscoveryClientController {

    @Autowired
    private DiscoveryClient discoveryClient;


    @GetMapping("/services")
    public List<String> getServices() {
        return this.discoveryClient.getServices();
    }

    @GetMapping("/services/{serviceId}")
    public List<ServiceInstance> getInstances(@PathVariable String serviceId) {
        return this.discoveryClient.getInstances(serviceId);
    }


    @GetMapping("/services/{serviceId}/{instanceId}")
    public ServiceInstance getInstance(@PathVariable String serviceId,
                                       @PathVariable String instanceId) {
        return this.discoveryClient.getInstances(serviceId).stream()
                .filter(serviceInstance -> serviceInstance.getInstanceId().equals(instanceId))
                .findAny()
                .orElseThrow(() -> new RuntimeException("无此[" + instanceId + "]对应的实例服务"));
    }
}
