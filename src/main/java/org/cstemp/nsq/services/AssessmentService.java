/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.services;

import lombok.extern.slf4j.Slf4j;
import org.cstemp.nsq.models.relational.Assessment;
import org.cstemp.nsq.models.relational.User;
import org.cstemp.nsq.payload.PagedResponse;
import org.cstemp.nsq.repos.AssessmentRepository;
import org.cstemp.nsq.util.NinasUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *
 * @author chibuezeharry & MarcusDashe
 */
@Service
@Slf4j
public class AssessmentService {

    @Autowired
    private AssessmentRepository assessmentRepository;

    public ResponseEntity<?> getAssessments() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public ResponseEntity<?> getAssessmentUnitsByAssessor(User user, int page, int size, String s, String column, String direction) {

        NinasUtil.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), column);

        Page<Assessment> assessmentsPage = assessmentRepository.findByAssessorAndTitleContaining(user, s, pageable);

        PagedResponse pagedResponse = new PagedResponse(true, "Centres Retrieved Successfully",
                assessmentsPage.getContent(), page, size, assessmentsPage.getTotalElements(), assessmentsPage.getTotalPages(), assessmentsPage.isLast());

        return ResponseEntity.ok(pagedResponse);
    }

    public ResponseEntity<?> getAssessmentsByAssessorWithId(Integer assessorId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public ResponseEntity<?> deleteAssessment(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
