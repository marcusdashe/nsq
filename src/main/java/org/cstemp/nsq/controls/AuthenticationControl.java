/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.controls;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cstemp.nsq.config.JwtService;
import org.cstemp.nsq.models.relational.User;
import org.cstemp.nsq.payload.*;
import org.cstemp.nsq.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


/**
 *
 * @author chibuezeharry & MarcusDashe
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationControl {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtUtils;

    @Autowired
    AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterPayload.RegisterRequest registerRequest) {

        try {
            return accountService.registerUser(registerRequest);
        } catch (Exception e) {
            // Handle the exception and return an appropriate response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the registration request. "+ e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(accountService.login(request));
    }

//    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
//
//        Authentication authentication;
//        try {
//            authentication = authenticationManager
//                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
//
//        } catch (AuthenticationException ex) {
//            return ResponseEntity.badRequest().body(new BaseResponse(false, "Check username and password", ex.getMessage()));
//        }
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        String jwt = jwtUtils.generateJwtToken(authentication);
//
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//
//        if (userDetails == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginPayload.LoginErrorResponse());
//        }
//
//        User user = userDetails.getUser();
//
//        return ResponseEntity.ok(new LoginPayload.LoginResponse(jwt, user));
//    }



//    @PostMapping("/register")
//    public ResponseEntity<AuthenticationResponse> register(
//            @RequestBody RegisterRequest request
//    ) {
//        return ResponseEntity.ok(service.register(request));
//    }
}
