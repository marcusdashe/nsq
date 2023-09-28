///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package org.cstemp.nsq.services;
//
//
//import org.cstemp.nsq.models.Enums;
//import org.cstemp.nsq.models.Role;
//import org.cstemp.nsq.models.relational.User;
//import org.cstemp.nsq.payload.RegisterPayload;
//import org.cstemp.nsq.repos.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.cstemp.nsq.models.Enums.Role.ASSESSOR;
//import static org.cstemp.nsq.models.Enums.Role.TRAINEE;
////import static org.cstemp.nsq.models.relational.User.encoder;
//
///**
// * @author chibuezeharry & MarcusDashe
// */
//
//@Service
//public class AuthenticationService implements UserDetailsService {
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Override
//    @Transactional
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        User user = userRepository.findByEmailOrPhone(username, username)
//                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
//
//        return user;
//    }
//
////    public ResponseEntity<?> registerUser(RegisterPayload.RegisterRequest registerRequest) {
////
////        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
////            return ResponseEntity
////                    .badRequest()
////                    .body(new RegisterPayload.RegisterErrorResponse("Passwords don't match!"));
////        }
////
////        if (userRepository.existsByPhone(registerRequest.getPhone())) {
////            return ResponseEntity
////                    .badRequest()
////                    .body(new RegisterPayload.RegisterErrorResponse("Phone Number is already in use!"));
////        }
////
////        if (registerRequest.getEmail() != null) {
////            if (userRepository.existsByEmail(registerRequest.getEmail())) {
////                return ResponseEntity
////                        .badRequest()
////                        .body(new RegisterPayload.RegisterErrorResponse("Email Address is already in use!"));
////            }
////        }
////
////        User user = new User();
//////         Create new user's account
////        user.setFirstname(registerRequest.getFirstName());
////        user.setLastname(registerRequest.getLastName());
////
////        user.setEmail(registerRequest.getEmail());
////        user.setPhone(registerRequest.getPhone());
////
////        user.setPassword(encoder.encode(registerRequest.getPassword()));
////        user.setRole(Role.valueOf(registerRequest.getRole())); // set role to predefined register request user role... usually ARTISAN.
////
////        user.getStatus().add(Enums.Status.UNVERIFIED);
////        user.getStatus().add(Enums.Status.ACTIVE);
////
////        User registeredUser = userRepository.save(user);
////        switch (user.) {
////            case TRAINEE:
////                handleArtisanRegistration(user);
////                break;
////            case ASSESSOR:
////                handleAssessorRegistration(user);
////                break;
////            case :
////                handleZonalCoordinatorRegistration(user);
////                break;
////            case MANAGER:
////                handleManagerRegistration(user);
////                break;
////            default:
////                return ResponseEntity
////                        .badRequest()
////                        .body(new RegisterPayload.RegisterErrorResponse("Invalid Role in registration request"));
////        }
//////
////    Authentication authentication = authenticationManager.authenticate(
////                new UsernamePasswordAuthenticationToken(registerRequest.getPhone(), registerRequest.getPassword()));
////
////        SecurityContextHolder.getContext().setAuthentication(authentication);
////
////        String jwt = jwtUtils.generateJwtToken(authentication);
////
////        return ResponseEntity.ok(new RegisterResponse(jwt, new UserResponse(registeredUser)));
////
////    }
//}
