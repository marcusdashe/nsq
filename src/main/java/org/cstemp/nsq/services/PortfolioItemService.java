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
import org.cstemp.nsq.payload.PagedResponse;
import org.cstemp.nsq.payload.PortfolioItemPayload;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author chibuezeharry & MarcusDashe
 */

@Service
@Slf4j
public class PortfolioItemService {

    private static final String ITEMS_FOLDER_NAME = "portfolio-items";

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private RemarkRepository remarkRepository;

    @Autowired
    private EvidenceRepository evidenceRepository;

    @Autowired
    private AssessorRepository assessorRepository;

    @Autowired
    private PortfolioItemRepository portfolioItemRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;
//    @Autowired
//    private InternalVerifierRepository internalVerifierRepository;
//
//    @Autowired
//    private ExternalVerifierRepository externalVerifierRepository;

    public ResponseEntity<?> getPortfolioItemsByPortfolioId(int page, int size, String s, String column, String direction, Long portfolioId) {

        NinasUtil.validatePageNumberAndSize(page, size);

        Portfolio portfolio = new Portfolio();
        portfolio.setId(portfolioId);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), column);

        Page<PortfolioItem> portfolioItemPage = portfolioItemRepository.findByPortfolioAndTitleContains(portfolio, s, pageable);

        PagedResponse pagedResponse = new PagedResponse(true, "Portfolio items Retrieved Successfully",
                portfolioItemPage.getContent(), page, size, portfolioItemPage.getTotalElements(), portfolioItemPage.getTotalPages(), portfolioItemPage.isLast());

        return ResponseEntity.ok(pagedResponse);
    }

    public ResponseEntity<?> addPortfolioItem(Long portfolioId, MultipartFile media, PortfolioItemPayload.PortfolioItemRequest portfolioItemRequest) {

        PortfolioItem sheet = new PortfolioItem();

        BeanUtils.copyProperties(portfolioItemRequest, sheet);

        Portfolio portfolio = new Portfolio();
        portfolio.setId(portfolioId);

        sheet.setPortfolio(portfolio);

        sheet.setLogDate(LocalDate.ofInstant(portfolioItemRequest.getLogDate().toInstant(), ZoneId.systemDefault()));

        if (media != null) {

            String response = NinasUtil.storeToCloudinary(media, ITEMS_FOLDER_NAME);

            sheet.setMediaUrl(response);

        }

        PortfolioItem savedSheet = portfolioItemRepository.save(sheet);

        return ResponseEntity.ok(new BaseResponse(true, "Portfolio item saved successfully", savedSheet));
    }

    public ResponseEntity<?> deletePortfolioItem(Long portfolioItemId) {

        PortfolioItem portfolioItem = portfolioItemRepository.findById(portfolioItemId).orElse(null);

        if (null == portfolioItem) {
            throw new BaseException("Portfolio item not found", HttpStatus.NOT_FOUND);
        }

        portfolioItem.getEvidences().forEach((evidence) -> {
            try {
                NinasUtil.deleteFromCloudinary(evidence.getUrl());
                evidenceRepository.delete(evidence);
            } catch (Exception ex) {

            }
        });

        NinasUtil.deleteFromCloudinary(portfolioItem.getMediaUrl());

        portfolioItemRepository.deleteById(portfolioItemId);

        return ResponseEntity.ok(new BaseResponse(true, "Portfolio item deleted successfully", null));

    }

    public ResponseEntity<?> addEvidence(Long portfolioItemId, MultipartFile media) {

        PortfolioItem portfolioItem = portfolioItemRepository.findById(portfolioItemId).orElse(null);

        String response = NinasUtil.storeToCloudinary(media, ITEMS_FOLDER_NAME);

        Evidence evidence = new Evidence();

        evidence.setUrl(response);
        evidence.setEvidenceType("PHOTO");
        evidence.setPortfolioItem(portfolioItem);

        Evidence savedEvidence = evidenceRepository.save(evidence);

        portfolioItem.getEvidences().add(savedEvidence);

        return ResponseEntity.ok(new BaseResponse(true, "Portfolio item evidence added successfully", portfolioItem));
    }

    public ResponseEntity<?> deleteEvidence(Long portfolioItemId, Integer evidenceIndex) {

        PortfolioItem portfolioItem = portfolioItemRepository.findById(portfolioItemId).orElse(null);

        Evidence evidence = portfolioItem.getEvidences().get(evidenceIndex);

        NinasUtil.deleteFromCloudinary(evidence.getUrl());

        evidenceRepository.delete(evidence);

        return ResponseEntity.ok(new BaseResponse(true, "Portfolio item Evidence deleted successfully", null));
    }

    public ResponseEntity<?> addVideoEvidence(Long portfolioItemId, MultipartFile media) {

        PortfolioItem portfolioItem = portfolioItemRepository.findById(portfolioItemId).orElse(null);

        String response = NinasUtil.storeVideoToCloudinary(media, ITEMS_FOLDER_NAME);

        Evidence evidence = new Evidence();

        evidence.setUrl(response);
        evidence.setEvidenceType("VIDEO");
        evidence.setPortfolioItem(portfolioItem);

        Evidence savedEvidence = evidenceRepository.save(evidence);

        portfolioItem.getEvidences().add(savedEvidence);

        return ResponseEntity.ok(new BaseResponse(true, "Portfolio item evidence added successfully", portfolioItem));
    }

    public ResponseEntity<?> deleteVideoEvidence(Long portfolioItemId, Integer evidenceIndex) {

        PortfolioItem portfolioItem = portfolioItemRepository.findById(portfolioItemId).orElse(null);

        Evidence evidence = portfolioItem.getEvidences().remove(evidenceIndex.intValue());

        NinasUtil.deleteFromCloudinary(evidence.getUrl());

        portfolioItemRepository.save(portfolioItem);

        evidenceRepository.delete(evidence);

        return ResponseEntity.ok(new BaseResponse(true, "Portfolio item Evidence deleted successfully", null));
    }

    public ResponseEntity<?> saveTracks(Long portfolioItemId, List<String> tracks) {
        PortfolioItem portfolioItem = portfolioItemRepository.findById(portfolioItemId)
                .orElseThrow(() -> new BaseException("Portfolio item Not Found", HttpStatus.NOT_FOUND));

        portfolioItem.setTracks(tracks);

        portfolioItemRepository.save(portfolioItem);

        return ResponseEntity.ok(new BaseResponse(true, "Portfolio item tracked successfully", portfolioItem));
    }

    public ResponseEntity<?> supervisePortfolioItem(Long portfolioItemId, PortfolioItemPayload.AssessmentRequest assessmentRequest, UserDetails userDetails) {

        PortfolioItem portfolioItem = portfolioItemRepository.findById(portfolioItemId).orElse(null);

        if (portfolioItem == null) {
            throw new BaseException("Portfolio item Not Found", HttpStatus.NOT_FOUND);
        }

//        portfolioItem.setSupervised(true);
//        portfolioItem.setSupervisorRemark(assessmentRequest.getRemark());
        portfolioItemRepository.save(portfolioItem);

        return ResponseEntity.ok(new BaseResponse(true, "Portfolio item supervised successfully", portfolioItem));
    }

    public ResponseEntity<?> assessPortfolioItem(Long portfolioItemId, PortfolioItemPayload.AssessmentRequest assessmentRequest, UserDetails userDetails) {

        PortfolioItem portfolioItem = portfolioItemRepository.findById(portfolioItemId).orElse(null);

        if (portfolioItem == null) {
            throw new BaseException("Portfolio item Not Found", HttpStatus.NOT_FOUND);
        }

//        portfolioItem.setAssessed(true);
//        portfolioItem.setValid(assessmentRequest.getValid());
//        portfolioItem.setSufficient(assessmentRequest.getSufficient());
//        portfolioItem.setAssessmentRemark(assessmentRequest.getRemark());
        portfolioItemRepository.save(portfolioItem);

        return ResponseEntity.ok(new BaseResponse(true, "Portfolio item assessed successfully", portfolioItem));
    }

    public ResponseEntity<?> makeRemark(Long portfolioItemId, Remark remark, User user) {

        PortfolioItem portfolioItem = portfolioItemRepository.findById(portfolioItemId).orElseThrow(() -> new BaseException("Portfolio item Not Found", HttpStatus.NOT_FOUND));

//        User user = userDetails.getUser();

        remark.setUser(user);
        remark.setPortfolioItem(portfolioItem);
        remark.setNsqRole(user.getRolesList().get(0));

        Remark oldRemark = remarkRepository.findByPortfolioItemAndUser(portfolioItem, user).orElse(null);

        if (oldRemark != null) {

            remark.setId(oldRemark.getId());
        }

        Remark newRemark = remarkRepository.save(remark);

        return ResponseEntity.ok(new BaseResponse(true, "Portfolio item remarked successfully", newRemark));
    }

    public ResponseEntity<?> assessItem(Long portfolioItemId, Assessment assessment, User user) {

        PortfolioItem portfolioItem = portfolioItemRepository.findById(portfolioItemId).orElseThrow(() -> new BaseException("Portfolio item Not Found", HttpStatus.NOT_FOUND));

        Assessor assessor = portfolioItem.getPortfolio().getAssessor();

        if (assessor == null) {
            assessor = assessorRepository.findByUserAndProgramme(user, portfolioItem.getPortfolio().getProgramme()).orElseThrow();

            Portfolio portfolio = portfolioItem.getPortfolio();
            portfolio.setAssessor(assessor);

            portfolioRepository.save(portfolio);
        } else if (!assessor.getUser().equals(user)) {

            throw new BaseException("Invalid assessor");
        }

        if (portfolioItem.getAssessment() != null) {

            portfolioItem.getAssessment().setValid(assessment.getValid());
            portfolioItem.getAssessment().setRemark(assessment.getRemark());
            portfolioItem.getAssessment().setSufficient(assessment.getSufficient());
        } else {
            assessment.setAssessor(user);
            assessment.setPortfolioItem(portfolioItem);
            assessment.setPortfolio(portfolioItem.getPortfolio());
            assessment.setTrainee(portfolioItem.getPortfolio().getUser());
            portfolioItem.setAssessment(assessment);
        }

        PortfolioItem savedPortfolioItem = portfolioItemRepository.save(portfolioItem);

        return ResponseEntity.ok(new BaseResponse(true, "Portfolio item assessed successfully", savedPortfolioItem));
    }

    public ResponseEntity<?> addTracks(Long portfolioItemId, List<String> codes, User user) {

        PortfolioItem portfolioItem = portfolioItemRepository.findById(portfolioItemId).orElseThrow(() -> new BaseException("Portfolio item Not Found", HttpStatus.NOT_FOUND));

//        User user = userDetails.getUser();

        List<Track> tracks = new ArrayList<>();

        codes.forEach((code) -> {

            Track track = new Track();

            track.setCode(code);
            track.setUser(user);
            track.setPortfolioItem(portfolioItem);

        });

        List<Track> savedTracks = trackRepository.saveAll(tracks);

        return ResponseEntity.ok(new BaseResponse(true, "Portfolio item remarked successfully", null));
    }

}
