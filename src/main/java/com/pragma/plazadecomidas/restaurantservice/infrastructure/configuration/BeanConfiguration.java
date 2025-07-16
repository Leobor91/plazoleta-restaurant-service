package com.pragma.plazadecomidas.restaurantservice.infrastructure.configuration;


import com.pragma.plazadecomidas.restaurantservice.domain.api.IRestaurantServicePort;
import com.pragma.plazadecomidas.restaurantservice.infrastructure.output.adapter.AuthServiceImpl;
import com.pragma.plazadecomidas.restaurantservice.domain.api.impl.RestaurantServicePortImpl;
import com.pragma.plazadecomidas.restaurantservice.domain.spi.IAuthService;
import com.pragma.plazadecomidas.restaurantservice.domain.spi.IRestaurantPersistencePort;
import com.pragma.plazadecomidas.restaurantservice.domain.util.ValidationUtils;
import com.pragma.plazadecomidas.restaurantservice.infrastructure.output.adapter.RestaurantJpaAdapter;
import com.pragma.plazadecomidas.restaurantservice.infrastructure.output.jpa.mapper.IRestaurantEntityMapper;
import com.pragma.plazadecomidas.restaurantservice.infrastructure.output.jpa.repository.IRestaurantRepository;
import com.pragma.plazadecomidas.restaurantservice.application.mapper.IUserResponseMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class BeanConfiguration {


    @Bean
    public IRestaurantPersistencePort restaurantPersistencePort(IRestaurantRepository restaurantRepository, IRestaurantEntityMapper restaurantEntityMapper) {
        return new RestaurantJpaAdapter(restaurantRepository, restaurantEntityMapper);
    }

    @Bean
    public ValidationUtils validationUtils() {
        return new ValidationUtils();
    }

    @Bean
    public IAuthService authService(
            WebClient.Builder webClientBuilder,
            @Value("${spring.auth.service.host}") String authServiceHost,
            @Value("${spring.auth.service.url}") String authServiceUrl,
            IUserResponseMapper userResponseMapper
    ) {
        return new AuthServiceImpl(webClientBuilder, authServiceHost, authServiceUrl, userResponseMapper);
    }

    @Bean
    public IRestaurantServicePort restaurantServicePort(ValidationUtils validationUtils, IRestaurantPersistencePort restaurantPersistencePort, IAuthService authService) {
        return new RestaurantServicePortImpl(validationUtils, restaurantPersistencePort, authService);
    }

}