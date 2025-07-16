package com.pragma.plazadecomidas.restaurantservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class User {

    private Long id;

    private String name;

    private String lastName;

    private String identityDocument;

    private String phoneNumber;

    private String email;

    private String password;

    private LocalDate birthDate;

    private String roleId;

    private String roleName;

}
