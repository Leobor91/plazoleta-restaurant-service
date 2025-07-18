package com.pragma.plazadecomidas.restaurantservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DomainModelTests {


    // --- Helper methods for Restaurant model ---
    private Restaurant createBaseRestaurant() {
        return Restaurant.builder()
                .id(1L)
                .name("Model Restaurant")
                .nit("111222333")
                .address("Model Address 123")
                .phoneNumber("3005551234")
                .urlLogo("http://model.com/logo.png")
                .ownerId(10L)
                .ownerName("Owner Test")
                .build();
    }

    // --- Tests for Restaurant Model ---

    @Test
    @DisplayName("Restaurant: Should create an instance using no-args constructor and setters")
    void restaurant_NoArgsConstructorAndSettersTest() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("No Args Restaurant");
        restaurant.setNit("123");
        restaurant.setAddress("Street");
        restaurant.setPhoneNumber("12345");
        restaurant.setUrlLogo("url.com");
        restaurant.setOwnerId(1L);
        restaurant.setOwnerName("Owner Name");

        assertNotNull(restaurant);
        assertEquals(1L, restaurant.getId());
        assertEquals("No Args Restaurant", restaurant.getName());
        assertEquals("123", restaurant.getNit());
        assertEquals("Street", restaurant.getAddress());
        assertEquals("12345", restaurant.getPhoneNumber());
        assertEquals("url.com", restaurant.getUrlLogo());
        assertEquals(1L, restaurant.getOwnerId());
        assertEquals("Owner Name", restaurant.getOwnerName());
    }

    @Test
    @DisplayName("Restaurant: Should create an instance using all-args constructor")
    void restaurant_AllArgsConstructorTest() {
        Restaurant restaurant = new Restaurant(
                1L, "All Args Restaurant", "456", "Ave", "54321", "url2.com", 2L, "Other Owner"
        );

        assertNotNull(restaurant);
        assertEquals(1L, restaurant.getId());
        assertEquals("All Args Restaurant", restaurant.getName());
        assertEquals("456", restaurant.getNit());
        assertEquals("Ave", restaurant.getAddress());
        assertEquals("54321", restaurant.getPhoneNumber());
        assertEquals("url2.com", restaurant.getUrlLogo());
        assertEquals(2L, restaurant.getOwnerId());
        assertEquals("Other Owner", restaurant.getOwnerName());
    }

    @Test
    @DisplayName("Restaurant: Should create an instance using the Builder pattern")
    void restaurant_BuilderTest() {
        Restaurant restaurant = createBaseRestaurant();

        assertNotNull(restaurant);
        assertEquals(1L, restaurant.getId());
        assertEquals("Model Restaurant", restaurant.getName());
        assertEquals("111222333", restaurant.getNit());
        assertEquals("Model Address 123", restaurant.getAddress());
        assertEquals("3005551234", restaurant.getPhoneNumber());
        assertEquals("http://model.com/logo.png", restaurant.getUrlLogo());
        assertEquals(10L, restaurant.getOwnerId());
        assertEquals("Owner Test", restaurant.getOwnerName());
    }

    @Test
    @DisplayName("Restaurant: Builder should handle null values for all fields")
    void restaurant_BuilderWithNullFieldsTest() {
        Restaurant restaurant = Restaurant.builder().build(); // All fields null

        assertNotNull(restaurant);
        assertNull(restaurant.getId());
        assertNull(restaurant.getName());
        assertNull(restaurant.getNit());
        assertNull(restaurant.getAddress());
        assertNull(restaurant.getPhoneNumber());
        assertNull(restaurant.getUrlLogo());
        assertNull(restaurant.getOwnerId());
        assertNull(restaurant.getOwnerName());
    }


    @Test
    @DisplayName("Restaurant: Equals and HashCode should work correctly for same objects")
    void restaurant_EqualsAndHashCode_SameObjects_ReturnTrue() {
        Restaurant r1 = createBaseRestaurant();
        Restaurant r2 = createBaseRestaurant();

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
        assertEquals(r1, r1);
        assertEquals(r1, r2);
        assertEquals(r2, r1);
    }

    @Test
    @DisplayName("Restaurant: Equals should return false for objects with different properties")
    void restaurant_Equals_DifferentObjects_ReturnFalse() {
        Restaurant base = createBaseRestaurant();

        assertNotEquals(base, base.toBuilder().id(99L).build());
        assertNotEquals(base, base.toBuilder().name("Diff Name").build());
        assertNotEquals(base, base.toBuilder().nit("Diff Nit").build());
        assertNotEquals(base, base.toBuilder().address("Diff Address").build());
        assertNotEquals(base, base.toBuilder().phoneNumber("Diff Phone").build());
        assertNotEquals(base, base.toBuilder().urlLogo("Diff URL").build());
        assertNotEquals(base, base.toBuilder().ownerId(999L).build());
        assertNotEquals(base, base.toBuilder().ownerName("Diff Owner Name").build());
    }

    @Test
    @DisplayName("Restaurant: Equals should return false if one object's field is null and other's is not")
    void restaurant_Equals_NullVsNonNullFields_ReturnFalse() {
        Restaurant r1 = createBaseRestaurant();
        Restaurant r2 = createBaseRestaurant().toBuilder().phoneNumber(null).build();
        assertNotEquals(r1, r2);

        Restaurant r3 = createBaseRestaurant().toBuilder().ownerName(null).build();
        assertNotEquals(r1, r3);
    }

    @Test
    @DisplayName("Restaurant: Equals should return true if both objects have null for same fields")
    void restaurant_Equals_BothNullFields_ReturnTrue() {
        Restaurant r1 = createBaseRestaurant().toBuilder().phoneNumber(null).ownerName(null).build();
        Restaurant r2 = createBaseRestaurant().toBuilder().phoneNumber(null).ownerName(null).build();
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    @DisplayName("Restaurant: ToString should return a non-null and non-empty string")
    void restaurant_ToStringTest() {
        Restaurant restaurant = createBaseRestaurant();
        String toStringResult = restaurant.toString();
        assertNotNull(toStringResult);
        assertFalse(toStringResult.isEmpty());
        assertTrue(toStringResult.contains("name=Model Restaurant"));
        assertTrue(toStringResult.contains("nit=111222333"));
    }

    @Test
    @DisplayName("Restaurant: toBuilder() should create a builder with current object's properties")
    void restaurant_ToBuilderTest() {
        Restaurant original = createBaseRestaurant();
        Restaurant.RestaurantBuilder builder = original.toBuilder();
        Restaurant built = builder.build();
        assertEquals(original, built);
        assertEquals(original.hashCode(), built.hashCode());

        Restaurant modifiedBuilt = builder.name("Updated Via Builder").build();
        assertNotEquals(original, modifiedBuilt);
        assertEquals("Updated Via Builder", modifiedBuilt.getName());
    }

    // --- Helper methods for User model ---
    private User createBaseUser() {
        return User.builder()
                .id(1L)
                .name("User Name")
                .lastName("User Lastname")
                .identityDocument("100000000")
                .phoneNumber("3001234567")
                .email("user@example.com")
                .password("hashed_password")
                .birthDate(LocalDate.of(1995, 5, 15))
                .roleId("1")
                .roleName("CLIENT")
                .build();
    }

    // --- Tests for User Model ---

    @Test
    @DisplayName("User: Should create an instance using no-args constructor and setters")
    void user_NoArgsConstructorAndSettersTest() {
        User user = new User();
        user.setId(1L);
        user.setName("No Args User");
        user.setLastName("No Args Lastname");
        user.setIdentityDocument("123");
        user.setPhoneNumber("456");
        user.setEmail("email@test.com");
        user.setPassword("pass");
        user.setBirthDate(LocalDate.of(2000,1,1));
        user.setRoleId("2");
        user.setRoleName("ADMIN");

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("No Args User", user.getName());
        assertEquals("No Args Lastname", user.getLastName());
        assertEquals("123", user.getIdentityDocument());
        assertEquals("456", user.getPhoneNumber());
        assertEquals("email@test.com", user.getEmail());
        assertEquals("pass", user.getPassword());
        assertEquals(LocalDate.of(2000,1,1), user.getBirthDate());
        assertEquals("2", user.getRoleId());
        assertEquals("ADMIN", user.getRoleName());
    }

    @Test
    @DisplayName("User: Should create an instance using all-args constructor")
    void user_AllArgsConstructorTest() {
        User user = new User(
                1L, "All Args User", "All Args Lastname", "1234", "789", "all@test.com", "pass2", LocalDate.of(1990,1,1), "3", "SUPERADMIN"
        );

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("All Args User", user.getName());
        assertEquals("All Args Lastname", user.getLastName());
        assertEquals("1234", user.getIdentityDocument());
        assertEquals("789", user.getPhoneNumber());
        assertEquals("all@test.com", user.getEmail());
        assertEquals("pass2", user.getPassword());
        assertEquals(LocalDate.of(1990,1,1), user.getBirthDate());
        assertEquals("3", user.getRoleId());
        assertEquals("SUPERADMIN", user.getRoleName());
    }

    @Test
    @DisplayName("User: Should create an instance using the Builder pattern")
    void user_BuilderTest() {
        User user = createBaseUser();

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("User Name", user.getName());
        assertEquals("User Lastname", user.getLastName());
        assertEquals("100000000", user.getIdentityDocument());
        assertEquals("3001234567", user.getPhoneNumber());
        assertEquals("user@example.com", user.getEmail());
        assertEquals("hashed_password", user.getPassword());
        assertEquals(LocalDate.of(1995, 5, 15), user.getBirthDate());
        assertEquals("1", user.getRoleId());
        assertEquals("CLIENT", user.getRoleName());
    }

    @Test
    @DisplayName("User: Builder should handle null values for all fields")
    void user_BuilderWithNullFieldsTest() {
        User user = User.builder().build(); // All fields null

        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getLastName());
        assertNull(user.getIdentityDocument());
        assertNull(user.getPhoneNumber());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getBirthDate());
        assertNull(user.getRoleId());
        assertNull(user.getRoleName());
    }

    @Test
    @DisplayName("User: Equals and HashCode should work correctly for same objects")
    void user_EqualsAndHashCode_SameObjects_ReturnTrue() {
        User u1 = createBaseUser();
        User u2 = createBaseUser();

        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
        assertEquals(u1, u1);
        assertEquals(u1, u2);
        assertEquals(u2, u2);
    }

    @Test
    @DisplayName("User: Equals should return false for objects with different properties")
    void user_Equals_DifferentObjects_ReturnFalse() {
        User base = createBaseUser();

        assertNotEquals(base, base.toBuilder().id(99L).build());
        assertNotEquals(base, base.toBuilder().name("Diff Name").build());
        assertNotEquals(base, base.toBuilder().lastName("Diff Lastname").build());
        assertNotEquals(base, base.toBuilder().identityDocument("Diff Doc").build());
        assertNotEquals(base, base.toBuilder().phoneNumber("Diff Phone").build());
        assertNotEquals(base, base.toBuilder().email("diff@email.com").build());
        assertNotEquals(base, base.toBuilder().password("diff_pass").build());
        assertNotEquals(base, base.toBuilder().birthDate(LocalDate.of(2000, 1, 1)).build());
        assertNotEquals(base, base.toBuilder().roleId("9").build());
        assertNotEquals(base, base.toBuilder().roleName("Diff Role").build());
    }

    @Test
    @DisplayName("User: Equals should return false if one object's field is null and other's is not")
    void user_Equals_NullVsNonNullFields_ReturnFalse() {
        User u1 = createBaseUser();
        User u2 = createBaseUser().toBuilder().phoneNumber(null).build();
        assertNotEquals(u1, u2);

        User u3 = createBaseUser().toBuilder().birthDate(null).build();
        assertNotEquals(u1, u3);
    }

    @Test
    @DisplayName("User: Equals should return true if both objects have null for same fields")
    void user_Equals_BothNullFields_ReturnTrue() {
        User u1 = createBaseUser().toBuilder().phoneNumber(null).birthDate(null).build();
        User u2 = createBaseUser().toBuilder().phoneNumber(null).birthDate(null).build();
        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
    }

    @Test
    @DisplayName("User: ToString should return a non-null and non-empty string")
    void user_ToStringTest() {
        User user = createBaseUser();
        String toStringResult = user.toString();
        assertNotNull(toStringResult);
        assertFalse(toStringResult.isEmpty());
        assertTrue(toStringResult.contains("name=User Name"));
        assertTrue(toStringResult.contains("email=user@example.com"));
    }

    @Test
    @DisplayName("User: toBuilder() should create a builder with current object's properties")
    void user_ToBuilderTest() {
        User original = createBaseUser();
        User.UserBuilder builder = original.toBuilder();
        User built = builder.build();
        assertEquals(original, built);
        assertEquals(original.hashCode(), built.hashCode());

        User modifiedBuilt = builder.name("Updated Via Builder").build();
        assertNotEquals(original, modifiedBuilt);
        assertEquals("Updated Via Builder", modifiedBuilt.getName());
    }
}

