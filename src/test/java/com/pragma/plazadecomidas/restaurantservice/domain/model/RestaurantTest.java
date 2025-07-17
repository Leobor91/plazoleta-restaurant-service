package com.pragma.plazadecomidas.restaurantservice.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RestaurantTest {

    private Restaurant restaurant;
    private final Long id = 1L;
    private final String name = "El Sabor Colombiano";
    private final String nit = "900123456";
    private final String address = "Av. Siempre Viva 742";
    private final String phoneNumber = "+573101234567";
    private final String urlLogo = "http://el_sabor_colombiano.com/logo.png";
    private final Long ownerId = 101L;

    @BeforeEach
    void setUp() {

        restaurant = new Restaurant(
                id,
                name,
                nit,
                address,
                phoneNumber,
                urlLogo,
                ownerId,
                null
        );
    }

    @Test
    @DisplayName("Should create a Restaurant object with all arguments constructor")
    void constructor_AllArguments_ShouldCreateObject() {
        assertNotNull(restaurant);
        assertEquals(id, restaurant.getId());
        assertEquals(name, restaurant.getName());
        assertEquals(nit, restaurant.getNit());
        assertEquals(address, restaurant.getAddress());
        assertEquals(phoneNumber, restaurant.getPhoneNumber());
        assertEquals(urlLogo, restaurant.getUrlLogo());
        assertEquals(ownerId, restaurant.getOwnerId());
        assertNull(restaurant.getOwnerName());
    }

    @Test
    @DisplayName("Should create a Restaurant object with no arguments constructor")
    void constructor_NoArguments_ShouldCreateObject() {
        Restaurant emptyRestaurant = new Restaurant();
        assertNotNull(emptyRestaurant);
        assertNull(emptyRestaurant.getId());
        assertNull(emptyRestaurant.getName());
    }


    @Test
    @DisplayName("Should set and get ID correctly")
    void setId_ShouldSetAndGetId() {
        Long newId = 2L;
        restaurant.setId(newId);
        assertEquals(newId, restaurant.getId());
    }

    @Test
    @DisplayName("Should set and get Name correctly")
    void setName_ShouldSetAndGetName() {
        String newName = "Nuevo Restaurante";
        restaurant.setName(newName);
        assertEquals(newName, restaurant.getName());
    }

    @Test
    @DisplayName("Should set and get NIT correctly")
    void setNit_ShouldSetAndGetNit() {
        String newNit = "900987654";
        restaurant.setNit(newNit);
        assertEquals(newNit, restaurant.getNit());
    }

    @Test
    @DisplayName("Should set and get Address correctly")
    void setAddress_ShouldSetAndGetAddress() {
        String newAddress = "Carrera 1 #2-3";
        restaurant.setAddress(newAddress);
        assertEquals(newAddress, restaurant.getAddress());
    }

    @Test
    @DisplayName("Should set and get Phone Number correctly")
    void setPhoneNumber_ShouldSetAndGetPhoneNumber() {
        String newPhoneNumber = "+573209876543";
        restaurant.setPhoneNumber(newPhoneNumber);
        assertEquals(newPhoneNumber, restaurant.getPhoneNumber());
    }

    @Test
    @DisplayName("Should set and get URL Logo correctly")
    void setUrlLogo_ShouldSetAndGetUrlLogo() {
        String newUrlLogo = "http://newlogo.png";
        restaurant.setUrlLogo(newUrlLogo);
        assertEquals(newUrlLogo, restaurant.getUrlLogo());
    }

    @Test
    @DisplayName("Should set and get Owner ID correctly")
    void setOwnerId_ShouldSetAndGetOwnerId() {
        Long newOwnerId = 202L;
        restaurant.setOwnerId(newOwnerId);
        assertEquals(newOwnerId, restaurant.getOwnerId());
    }

    @Test
    @DisplayName("Should set and get Owner Name correctly")
    void setOwnerName_ShouldSetAndGetOwnerName() {
        String newOwnerName = "Nuevo Propietario";
        restaurant.setOwnerName(newOwnerName);
        assertEquals(newOwnerName, restaurant.getOwnerName());
    }

    @Test
    @DisplayName("Should use Lombok's builder pattern correctly")
    void builder_ShouldCreateObject() {
        String builtOwnerName = "Built Owner";
        Restaurant builtRestaurant = Restaurant.builder()
                .id(id)
                .name(name)
                .nit(nit)
                .address(address)
                .phoneNumber(phoneNumber)
                .urlLogo(urlLogo)
                .ownerId(ownerId)
                .ownerName(builtOwnerName)
                .build();

        assertNotNull(builtRestaurant);
        assertEquals(id, builtRestaurant.getId());
        assertEquals(name, builtRestaurant.getName());
        assertEquals(nit, builtRestaurant.getNit());
        assertEquals(address, builtRestaurant.getAddress());
        assertEquals(phoneNumber, builtRestaurant.getPhoneNumber());
        assertEquals(urlLogo, builtRestaurant.getUrlLogo());
        assertEquals(ownerId, builtRestaurant.getOwnerId());
        assertEquals(builtOwnerName, builtRestaurant.getOwnerName());
    }

    @Test
    @DisplayName("Should correctly use toBuilder pattern to modify fields")
    void toBuilder_ShouldModifyFields() {
        String updatedName = "Updated Name";
        String updatedAddress = "Updated Address";
        Restaurant modifiedRestaurant = restaurant.toBuilder()
                .name(updatedName)
                .address(updatedAddress)
                .build();

        assertNotNull(modifiedRestaurant);
        assertEquals(updatedName, modifiedRestaurant.getName());
        assertEquals(updatedAddress, modifiedRestaurant.getAddress());

        assertEquals(id, modifiedRestaurant.getId());
        assertEquals(nit, modifiedRestaurant.getNit());
        assertEquals(phoneNumber, modifiedRestaurant.getPhoneNumber());
        assertEquals(urlLogo, modifiedRestaurant.getUrlLogo());
        assertEquals(ownerId, modifiedRestaurant.getOwnerId());
        assertNull(modifiedRestaurant.getOwnerName());
    }

    @Test
    @DisplayName("Should return correct string representation with toString()")
    void toString_ShouldReturnCorrectString() {

        assertTrue(restaurant.toString().contains("id=" + id));
        assertTrue(restaurant.toString().contains("name=" + name));
        assertTrue(restaurant.toString().contains("nit=" + nit));
        assertTrue(restaurant.toString().contains("address=" + address));
        assertTrue(restaurant.toString().contains("phoneNumber=" + phoneNumber));
        assertTrue(restaurant.toString().contains("urlLogo=" + urlLogo));
        assertTrue(restaurant.toString().contains("ownerId=" + ownerId));
        assertTrue(restaurant.toString().contains("ownerName=null")); // o el valor que esperes
    }

    @Test
    @DisplayName("Should return true for equal objects with equals() and same hashCode()")
    void equalsAndHashCode_ShouldReturnTrueForEqualObjects() {
        Restaurant anotherRestaurant = new Restaurant(
                id,
                name,
                nit,
                address,
                phoneNumber,
                urlLogo,
                ownerId,
                null
        );
        assertEquals(restaurant, anotherRestaurant);
        assertEquals(restaurant.hashCode(), anotherRestaurant.hashCode());
    }

    @Test
    @DisplayName("Should return false for non-equal objects with equals()")
    void equals_ShouldReturnFalseForNonEqualObjects() {
        Restaurant differentRestaurant = new Restaurant(
                2L, // Different ID
                name,
                nit,
                address,
                phoneNumber,
                urlLogo,
                ownerId,
                null
        );
        assertNotEquals(restaurant, differentRestaurant);
    }
}
