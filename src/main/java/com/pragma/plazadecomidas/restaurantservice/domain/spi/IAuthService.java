package com.pragma.plazadecomidas.restaurantservice.domain.spi;

import com.pragma.plazadecomidas.restaurantservice.domain.model.User;

import java.util.Optional;

public interface IAuthService {

    Optional<User> findById(Long id);
}
