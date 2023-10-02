/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.payload;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import org.cstemp.nsq.models.Role;


/**
 *
 * @author chibuezeharry & MarcusDashe
 */
public class RegisterPayload {

    @Data
    @Builder
    public static class RegisterRequest {

//        private String firstname;
//        private String lastname;
//        private String email;
//        private String password;
//        private Role role;


        @NotBlank(message = "First name is required")
        private String firstName;

        @NotBlank(message = "Last name is required")
        private String lastName;

        @Email
        private String email;

//        @NotBlank(message = "User Role is required")
        private String role;

        @NotBlank(message = "Phone Number is required")
        @Pattern(regexp = "[0-9]{11}", message = "Valid Phone Number Required (E.g: 08012345678)")
        private String phone;

        @NotBlank(message = "Password is required")
        private String password;

        @NotBlank(message = "Confirm Password is required")
        private String confirmPassword;

    }

    @Data
    public static class RegisterResponse {

        private String accessToken;
        private String tokenType = "Bearer";
        private UserPayload.UserResponse user;

        public RegisterResponse(String accessToken, UserPayload.UserResponse user) {
            this.accessToken = accessToken;
            this.user = user;
        }
    }

    @Data
    public static class RegisterErrorResponse {

        private Boolean status = false;

        private String message;

        public RegisterErrorResponse(String message) {
            this.message = message;
        }

    }
}
