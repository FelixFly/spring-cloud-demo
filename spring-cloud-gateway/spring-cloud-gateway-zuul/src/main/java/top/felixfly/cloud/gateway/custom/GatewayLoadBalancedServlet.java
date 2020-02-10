package top.felixfly.cloud.gateway.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 服务网关基于LoadBalanced Servlet实现
 *
 * @author FelixFly <chenglinxu@yeah.net>
 * @date 2020/2/8
 */
@WebServlet(name = "gatewaylb", urlPatterns = "/gatewaylb/*")
public class GatewayLoadBalancedServlet extends HttpServlet {

    @Autowired
    @LoadBalanced
    private RestTemplate restTemplate;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 访问地址的Uri:/{service-name}/${service-uri}
        String pathInfo = req.getPathInfo();
        // 目标地址
        String targetURL = "http://" + pathInfo.substring(1) + "?" + req.getQueryString();
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
}
