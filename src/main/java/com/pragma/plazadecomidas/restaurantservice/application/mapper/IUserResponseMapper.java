package com.pragma.plazadecomidas.restaurantservice.application.mapper;

import com.pragma.plazadecomidas.restaurantservice.application.dto.response.UserResponseDto;
import com.pragma.plazadecomidas.restaurantservice.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUserResponseMapper {

    @Mapping(source = "roleName", target = "role")
    UserResponseDto toResponseDto(User user);

    @Mapping(source = "role", target = "roleName")
    User toUser(UserResponseDto userResponseDto);
}
