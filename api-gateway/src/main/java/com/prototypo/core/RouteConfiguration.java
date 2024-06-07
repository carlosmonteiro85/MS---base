// package com.prototypo.core;

// import org.springframework.cloud.gateway.route.RouteLocator;
// import org.springframework.cloud.gateway.route.builder.BooleanSpec;
// import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// import com.prototypo.filter.AuthenticationFilter;

// @Configuration
// class RouteConfiguration {

//     @Bean
//     public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//         return builder.routes()
//                 .route("api-auth-abertos", r -> r.path("/api-auth/api-auth/login", "/api-auth/api-auth/auth/**").uri("lb://api-auth"))
//                 .route("api-auth-fechados", r -> ( r.path("/api-auth/api-auth/dominios/**", "/api-auth/api-auth/users/**").uri("lb://api-auth")).filters(f -> f.filter(new AuthenticationFilter())))
//                 .route("site-auth-abertos", r -> r.path("/projeta/css/**").uri("lb://site"))
//                 .route("front-api", r -> r.path("/**").uri("lb://front-api"))
//                 .build();
//     }
// }
