///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package org.cstemp.nsq.controls;
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.cstemp.nsq.models.relational.User;
//import org.cstemp.nsq.security.CurrentUser;
//import org.cstemp.nsq.services.AssessmentService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.*;
//
//import static org.cstemp.nsq.util.NinasUtil.DEFAULT_PAGE_NUMBER;
//import static org.cstemp.nsq.util.NinasUtil.DEFAULT_PAGE_SIZE;
//
//
///**
// *
// * @author chibuezeharry & MarcusDashe
// */
//@CrossOrigin(origins = "*")
//@RestController
//@RequestMapping("/api/v1/assessment")
//@Slf4j
//public class AssessmentControl {
//
//    @Autowired
//    private AssessmentService assessmentService;
//
//
//    @GetMapping("/")
//    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
//    public ResponseEntity<?> getAssessments(@PathVariable Long id) {
//
//        return assessmentService.getAssessments();
//    }
//
//    @GetMapping("/assessor")
//    @PreAuthorize("hasAuthority('ASSESSOR')") //@CurrentUser UserDetails userDetails,
//    public ResponseEntity<?> getAssessmentUnitsByAssessor(@RequestParam(defaultValue = "") String s,
//                                                          @RequestParam(value = "d", defaultValue = "DESC") String direction,
//                                                          @RequestParam(value = "c", defaultValue = "createdAt") String column,
//                                                          @RequestParam(value = "page", defaultValue = DEFAULT_PAGE_NUMBER) int page,
//                                                          @RequestParam(value = "size", defaultValue = DEFAULT_PAGE_SIZE) int size) {
//
////        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
////        String username = userDetails.getUsername();
////        User user  = userRepository.findUserName(username);
//
//        return assessmentService.getAssessmentUnitsByAssessor(userDetails.getUser(), page, size, s, column, direction);
//    }
//
//    @GetMapping("/assessor/{id}")
//    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
//    public ResponseEntity<?> getAssessmentsByAssessorWithId(@PathVariable Integer id) {
//
//        return assessmentService.getAssessmentsByAssessorWithId(id);
//    }
//
//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('CENTRE_ADMIN')")
//    public ResponseEntity<?> removeInternalVerifier(@PathVariable Integer id) {
//
//        return assessmentService.deleteAssessment(id);
//    }
//
//}
