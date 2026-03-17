package it.alessandrohan.pollbffservice.proxy;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bff/api/v0")
public class ProxyController {
    private final ProxyService proxyService;

    public ProxyController(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    @RequestMapping("/**")
    public ResponseEntity<byte[]> proxy(
            HttpMethod method,
            HttpServletRequest request,
            @RequestHeader HttpHeaders headers,
            @RequestBody(required = false) byte[] body
    ) {
        String fullPath = request.getRequestURI();
        String prefix = request.getContextPath() + "/bff/api/v0";

        //original core service path
        String path = fullPath.startsWith(prefix) ? fullPath.substring(prefix.length()) : fullPath;

        if (path.isEmpty()) path = "/";

        String query = request.getQueryString();

        return proxyService.forward(method, path, query, headers, body);
    }
}
