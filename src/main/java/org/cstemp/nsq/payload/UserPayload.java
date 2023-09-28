/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.payload;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.cstemp.nsq.models.relational.User;
import org.springframework.beans.BeanUtils;

;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author chibuezeharry
 */
public class UserPayload {

    @Data
    public static class UserRequest {

        @NotBlank(message = "Firstname is required")
        private String firstName;

        @NotBlank(message = "Lastname is required")
        private String lastName;

        @Email
        private String email;

        @NotBlank(message = "Phone Number is required")
        private String phone;

    }

    @Data
    public static class UserContactRequest {

        @Email
        private String email;

        @NotBlank(message = "Phone Number is required")
        private String phone;

    }

    @Data
    public static class PasswordChangeRequest {

        @NotBlank(message = "Old Password is required")
        private String oldPassword;

        @NotBlank(message = "New Password is required")
        private String newPassword;

        @NotBlank(message = "Password Confirmation is required")
        private String confirmPassword;

    }

    @Data
    public static class UserResponse {

        private String id;

        private String photoUrl;

        private String firstName;

        private String lastName;

        private String email;

        private String phone;

        private Set<String> roles = new HashSet<>();

        public String getName() {
            return (firstName + " " + lastName);
        }

        public UserResponse() {
        }

        public UserResponse(User user) {
            BeanUtils.copyProperties(user, this);
        }

    }
}
