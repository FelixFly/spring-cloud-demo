package top.felixfly.cloud.load.balance.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 服务提供端点
 *
 * @author FelixFly <chenglinxu@yeah.net>
 * @date 2020/2/4
 */
@RestController
public class EchoController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    @LoadBalanced
    private RestTemplate lbRestTemplate;

    @GetMapping("/{serveName}/echo/{message}")
    public String echo(@PathVariable String serveName,@PathVariable String message) {
        return restTemplate.getForObject("http://"+serveName+"/echo?message=" + message, String.class);
    }


    @GetMapping("/{serveName}/lb-echo/{message}")
    public String lbEcho(@PathVariable String serveName,@PathVariable String message) {
        return lbRestTemplate.getForObject("http://"+serveName+"/echo?message=" + message, String.class);
    }



}
