/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.services;

import lombok.extern.slf4j.Slf4j;
import org.cstemp.nsq.exception.BaseException;
import org.cstemp.nsq.models.relational.*;
import org.cstemp.nsq.payload.BaseResponse;
import org.cstemp.nsq.payload.ProgrammePayload;
import org.cstemp.nsq.repos.AssessorRepository;
import org.cstemp.nsq.repos.CentreRepository;
import org.cstemp.nsq.repos.InternalVerifierRepository;
import org.cstemp.nsq.repos.ProgrammeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author chibuezeharry & MarcusDashe
 */
@Service
@Slf4j
public class ProgrammeService {

    @Autowired
    private AssessorRepository assessorRepository;

    @Autowired
    private InternalVerifierRepository internalVerifierRepository;

    @Autowired
    private CentreRepository centreRepository;

    @Autowired
    private ProgrammeRepository programmeRepository;

    public ResponseEntity<?> saveProgramme(ProgrammePayload.ProgrammeRequest programmeRequest){



        if(programmeRequest == null ){
            return ResponseEntity.badRequest().body(new BaseResponse(false, "Programme cannot be Empty", programmeRequest));
        }
        if(programmeRepository.findByTitle(programmeRequest.getTitle()) != null && programmeRepository.findByFee(programmeRequest.getFee()) != null){
            return ResponseEntity.badRequest().body(new BaseResponse(false, "Programme already exist in database", programmeRequest));
        }

        Programme programme = Programme.builder().title(programmeRequest.getTitle()).
                description(programmeRequest.getDescription()).
                fee(programmeRequest.getFee()).
                discountedFee(programmeRequest.getDiscountedFee()).
                requirements(programmeRequest.getRequirements())
                .startDate(programmeRequest.getStartDate())
                .endDate(programmeRequest.getEndDate())
                .coursesLimit(programmeRequest.getCoursesLimit())
                .visibility(programmeRequest.isVisibility())
                .build();

            Programme response = programmeRepository.save(programme);
            ProgrammePayload.ProgrammeResponse programmeResponse = new ProgrammePayload.ProgrammeResponse(response);

            return ResponseEntity.ok(new BaseResponse(true, "Programme created successfully", programmeResponse));
    }

    public ResponseEntity<?> getProgrammes() {

        List<Programme> programmeList = programmeRepository.findAll();

        return ResponseEntity.ok(new BaseResponse(true, "Programmes retrieved successfully", programmeList));
    }

    public ResponseEntity<?> getProgramme(UserDetails userDetails) {

        return ResponseEntity.ok(new BaseResponse(true, "Centre retrieved successfully", userDetails));
    }

    public ResponseEntity<?> getProgrammeById(Long id) {

        Programme programme = programmeRepository.findById(id).orElseThrow(
                () -> new BaseException("Unable to find programme.", HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(new BaseResponse(true, "Centre retrieved successfully", programme));
    }

    public ResponseEntity<?> getProgrammeByAssessor(User user) {

        List<Assessor> assessorProfiles = assessorRepository.findAllByUser(user);

        List<Programme> programmes = new ArrayList<>();

        assessorProfiles.forEach(assessor -> {
            Programme programme = new Programme();

            BeanUtils.copyProperties(assessor.getProgramme(), programme);

            programmes.add(programme);
        });

        return ResponseEntity.ok(new BaseResponse(true, "Programmes retrieved successfully", programmes));
    }

    public ResponseEntity<?> getProgrammeByInternalVerifier(User user) {

        List<InternalVerifier> internalVerifierProfiles = internalVerifierRepository.findAllByUser(user);

        List<Programme> programmes = new ArrayList<>();

        internalVerifierProfiles.forEach(internalVerifier -> {
            Programme programme = new Programme();

            BeanUtils.copyProperties(internalVerifier.getProgramme(), programme);

            programmes.add(programme);
        });

        return ResponseEntity.ok(new BaseResponse(true, "Programmes retrieved successfully", programmes));
    }

    public ResponseEntity<?> getProgrammeByCentre(User user) {

        Centre centre = centreRepository.findByAdmin(user).get();

        return ResponseEntity.ok(new BaseResponse(true, "Programmes retrieved successfully", centre.getProgrammes()));
    }
}
