/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.services;


import lombok.extern.slf4j.Slf4j;
import org.cstemp.nsq.exception.BaseException;
import org.cstemp.nsq.models.relational.Centre;
import org.cstemp.nsq.models.relational.Programme;
import org.cstemp.nsq.models.relational.User;
import org.cstemp.nsq.payload.BaseResponse;
import org.cstemp.nsq.payload.CentrePayload;
import org.cstemp.nsq.payload.PagedResponse;
import org.cstemp.nsq.repos.*;
import org.cstemp.nsq.util.NinasUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author chibuezeharry & MarcusDashe
 */
@Service
@Slf4j
public class CentreService {

    @Autowired
    private CentreRepository centreRepository;

    @Autowired
    private ProgrammeRepository programmeRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private AssessorRepository assessorRepository;

    @Autowired
    private InternalVerifierRepository internalVerifierRepository;

    @Autowired
    private ExternalVerifierRepository externalVerifierRepository;

    @Autowired
    private TemplateService templateService;

//    public ResponseEntity<?> getCentreByAdmin(UserDetails userDetails) {
//
//        Centre centre = centreRepository.findByAdmin(userDetails.getUser()).orElse(null);
//
//        if (centre == null) {
//            centre = new Centre();
//
//            String name = userDetails.getUser().getFirstName() + " " + userDetails.getUser().getLastName() + "'s New Centre";
//
//            centre.setName(name);
//
//            centre.setAdmin(userDetails.getUser());
//
//            centre = centreRepository.save(centre);
//        }
//
//        centre.setTraineeCount(portfolioRepository.countByCentre(centre));
//
//        centre.setAssessorCount(assessorRepository.countByCentresContaining(centre));
//
//        centre.setInternalVerifierCount(internalVerifierRepository.countByCentresContaining(centre));
//
//        return ResponseEntity.ok(new BaseResponse(true, "Centre retrieved successfully", centre));
//    }

    public ResponseEntity<?> getCentreByAdmin(User user) {

        Centre centre = centreRepository.findByAdmin(user).orElse(null);

        if (centre == null) {
            centre = new Centre();

            String name = user.getFirstname() + " " + user.getLastname() + "'s New Centre";

            centre.setName(name);

            centre.setAdmin(user);

            centre = centreRepository.save(centre);
        }

        centre.setTraineeCount(portfolioRepository.countByCentre(centre));

        centre.setAssessorCount(assessorRepository.countByCentresContaining(centre));

        centre.setInternalVerifierCount(internalVerifierRepository.countByCentresContaining(centre));

        return ResponseEntity.ok(new BaseResponse(true, "Centre retrieved successfully", centre));
    }

    public ResponseEntity<?> getCentres(int page, int size, String s, String column, String direction) {

        NinasUtil.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), column);

        Page<Centre> centresPage = centreRepository.findByNameContainingOrAddressContaining(s, s, pageable);

        PagedResponse pagedResponse = new PagedResponse(true, "Centres Retrieved Successfully",
                centresPage.getContent(), page, size, centresPage.getTotalElements(), centresPage.getTotalPages(), centresPage.isLast());

        return ResponseEntity.ok(pagedResponse);
    }

    public ResponseEntity<?> getCentresByProgramme(Long programmeId) {
        Programme programme = programmeRepository.findById(programmeId).orElseThrow(
                () -> new BaseException("Programme with id (" + programmeId + ") not found", HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(new BaseResponse(true, "Centres retireved successfully", programme.getCentres()));
    }

    public ResponseEntity<?> updateCentre(CentrePayload.CentreRequest centreRequest, User user) {

        Centre centre = centreRepository.findByAdmin(user).orElseThrow(() -> new UnsupportedOperationException("No centre attahced to admin"));

        BeanUtils.copyProperties(centreRequest, centre);

        Set<Programme> programmes = new HashSet<>();

        centreRequest.getProgrammes().forEach((programmeId) -> {

            Programme programme = programmeRepository.getById(programmeId);

            programmes.add(programme);
        });

        centre.setProgrammes(programmes);

        Centre savedCentre = centreRepository.save(centre);

        return ResponseEntity.ok(new BaseResponse(true, "Centre updated successfully", savedCentre));
    }

}
