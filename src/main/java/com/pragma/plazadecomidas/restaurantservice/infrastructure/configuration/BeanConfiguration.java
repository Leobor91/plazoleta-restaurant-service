package com.pragma.plazadecomidas.restaurantservice.infrastructure.configuration;



import com.pragma.plazadecomidas.restaurantservice.application.mapper.IUserResponseMapper;
import com.pragma.plazadecomidas.restaurantservice.domain.spi.IAuthService;
import com.pragma.plazadecomidas.restaurantservice.infrastructure.out.jpa.adapter.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class BeanConfiguration {

    @Bean
    public IAuthService authService(
            WebClient.Builder webClientBuilder,
            @Value("${spring.auth.service.host}") String authServiceHost,
            @Value("${spring.auth.service.url}") String authServiceUrl,
            IUserResponseMapper userResponseMapper
    ) {
        return new AuthServiceImpl(webClientBuilder, authServiceHost, authServiceUrl, userResponseMapper);
    }

}