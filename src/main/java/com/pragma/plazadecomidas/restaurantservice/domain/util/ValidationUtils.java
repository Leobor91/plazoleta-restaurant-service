package com.pragma.plazadecomidas.restaurantservice.domain.util;


import com.pragma.plazadecomidas.restaurantservice.domain.model.MessageEnum;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;

@Component
public class ValidationUtils {

    public boolean isValid(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public  boolean isValidNameStructure(String name) {
        if (name == null) return false;
        Matcher matcher = java.util.regex.Pattern.compile(MessageEnum.NAME_STRUCTURE.getMessage()).matcher(name);
        return matcher.matches();
    }

    public  boolean isValidPhoneStructure(String phone) {
        if (phone == null) return false;
        Matcher matcher = java.util.regex.Pattern.compile(MessageEnum.PHONE_STRUCTURE.getMessage()).matcher(phone);
        return matcher.matches();
    }

    public boolean containsOnlyNumbers(String nit) {
        if (nit == null) return false;
        Matcher matcher = java.util.regex.Pattern.compile(MessageEnum.NIT_STRUCTURE.getMessage()).matcher(nit);
        return matcher.matches();
    }

    public boolean isValidUrl(String url){
        return isValid(url);
    }

    public boolean isValidateRole(String userRole, String roleName){
        if (userRole == null || roleName == null) return false;
        return userRole.equalsIgnoreCase(roleName);
    }

    public boolean isValidName(String name) {
        return isValid(name);
    }

    public boolean isValidNit(String nit) {
        return isValid(nit);
    }

    public boolean isValidAdress(String address) {
        return isValid(address);
    }

    public boolean isValidPhoneNumber(String phoneNumber) {
        return isValid(phoneNumber);
    }

    public boolean isValidOwnerId(String ownerId) {
        return isValid(ownerId);
    }
}
