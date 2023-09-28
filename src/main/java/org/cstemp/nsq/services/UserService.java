/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.services;


import lombok.extern.slf4j.Slf4j;
import org.cstemp.nsq.exception.BadRequestException;
import org.cstemp.nsq.models.relational.User;
import org.cstemp.nsq.payload.BaseResponse;
import org.cstemp.nsq.payload.PagedResponse;
import org.cstemp.nsq.payload.UserPayload;
import org.cstemp.nsq.repos.ProfileRepository;
import org.cstemp.nsq.repos.UserRepository;
import org.cstemp.nsq.util.NinasUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

/**
 *
 * @author chibuezeharry
 */
@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    PasswordEncoder encoder;

    public ResponseEntity<Object> getUserProfile(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        UserPayload.UserResponse profileResponse = new UserPayload.UserResponse(user);

        return ResponseEntity.ok(new BaseResponse(true, null, profileResponse));
    }

    public ResponseEntity<?> getUserProfile() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

        if (user == null) {
            return ResponseEntity.ok(new BaseResponse(false, "No User Found", null));
        }

        UserPayload.UserResponse userResponse = new UserPayload.UserResponse(user);

        userResponse.setRoles(Set.copyOf(user.getRolesList()));

        return ResponseEntity.ok(new BaseResponse(true, null, userResponse));

    }

    public ResponseEntity<?> saveUser(UserPayload.UserRequest userRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = userRepository.findByEmail(userDetails.getUsername()).orElse(new User());

        BeanUtils.copyProperties(userRequest, user);

        userRepository.save(user);

        UserPayload.UserResponse userResponse = new UserPayload.UserResponse(user);

        return ResponseEntity.ok(new BaseResponse(true, null, userResponse));
    }

    public ResponseEntity<?> uploadPhoto(MultipartFile file, User user) {

        String response = NinasUtil.storeToCloudinary(file, "user-photos/");

//        User user = userRepository.findById(user.getId()).orElse(null);

        user.setPhotoUrl(response);

        User savedUser = userRepository.save(user);

        UserPayload.UserResponse userResponse = new UserPayload.UserResponse();

        BeanUtils.copyProperties(savedUser, userResponse);

        return ResponseEntity.ok(new BaseResponse(true, null, userResponse));

    }

    public ResponseEntity<?> changeUserPassword(UserPayload.PasswordChangeRequest userRequest, User user) {

        if (!userRequest.getNewPassword().equals(userRequest.getConfirmPassword())) {
            throw new BadRequestException("Passwords don't match. Please check and try again!");
        }

        if (userRequest.getNewPassword().length() < 8) {
            throw new BadRequestException("Password needs to be at least eight (8) charachters");
        }

//        User user = userRepository.findById(userDetails.getId()).orElse(null);

        if (!encoder.matches(userRequest.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("Current Password is not correct. If you've lost your password, please use the forgot password function on login.");
        }

        user.setPassword(encoder.encode(userRequest.getNewPassword()));

        userRepository.save(user);

        return ResponseEntity.ok(new BaseResponse(true, "Password Changed Successfully", null));
    }

    public ResponseEntity<?> getUsersPaged(int page, int size, String s, String column, String direction) {

        NinasUtil.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), column);

        Page<User> usersPage = userRepository.findByFirstnameContainingOrLastnameContainingOrEmailContaining(s, s, s, pageable);

        List<UserPayload.UserResponse> individualResponses = usersPage.map((user) -> {
            UserPayload.UserResponse userResponse = new UserPayload.UserResponse();
            BeanUtils.copyProperties(user, userResponse);
            return userResponse;
        }).getContent();

        PagedResponse pagedResponse = new PagedResponse(true, "Users Retrieved Successfully",
                individualResponses, page, size, usersPage.getTotalElements(), usersPage.getTotalPages(), usersPage.isLast());

        return ResponseEntity.ok(pagedResponse);
    }
}
