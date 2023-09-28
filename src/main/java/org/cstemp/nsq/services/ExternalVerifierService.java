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
import org.cstemp.nsq.exception.BadRequestException;
import org.cstemp.nsq.exception.BaseException;
import org.cstemp.nsq.models.relational.Centre;
import org.cstemp.nsq.models.relational.ExternalVerifier;
import org.cstemp.nsq.models.relational.Invite;
import org.cstemp.nsq.payload.AssessorPayload;
import org.cstemp.nsq.payload.BaseResponse;
import org.cstemp.nsq.payload.PagedResponse;
import org.cstemp.nsq.repos.CentreRepository;
import org.cstemp.nsq.repos.ExternalVerifierRepository;
import org.cstemp.nsq.repos.InviteRepository;
import org.cstemp.nsq.repos.UserRepository;
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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author chibuezeharry
 */
@Service
@Slf4j
public class ExternalVerifierService {

    @Autowired
    private JwtService jwtUtils;

    @Autowired
    private ExternalVerifierRepository externalVerifierRepository;

    @Autowired
    private InviteRepository inviteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CentreRepository centreRepository;

    @Autowired
    private TemplateService templateService;

    @Autowired
    ApplicationEventPublisher publisher;

    public ResponseEntity<?> getExternalVerifiersByCentre(int page, int size, String searchString, String column, String direction, Long id) {

        NinasUtil.validatePageNumberAndSize(page, size);

        Centre centre = new Centre();
        centre.setId(id);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), column);

        Page<ExternalVerifier> externalVerifiers = externalVerifierRepository.findAllByCentresContaining(centre, pageable);

        PagedResponse pagedResponse = new PagedResponse(true, "Assessors Retrieved Successfully",
                externalVerifiers.getContent(), page, size, externalVerifiers.getTotalElements(), externalVerifiers.getTotalPages(), externalVerifiers.isLast());

        return ResponseEntity.ok(pagedResponse);

    }

    public ResponseEntity<?> addExternalVerifiersToCentre(AssessorPayload.Invites invites) {

        Centre centre = centreRepository.findById(invites.getCentreId())
                .orElseThrow(() -> new BaseException("Centre Not Found", HttpStatus.NOT_FOUND));

        List<Invite> assessorInvites = new ArrayList<>();

        for (String email : invites.getEmails()) {

            Invite assessorInvite = new Invite();

            assessorInvite.setEmail(email);
            assessorInvite.setCentre(centre);

            assessorInvites.add(assessorInvite);

        }

        List<Invite> savedExternalVerifierInvites = inviteRepository.saveAll(assessorInvites);

        publisher.publishEvent(new InviteEvent.Event(this, savedExternalVerifierInvites));

        BaseResponse baseResponse = new BaseResponse(true, "ExternalVerifiers added successfully", null);

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
                    .header("Content-Disposition", "download; filename=ExternalVerifierInviteList.xlsx")
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

        List<Invite> savedExternalVerifierInvites = inviteRepository.saveAll(assessorInvites);

        publisher.publishEvent(new InviteEvent.Event(this, savedExternalVerifierInvites));

        BaseResponse baseResponse = new BaseResponse(true, "Invite Sent Successfully", savedExternalVerifierInvites);

        return ResponseEntity.ok(baseResponse);
    }

    public ResponseEntity<?> getInvites(Long centreId) {

        List<Invite> assessorInvites = inviteRepository.findAllByCentre(new Centre(centreId));

        BaseResponse baseResponse = new BaseResponse(true, "Invites retrieved Successfully", assessorInvites);

        return ResponseEntity.ok(baseResponse);
    }

    public ResponseEntity<?> acceptInvite(String token) {

//        publisher.publishEvent(new InviteExternalVerifier.Event(this, savedExternalVerifierInvites));
//
//        BaseResponse baseResponse = new BaseResponse(true, "ExternalVerifiers Invited Successfully", savedExternalVerifierInvites);
//
//        return ResponseEntity.ok(baseResponse);
        String inviteId = jwtUtils.getSubjectFromJwtToken(token);

        return ResponseEntity.ok(inviteId);

    }

}
