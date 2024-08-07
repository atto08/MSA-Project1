package com.sparta.msa_exam.gateway;


import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class PreFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 요청 로깅
        System.out.println("Request: " + exchange.getRequest().getPath());
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {  // 필터의 순서를 지정합니다.
        return -1;  // 필터 순서를 가장 높은 우선 순위로 설정합니다.
    }
}


