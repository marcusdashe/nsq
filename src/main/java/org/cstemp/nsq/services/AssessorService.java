/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.services;


import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cstemp.nsq.config.JwtService;
import org.cstemp.nsq.events.InviteEvent;
import org.cstemp.nsq.events.RoleRequestEvent;
import org.cstemp.nsq.exception.BadRequestException;
import org.cstemp.nsq.exception.BaseException;
import org.cstemp.nsq.models.relational.*;
import org.cstemp.nsq.payload.AssessorPayload;
import org.cstemp.nsq.payload.BaseResponse;
import org.cstemp.nsq.payload.PagedResponse;
import org.cstemp.nsq.repos.*;
import org.cstemp.nsq.util.NinasUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author chibuezeharry
 */
@Service
@Slf4j
public class AssessorService {


    @Autowired
    private JwtService jwtUtils;

    @Autowired
    private AssessorRepository assessorRepository;

    @Autowired
    private InviteRepository inviteRepository;

    @Autowired
    private RoleRequestRepository roleRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CentreRepository centreRepository;

    @Autowired
    private ProgrammeRepository programmeRepository;

    @Autowired
    private TemplateService templateService;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    public ResponseEntity<?> getAssessorsByCentre(int page, int size, String searchString, String column, String direction, Long centreId) {

        NinasUtil.validatePageNumberAndSize(page, size);

        Centre centre = new Centre();
        centre.setId(centreId);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), column);

        Page<Assessor> assessors = assessorRepository.findAllByCentresContaining(centre, pageable);

        PagedResponse pagedResponse = new PagedResponse(true, "Assessors Retrieved Successfully",
                assessors.getContent(), page, size, assessors.getTotalElements(), assessors.getTotalPages(), assessors.isLast());

        return ResponseEntity.ok(pagedResponse);
    }

    public ResponseEntity<?> addAssessorsToCentre(AssessorPayload.Invites invites) {

        Centre centre = centreRepository.findById(invites.getCentreId())
                .orElseThrow(() -> new BaseException("Centre Not Found", HttpStatus.NOT_FOUND));

        List<Invite> assessorInvites = new ArrayList<>();

        for (String email : invites.getEmails()) {

            Invite assessorInvite = new Invite();

            assessorInvite.setEmail(email);
            assessorInvite.setCentre(centre);

            assessorInvites.add(assessorInvite);

        }

        List<Invite> savedAssessorInvites = inviteRepository.saveAll(assessorInvites);

        eventPublisher.publishEvent(new InviteEvent.Event(this, savedAssessorInvites));

        BaseResponse baseResponse = new BaseResponse(true, "Assessors added successfully", null);

        return ResponseEntity.ok(baseResponse);
    }

    public ResponseEntity getInviteTemplate() {

        ByteArrayOutputStream stream;

        try {
            XSSFWorkbook workbook = templateService.generateSpreadSheetTemplate(AssessorPayload.Invite.class);

            stream = new ByteArrayOutputStream();

            workbook.write(stream);

            return ResponseEntity.ok()
                    .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .header("Content-Disposition", "download; filename=AssessorInviteList.xlsx")
                    .body(stream.toByteArray());
        } catch (IOException ex) {

            throw new BaseException("The template could not be retrieved", HttpStatus.EXPECTATION_FAILED);
        }
    }

    public ResponseEntity<?> uploadInvites(MultipartFile file, Long centreId) throws Exception {

        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());

        List<AssessorPayload.Invite> inviteList = templateService.importSpreadSheet(AssessorPayload.Invite.class, workbook.getSheetAt(0));

        List<Invite> assessorInvites = new ArrayList<>();

        inviteList.forEach((invite) -> {

            if (!invite.getEmail().matches("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?")) {
                throw new BadRequestException("Invalid Email: " + invite.getEmail());
            }

            Invite assessorInvite = new Invite();

            assessorInvite.setEmail(invite.getEmail());
            assessorInvite.setCentre(new Centre(centreId));

            assessorInvites.add(assessorInvite);
        });

        List<Invite> savedAssessorInvites = inviteRepository.saveAll(assessorInvites);

        eventPublisher.publishEvent(new InviteEvent.Event(this, savedAssessorInvites));

        BaseResponse baseResponse = new BaseResponse(true, "Invite Sent Successfully", savedAssessorInvites);

        return ResponseEntity.ok(baseResponse);
    }

    public ResponseEntity<?> getInvites(Long centreId) {

        List<Invite> assessorInvites = inviteRepository.findAllByCentre(new Centre(centreId));

        BaseResponse baseResponse = new BaseResponse(true, "Invites retrieved Successfully", assessorInvites);

        return ResponseEntity.ok(baseResponse);
    }

    public ResponseEntity<?> acceptInvite(String token) {

        String inviteId = jwtUtils.getSubjectFromJwtToken(token);

        return ResponseEntity.ok(inviteId);

    }

    public ResponseEntity<?> sendAssessorRequest(User user, AssessorPayload.AssessorRequest assessorRequest) {

        RoleRequest roleRequest = new RoleRequest();

        roleRequest.setRequestor(user);
        roleRequest.setRole("ASSESSOR");
        roleRequest.setMessage(assessorRequest.getMessage());
        roleRequest.setProgramme(new Programme(assessorRequest.getProgrammeId()));
        roleRequest.setCentre(new Centre(assessorRequest.getCentreId()));

        if (assessorRepository.existsByUserAndProgrammeAndCentresContaining(user, roleRequest.getProgramme(), roleRequest.getCentre())) {
            throw new BaseException("Duplicate request", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            RoleRequest savedAssessorRequest = roleRequestRepository.save(roleRequest);

            eventPublisher.publishEvent(new RoleRequestEvent.Event(this, savedAssessorRequest));

            BaseResponse baseResponse = new BaseResponse(true, "Request sent successfully", savedAssessorRequest);

            return ResponseEntity.ok(baseResponse);
        } catch (Exception ex) {
            throw new BaseException("Duplicate request", HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }

    public ResponseEntity<?> acceptAssessorRequest(Long roleRequestId) {

        RoleRequest roleRequest = roleRequestRepository.findById(roleRequestId)
                .orElseThrow(() -> new BaseException("Role request with id (" + roleRequestId + ")", HttpStatus.NOT_FOUND));

        Assessor assessor = assessorRepository.findByUserAndProgramme(roleRequest.getRequestor(), roleRequest.getProgramme()).orElse(new Assessor());

        assessor.setUser(roleRequest.getRequestor());
        assessor.setProgramme(roleRequest.getProgramme());
        assessor.getCentres().add(roleRequest.getCentre());

        assessorRepository.save(assessor);

        roleRequest.setAcceptDate(Date.from(Instant.now()));

        RoleRequest savedRoleRequest = roleRequestRepository.save(roleRequest);

        return ResponseEntity.ok(new BaseResponse(true, "Request accepted successfully", savedRoleRequest));
    }

    public ResponseEntity<?> getAssessorRequests(User user, int page, int size, String searchString, String column, String direction) {

        NinasUtil.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), column);

        Page<RoleRequest> assessorRequests = roleRequestRepository.findAllByRequestor(user, pageable);

        PagedResponse pagedResponse = new PagedResponse(true, "Requests Retrieved Successfully",
                assessorRequests.getContent(), page, size, assessorRequests.getTotalElements(), assessorRequests.getTotalPages(), assessorRequests.isLast());

        return ResponseEntity.ok(pagedResponse);
    }

    public ResponseEntity<?> deleteAssessorRequest(Long id) {

        RoleRequest roleRequest = roleRequestRepository.findById(id).orElseThrow(() -> new BaseException("Assessor request not found", HttpStatus.NOT_FOUND));

        roleRequestRepository.delete(roleRequest);

        return ResponseEntity.ok(new BaseResponse(true, "Request deleted successfully", null));
    }
}
