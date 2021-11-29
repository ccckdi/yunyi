package com.cy.yunyi.gateway.filter;

import com.cy.yunyi.common.constant.AuthConstant;
import com.cy.yunyi.gateway.config.IgnoreUrlsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

/**
 * @Author: chx
 * @Description: 移除白名单的请求头
 * @DateTime: 2021/11/29 15:57
 **/
@Component
public class IgnoreUrlsRemoveJwtFilter implements WebFilter {
    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        PathMatcher pathMatcher = new AntPathMatcher();
        //白名单移除JWT请求头
        List<String> ignoreUrls = ignoreUrlsConfig.getUrls();
        for (String ignoreUrl : ignoreUrls) {
            if (pathMatcher.match(ignoreUrl,uri.getPath())){
                request = exchange.getRequest().mutate().header(AuthConstant.JWT_TOKEN_HEADER, "").build();
                exchange = exchange.mutate().request(request).build();
            }
        }
        return chain.filter(exchange);
    }
}
