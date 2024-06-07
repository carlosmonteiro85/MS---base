package com.prototypo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

import com.prototypo.api.TokenInvalidoException;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final WebClient.Builder webClientBuilder;

    @Value("${client.urlapi-auth}")
    private String  userServiceUrl;

    public Mono<Void> validarToken(ServerWebExchange exchange, GatewayFilterChain chain, String token) {
        return webClientBuilder.build()
                .get()
                .uri( userServiceUrl + "?token=" + token)
                .retrieve()
                .bodyToMono(Void.class) 
                .flatMap(response -> chain.filter(exchange))
                .onErrorMap(ex -> new TokenInvalidoException("Token inválido")); 
    }
}