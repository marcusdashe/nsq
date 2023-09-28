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
import org.cstemp.nsq.payload.InternalVerifierPayload;
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
 * @author chibuezeharry $ MarcusDashe
 */
@Service
@Slf4j
public class InternalVerifierService {

    @Autowired
    private JwtService jwtUtils;

    @Autowired
    private InternalVerifierRepository internalVerifierRepository;

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

    public ResponseEntity<?> getInternalVerifiersByCentre(int page, int size, String searchString, String column, String direction, Long id) {

        NinasUtil.validatePageNumberAndSize(page, size);

        Centre centre = new Centre();
        centre.setId(id);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), column);

        Page<InternalVerifier> internalVerifiers = internalVerifierRepository.findAllByCentresContaining(centre, pageable);

        PagedResponse pagedResponse = new PagedResponse(true, "InternalVerifiers Retrieved Successfully",
                internalVerifiers.getContent(), page, size, internalVerifiers.getTotalElements(), internalVerifiers.getTotalPages(), internalVerifiers.isLast());

        return ResponseEntity.ok(pagedResponse);

    }

    public ResponseEntity<?> addInternalVerifiersToCentre(AssessorPayload.Invites invites) {

        Centre centre = centreRepository.findById(invites.getCentreId())
                .orElseThrow(() -> new BaseException("Centre Not Found", HttpStatus.NOT_FOUND));

        List<Invite> internalVerifierInvites = new ArrayList<>();

        for (String email : invites.getEmails()) {

            Invite internalVerifierInvite = new Invite();

            internalVerifierInvite.setEmail(email);
            internalVerifierInvite.setCentre(centre);

            internalVerifierInvites.add(internalVerifierInvite);

        }

        List<Invite> savedInternalVerifierInvites = inviteRepository.saveAll(internalVerifierInvites);

        eventPublisher.publishEvent(new InviteEvent.Event(this, savedInternalVerifierInvites));

        BaseResponse baseResponse = new BaseResponse(true, "InternalVerifiers added successfully", null);

        return ResponseEntity.ok(baseResponse);
    }

    public ResponseEntity getInviteTemplate() {

        ByteArrayOutputStream stream;

        try {
            XSSFWorkbook workbook = templateService.generateSpreadSheetTemplate(Invite.class);

            stream = new ByteArrayOutputStream();

            workbook.write(stream);

            return ResponseEntity.ok()
                    .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .header("Content-Disposition", "download; filename=InternalVerifierInviteList.xlsx")
                    .body(stream.toByteArray());
        } catch (IOException ex) {

            throw new BaseException("The template could not be retrieved", HttpStatus.EXPECTATION_FAILED);
        }
    }

    public ResponseEntity<?> uploadInvites(MultipartFile file, Long centreId) throws Exception {

        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());

        List<Invite> inviteList = templateService.importSpreadSheet(Invite.class, workbook.getSheetAt(0));

        List<Invite> internalVerifierInvites = new ArrayList<>();

        inviteList.forEach((invite) -> {

            if (!invite.getEmail().matches("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?")) {
                throw new BadRequestException("Invalid Email: " + invite.getEmail());
            }

            Invite internalVerifierInvite = new Invite();

            internalVerifierInvite.setEmail(invite.getEmail());
            internalVerifierInvite.setCentre(new Centre(centreId));

            internalVerifierInvites.add(internalVerifierInvite);
        });

        List<Invite> savedInternalVerifierInvites = inviteRepository.saveAll(internalVerifierInvites);

        eventPublisher.publishEvent(new InviteEvent.Event(this, savedInternalVerifierInvites));

        BaseResponse baseResponse = new BaseResponse(true, "Invite Sent Successfully", savedInternalVerifierInvites);

        return ResponseEntity.ok(baseResponse);
    }

    public ResponseEntity<?> getInvites(Long centreId) {

        List<Invite> internalVerifierInvites = inviteRepository.findAllByCentre(new Centre(centreId));

        BaseResponse baseResponse = new BaseResponse(true, "Invites retrieved Successfully", internalVerifierInvites);

        return ResponseEntity.ok(baseResponse);
    }

    public ResponseEntity<?> acceptInvite(String token) {

        String inviteId = jwtUtils.getSubjectFromJwtToken(token);

        return ResponseEntity.ok(inviteId);

    }

    public ResponseEntity<?> sendInternalVerifierRequest(User user, InternalVerifierPayload.InternalVerifierRequest internalVerifierRequest) {

        RoleRequest roleRequest = new RoleRequest();

        roleRequest.setRequestor(user);
        roleRequest.setRole("INTERNAL_VERIFIER");
        roleRequest.setMessage(internalVerifierRequest.getMessage());
        roleRequest.setProgramme(new Programme(internalVerifierRequest.getProgrammeId()));
        roleRequest.setCentre(new Centre(internalVerifierRequest.getCentreId()));

        if (internalVerifierRepository.existsByUserAndProgrammeAndCentresContaining(user, roleRequest.getProgramme(), roleRequest.getCentre())) {
            throw new BaseException("Duplicate request", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            RoleRequest savedInternalVerifierRequest = roleRequestRepository.save(roleRequest);

            eventPublisher.publishEvent(new RoleRequestEvent.Event(this, savedInternalVerifierRequest));

            BaseResponse baseResponse = new BaseResponse(true, "Request sent successfully", savedInternalVerifierRequest);

            return ResponseEntity.ok(baseResponse);
        } catch (Exception ex) {
            throw new BaseException("Duplicate request", HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }

    public ResponseEntity<?> acceptInternalVerifierRequest(Long roleRequestId) {

        RoleRequest roleRequest = roleRequestRepository.findById(roleRequestId)
                .orElseThrow(() -> new BaseException("Role request with id (" + roleRequestId + ")", HttpStatus.NOT_FOUND));

        InternalVerifier internalVerifier = internalVerifierRepository.findByUserAndProgramme(roleRequest.getRequestor(), roleRequest.getProgramme()).orElse(new InternalVerifier());

        internalVerifier.setUser(roleRequest.getRequestor());
        internalVerifier.setProgramme(roleRequest.getProgramme());
        internalVerifier.getCentres().add(roleRequest.getCentre());

        internalVerifierRepository.save(internalVerifier);

        roleRequest.setAcceptDate(Date.from(Instant.now()));

        RoleRequest savedRoleRequest = roleRequestRepository.save(roleRequest);

        return ResponseEntity.ok(new BaseResponse(true, "Request accepted successfully", savedRoleRequest));
    }

    public ResponseEntity<?> getInternalVerifierRequests(User user, int page, int size, String searchString, String column, String direction) {

        NinasUtil.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), column);

        Page<RoleRequest> internalVerifierRequests = roleRequestRepository.findAllByRequestor(user, pageable);

        PagedResponse pagedResponse = new PagedResponse(true, "Requests Retrieved Successfully",
                internalVerifierRequests.getContent(), page, size, internalVerifierRequests.getTotalElements(), internalVerifierRequests.getTotalPages(), internalVerifierRequests.isLast());

        return ResponseEntity.ok(pagedResponse);
    }

    public ResponseEntity<?> deleteInternalVerifierRequest(Long id) {

        RoleRequest roleRequest = roleRequestRepository.findById(id).orElseThrow(() -> new BaseException("InternalVerifier request not found", HttpStatus.NOT_FOUND));

        roleRequestRepository.delete(roleRequest);

        return ResponseEntity.ok(new BaseResponse(true, "Request deleted successfully", null));
    }

}
