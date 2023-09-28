/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.services;


import lombok.extern.slf4j.Slf4j;
import org.cstemp.nsq.config.JwtService;
import org.cstemp.nsq.models.Role;
import org.cstemp.nsq.models.relational.Profile;
import org.cstemp.nsq.models.relational.User;
//import org.cstemp.nsq.payload.RegisterPayload;
//import org.cstemp.nsq.payload.RegisterRequest;
import org.cstemp.nsq.payload.RegisterPayload;
import org.cstemp.nsq.payload.UserPayload;
import org.cstemp.nsq.repos.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author chibuezeharry & MarcusDashe
 */
@Service
@Slf4j
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
    PasswordEncoder encoder;

    @Autowired
    JwtService jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;

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

        Role userRole = parseUserRole(registerRequest.getRole());

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

        User user = new User();

        BeanUtils.copyProperties(registerRequest, user);

        if (userRole == Role.TRAINEE) {
            user.setEmail(registerRequest.getPhone() + "@cstemp.org");
        }
        if (userRole == Role.CENTRE_ADMIN) {
            registerRequest.setRole("CENTRE_ADMIN");
        }

        user.setPassword(encoder.encode(registerRequest.getPassword()));
        user.setRole(userRole);
//                .addRole(registerRequest.getRole()); // set role to predefined register request user role... usually TRAINEE.

        user.setStatus(true);
        user.setVerified(false);

        User savedUser = userRepository.save(user);

        arrangeAccount(savedUser);

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(registerRequest.getPhone(), registerRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.generateJwtToken(authentication);

        UserPayload.UserResponse userResponse = new UserPayload.UserResponse(savedUser);

        userResponse.setRoles(Set.of(userRole.name()));

        RegisterPayload.RegisterResponse response = new RegisterPayload.RegisterResponse(accessToken, userResponse);

        return ResponseEntity.ok(response);
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

}
