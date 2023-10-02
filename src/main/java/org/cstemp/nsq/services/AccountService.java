/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cstemp.nsq.config.JwtService;
import org.cstemp.nsq.models.Enums;
import org.cstemp.nsq.models.Role;
import org.cstemp.nsq.models.relational.Profile;
import org.cstemp.nsq.models.relational.Token;
import org.cstemp.nsq.models.relational.User;
//import org.cstemp.nsq.payload.RegisterPayload;
//import org.cstemp.nsq.payload.RegisterRequest;
import org.cstemp.nsq.payload.AuthenticationRequest;
import org.cstemp.nsq.payload.AuthenticationResponse;
import org.cstemp.nsq.payload.RegisterPayload;
import org.cstemp.nsq.payload.UserPayload;
import org.cstemp.nsq.repos.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;

/**
 * @author chibuezeharry & MarcusDashe
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    ParticipantRepository participantRepository;

    @Autowired
    PortfolioRepository portfolioRepository;

    @Autowired
    AssessorRepository assessorRepository;

    @Autowired
    InternalVerifierRepository internalVerifierRepository;

    @Autowired
    CentreRepository centreRepository;

    @Autowired
    ExternalVerifierRepository externalVerifierRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtService jwtService;


    @Autowired
    TokenRepository tokenRepository;

    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> registerUser(RegisterPayload.RegisterRequest registerRequest) {
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            return ResponseEntity
                    .badRequest()
                    .body(new RegisterPayload.RegisterErrorResponse("Passwords don't match!"));
        }

        if (userRepository.existsByPhone(registerRequest.getPhone())) {
            return ResponseEntity
                    .badRequest()
                    .body(new RegisterPayload.RegisterErrorResponse("Phone Number is already in use!"));
        }

//        Role userRole = parseUserRole(registerRequest.getRole());
        Role userRole = Role.valueOf(registerRequest.getRole());

        if (userRole == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new RegisterPayload.RegisterErrorResponse("Invalid Role!"));
        }

//        if (!registerRequest.getRole().equalsIgnoreCase("TRAINEE") && userRepository.existsByEmail(registerRequest.getEmail())) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(new RegisterPayload.RegisterErrorResponse("Email Address is already in use!"));
//        }
        if(userRole != Role.TRAINEE && userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new RegisterPayload.RegisterErrorResponse("Email Address is already in use!"));
        }


        var user = User.builder()
                .firstname(registerRequest.getFirstName())
                        .lastname(registerRequest.getLastName())
                                        .password(passwordEncoder.encode(registerRequest.getPassword()))
                                                        .build();
//        User user = new User();

        BeanUtils.copyProperties(registerRequest, user);

        if (userRole == Role.TRAINEE) {
            user.setEmail(registerRequest.getPhone() + "@cstemp.org");
        }
        if (userRole == Role.CENTRE_ADMIN) {
            registerRequest.setRole(String.valueOf(Role.valueOf("CENTRE_ADMIN")));
        }

//        user.setPassword(encoder.encode(registerRequest.getPassword()));
        user.setRole(userRole);
//                .addRole(registerRequest.getRole()); // set role to predefined register request user role... usually TRAINEE.

        user.setStatus(true);
        user.setVerified(false);

        User savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);

        arrangeAccount(savedUser);

//        Authentication authentication = authenticationManager
//                .authenticate(new UsernamePasswordAuthenticationToken(registerRequest.getPhone(), registerRequest.getPassword()));

//        SecurityContextHolder.getContext().setAuthentication(authentication);

//        String accessToken = jwtService.generateJwtToken(authentication);

//        UserPayload.UserResponse userResponse = new UserPayload.UserResponse(savedUser);

//        userResponse.setRoles(Set.of(userRole.name()));

//        RegisterPayload.RegisterResponse response = new RegisterPayload.RegisterResponse(accessToken, userResponse);

//        return ResponseEntity.ok(response);
        return ResponseEntity.ok(
                AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build()
        );
    }


    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    public ResponseEntity<?> sendPasswordResetToken(String username) {
        return null;
    }

    public ResponseEntity<?> validateResetPasswordToken(String token) {
        return null;
    }

    public ResponseEntity<?> resetPassword(String password, String confirmPassword, String token) {
        return null;
    }

    public void arrangeAccount(User user) {

        // get roles in a set and populate the user.
        Profile profile = profileRepository.findByUser(user).orElse(new Profile());

        profile.setUser(user);
//        profile.

        profileRepository.save(profile);

    }

    private Role parseUserRole(String role){
        try{
            return Role.valueOf(role.toLowerCase());
        } catch (IllegalArgumentException e){
            return null;
        }
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(Enums.TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
