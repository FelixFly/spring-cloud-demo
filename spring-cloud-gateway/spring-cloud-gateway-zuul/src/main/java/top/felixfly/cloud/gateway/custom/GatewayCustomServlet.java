package top.felixfly.cloud.gateway.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * 服务网关Servlet实现
 *
 * @author FelixFly <chenglinxu@yeah.net>
 * @date 2020/2/8
 */
@WebServlet(name = "gateway", urlPatterns = "/gateway/*")
public class GatewayCustomServlet extends HttpServlet {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 访问地址的Uri:/{service-name}/${service-uri}
        String pathInfo = req.getPathInfo();
        String[] paths = StringUtils.split(pathInfo.substring(1), "/");
        // 服务名称
        String serverName = paths[0];
        // 服务访问地址
        String serverURI = paths[1];
        // 获取服务实例
        ServiceInstance serviceInstance = choose(serverName);
        // 目标地址
        String targetURL = createTargetURL(serviceInstance, serverURI, req);
        RestTemplate restTemplate = new RestTemplate();
        String method = req.getMethod();
        ResponseEntity<byte[]> responseEntity = restTemplate
                .exchange(targetURL, HttpMethod.resolve(method), null, byte[].class);
        // 返回状态
        resp.setStatus(responseEntity.getStatusCodeValue());
        // 媒体类型
        HttpHeaders headers = responseEntity.getHeaders();
        MediaType contentType = headers.getContentType();
        if (Objects.nonNull(contentType)) {
            resp.setHeader(HttpHeaders.CONTENT_TYPE, contentType.toString());
        }
        // 返回内容
        byte[] body = responseEntity.getBody();
        if (Objects.nonNull(body)) {
            ServletOutputStream outputStream = resp.getOutputStream();
            outputStream.write(responseEntity.getBody());
            outputStream.flush();
        }
    }

    private String createTargetURL(ServiceInstance instance, String serverURI,
                                   HttpServletRequest req) {
        StringBuilder urlBuilder = new StringBuilder(64);
        return urlBuilder.append(instance.isSecure() ? "https" : "http").append("://")
                .append(instance.getHost()).append(":").append(instance.getPort())
                .append(serverURI).append("?").append(req.getQueryString())
                .toString();
    }

    private ServiceInstance choose(String serverName) {
        List<ServiceInstance> instances = this.discoveryClient.getInstances(serverName);
        if (CollectionUtils.isEmpty(instances)) {
            throw new RuntimeException("无服务实例");
        }
        int index = new Random().nextInt(instances.size());
        return instances.get(index);
    }
}
