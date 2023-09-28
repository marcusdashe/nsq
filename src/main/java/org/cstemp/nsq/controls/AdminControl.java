/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.controls;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.cstemp.nsq.exception.AssessorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.cstemp.nsq.services.AdminService;


/**
 *
 * @author chibuezeharry & MarcusDashe
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/admin")
@Slf4j
public class AdminControl {

    @Autowired
    private AdminService adminService;

    @GetMapping("/assessors")
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR','SUPER_ADMIN')")
    public ResponseEntity<?> getAssessors() {

        return null;
    }

//    @PostMapping("/profile")
//    public ResponseEntity<?> saveProfile(@Valid @RequestBody UserRequest userRequest, @CurrentUser UserDetails userDetails) {
//
//        return adminService.saveUser(userRequest, userDetails);
//    }
//
//    @PostMapping("/contact-info")
//    public ResponseEntity<?> saveContactInfo(@Valid @RequestBody UserContactRequest userRequest, @CurrentUser UserDetails userDetails) {
//
//        return adminService.saveUserContactInfo(userRequest, userDetails);
//    }
//
//    @PostMapping("/change")
//    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest userRequest, @CurrentUser UserDetails userDetails) {
//
//        return adminService.changeUserPassword(userRequest, userDetails);
//    }
    @ExceptionHandler(AssessorException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Error message")
    public Object handleError(HttpServletRequest req, Exception ex) {
        Object errorObject = new Object();
        return errorObject;
    }

}
