package com.pragma.plazadecomidas.restaurantservice.infrastructure.out.jpa.adapter;

import com.pragma.plazadecomidas.restaurantservice.application.dto.response.UserResponseDto;
import com.pragma.plazadecomidas.restaurantservice.application.mapper.IUserResponseMapper;
import com.pragma.plazadecomidas.restaurantservice.domain.exception.PersonalizedException;
import com.pragma.plazadecomidas.restaurantservice.domain.exception.PersonalizedNotFoundException;
import com.pragma.plazadecomidas.restaurantservice.domain.model.MessageEnum;
import com.pragma.plazadecomidas.restaurantservice.domain.model.User;
import com.pragma.plazadecomidas.restaurantservice.domain.spi.IAuthService;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.Optional;


public class AuthServiceImpl implements IAuthService {

    private final String authServiceUrl;
    private final WebClient webClient;
    private final IUserResponseMapper userResponseMapper;

    public AuthServiceImpl(
            WebClient.Builder webClientBuilder,
            String authServiceHost,
            String authServiceUrl,
            IUserResponseMapper userResponseMapper
    ) {
        this.authServiceUrl = authServiceUrl;
        this.userResponseMapper = userResponseMapper;
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .doOnConnected(connection -> connection
                        .addHandlerLast(new ReadTimeoutHandler(5))
                        .addHandlerLast(new WriteTimeoutHandler(5)));
        this.webClient = webClientBuilder
                .baseUrl(authServiceHost)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Override
    public Optional<User> findById(Long id) {
        UserResponseDto userResponseDto = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(authServiceUrl)
                        .queryParam("userId", id)
                        .build()
                )
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        clientResponse -> Mono.error(new PersonalizedNotFoundException(MessageEnum.ERROR_4XX.getMessage()))
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        clientResponse -> Mono.error(new PersonalizedException(MessageEnum.ERROR_5XX.getMessage()))
                )
                .bodyToMono(UserResponseDto.class)
                .block();
        return Optional.ofNullable(userResponseMapper.toUser(userResponseDto));
    }

}
