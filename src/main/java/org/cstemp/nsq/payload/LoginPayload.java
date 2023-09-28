/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.cstemp.nsq.models.relational.User;

import java.util.Set;


/**
 *
 * @author chibuezeharry & MarcusDashe
 */


public class LoginPayload {

    @Data
    public static class LoginRequest {

//        @NotBlank(message = "Valid Session Key is required")
//        private String tsk;
//
        @NotBlank(message = "Email is required")
        private String email;

        @NotBlank(message = "Password is required")
        private String password;
    }

    @Data
    public static class LoginResponse {

        private String accessToken;
        private String tokenType = "Bearer";
        private UserPayload.UserResponse user;

        public LoginResponse(String accessToken, User user) {
            this.accessToken = accessToken;

            this.user = new UserPayload.UserResponse(user);

//            this.user.setRoles();
            this.user.setRoles(Set.copyOf(user.getRolesList()));

        }
    }

    @Data
    public static class LoginErrorResponse {

        private Boolean status = false;
        private String message = "Failed Due To Invalid Credential";

    }
}
