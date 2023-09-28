/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.services;


import lombok.extern.slf4j.Slf4j;
import org.cstemp.nsq.events.PortfolioRequestEvent;
import org.cstemp.nsq.exception.BadRequestException;
import org.cstemp.nsq.exception.BaseException;
import org.cstemp.nsq.models.relational.*;
import org.cstemp.nsq.payload.BaseResponse;
import org.cstemp.nsq.payload.PagedResponse;
import org.cstemp.nsq.payload.PortfolioPayload;
import org.cstemp.nsq.repos.*;
import org.cstemp.nsq.util.NinasUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author chibuezeharry & MarcusDashe
 *
 */

@Service
@Slf4j
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AssessorRepository assessorRepository;

    @Autowired
    private InternalVerifierRepository internalVerifierRepository;

    @Autowired
    private ExternalVerifierRepository externalVerifierRepository;

    @Autowired
    private ProgrammeRepository programmeRepository;

    @Autowired
    private CentreRepository centreRepository;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    public ResponseEntity<?> getPortfolioByUser(User user) {

        List<Portfolio> portfolios = portfolioRepository.findAllByUser(user);

        return ResponseEntity.ok(new BaseResponse(true, "Portfolios retrieved successfully", portfolios));
    }

    public ResponseEntity<?> createPortfolios(User user, PortfolioPayload.PortfolioRequest portfolioRequest) {

        Programme programme = programmeRepository.getById(portfolioRequest.getProgramme());

        List<Portfolio> portfolioList = new ArrayList<>();

        portfolioRequest.getCourses().forEach((courseId) -> {
            Centre centre = centreRepository.getById(portfolioRequest.getCentre());

            Portfolio portfolio = new Portfolio();

            portfolio.setCourse(new Course(courseId));
            portfolio.setProgramme(programme);
            portfolio.setCentre(centre);
            portfolio.setUser(user);

            portfolioList.add(portfolio);
        });

        List<Portfolio> savedPortfolios = portfolioRepository.saveAll(portfolioList);

        eventPublisher.publishEvent(new PortfolioRequestEvent.Event(this, savedPortfolios));

        String s = portfolioRequest.getCourses().size() > 1 ? "s" : "";
        return ResponseEntity.ok(new BaseResponse(true, "Assessment Track" + s + " created successfully", null));
    }

    public ResponseEntity<?> updatePortfolios(User user) {

        List<Portfolio> portfolios = portfolioRepository.findAllByUser(user);

        portfolios.forEach((portfolio) -> {
            portfolio.setPhone(user.getPhone());
            portfolio.setPhotoUrl(user.getPhotoUrl());
            portfolio.setName(user.getFirstname() + " " + user.getLastname());
        });

        portfolioRepository.saveAll(portfolios);

        return ResponseEntity.ok(new BaseResponse(true, "Portdfolios updated successfully", null));

    }

    public ResponseEntity<?> disablePortfolio(Long portfolioId) {

        portfolioRepository.deleteById(portfolioId.longValue());

        return ResponseEntity.ok(new BaseResponse(true, "Portfolios deleted successfully", null));

    }

    public ResponseEntity<?> getPortfoliosByAssessor(int page, int size, String searchString, String column, String direction, Long id) {

        NinasUtil.validatePageNumberAndSize(page, size);

        Assessor assessor = new Assessor();
        assessor.setId(id);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), column);

        Page<Portfolio> portfolios = portfolioRepository.findAllByAssessor(assessor, pageable);

        PagedResponse pagedResponse = new PagedResponse(true, "Log Sheets Retrieved Successfully",
                portfolios.getContent(), page, size, portfolios.getTotalElements(), portfolios.getTotalPages(), portfolios.isLast());

        return ResponseEntity.ok(pagedResponse);

    }

    public ResponseEntity<?> getPortfolios(int page, int size, String searchString, String column, String direction, Long programmeId, User user) {

        NinasUtil.validatePageNumberAndSize(page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), column);

        if (user.getAuthorities().contains(new SimpleGrantedAuthority("TRAINEE"))) {
            throw new BaseException("Trainees cannot access this resource.", HttpStatus.FORBIDDEN);
        }

        Programme programme = programmeRepository.findById(programmeId)
                .orElseThrow(() -> new BaseException("Programme Not Found", HttpStatus.NOT_FOUND));

        ExampleMatcher portfolioMatcher = ExampleMatcher.matchingAny()
                .withMatcher("firstName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("lastName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("phone", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        //  Defining Example
        Portfolio portfolio = new Portfolio();
        portfolio.setName(searchString);
        portfolio.setPhone(searchString);

        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ASSESSOR"))) {
            Assessor assessor = assessorRepository.findByUserAndProgramme(user, programme)
                    .orElseThrow(() -> new BaseException("No Assessor Found For User With Assessor Role", HttpStatus.EXPECTATION_FAILED));
            portfolio.setAssessor(assessor);
        }

        Example<Portfolio> portfolioExample = Example.of(portfolio, portfolioMatcher);

        Page< Portfolio> portfolios = portfolioRepository.findAll(portfolioExample, pageable);

        PagedResponse pagedResponse = new PagedResponse(true, "Portfolios Retrieved Successfully",
                portfolios.getContent(), page, size, portfolios.getTotalElements(), portfolios.getTotalPages(), portfolios.isLast());

        return ResponseEntity.ok(pagedResponse);

    }

    public ResponseEntity<?> assignAssessor(Long portfolioId, String email, Long centreId) {

        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(() -> new BadRequestException("Trainee log book with id: (" + portfolioId + ") not found"));

        User user = userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException("User with email: (" + email + ") not found"));

        Centre centre = new Centre(centreId);

        Assessor assessor = assessorRepository.findByUserAndCentresContaining(user, centre).orElseThrow(() -> new BadRequestException(""));

        portfolio.setAssessor(assessor);

        Portfolio savedPortfolio = portfolioRepository.save(portfolio);

        return ResponseEntity.ok(new BaseResponse(true, "Assessor assigned successfully", savedPortfolio));

    }

    public ResponseEntity<?> assignAssessor(Long portfolioId, Long assessorId) {

        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new BaseException("Portfolio with id : (" + portfolioId + ") does not exists", HttpStatus.NOT_FOUND));

        Assessor assessor = new Assessor();
        assessor.setId(assessorId);
        portfolio.setAssessor(assessor);

        portfolioRepository.save(portfolio);

        return ResponseEntity.ok(new BaseResponse(true, "Assessor assigned successfully", portfolio));

    }
//
//    public ResponseEntity<?> assignInternalVerifier(Long portfolioId, Long internalVerifierId) {
//
//        Portfolio portfolio = portfolioRepository.findById(portfolioId)
//                .orElseThrow(() -> new BaseException("Portfolio with id : (" + portfolioId + ") does not exists", HttpStatus.NOT_FOUND));
//
//        InternalVerifier internalVerifier = new InternalVerifier();
//        internalVerifier.setId(internalVerifierId);
//        portfolio.setInternalVerifier(internalVerifier);
//
//        portfolioRepository.save(portfolio);
//
//        return ResponseEntity.ok(new BaseResponse(true, "Internal Verifier assigned successfully", portfolio));
//
//    }
//
//    public ResponseEntity<?> assignExternalVerifier(Long portfolioId, Long externalVerifierId) {
//
//        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElse(null);
//
//        if (portfolio == null) {
//            throw new BaseException("Portfolio with id : (" + portfolioId + ") does not exists", HttpStatus.NOT_FOUND);
//        }
//
//        ExternalVerifier externalVerifier = new ExternalVerifier();
//        externalVerifier.setId(externalVerifierId);
//        portfolio.setExternalVerifier(externalVerifier);
//
//        portfolioRepository.save(portfolio);
//
//        return ResponseEntity.ok(new BaseResponse(true, "External Verifier assigned successfully", portfolio));
//
//    }

//    public ResponseEntity<?> getPortfolioByRole(int page, int size, String s, String column, String direction, String role, String userId) {
//
//        NinasUtil.validatePageNumberAndSize(page, size);
//
//        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), column);
//
//        Portfolio portfolio = new Portfolio();
//        portfolio.setFirstName(s);
//        portfolio.setLastName(s);
//
//        ExampleMatcher em = ExampleMatcher.matching()
//                .withMatcher("firstName", contains())
//                .withMatcher("lastName", contains());
//        try {
//            switch (role) {
//                case "master_artisan":
//                    portfolio.setMasterArtisanId(userId);
//                    em = em.withMatcher("masterArtisanId", exact());
//                    break;
//                case "supervisor":
//                    portfolio.setSupervisorId(userId);
//                    em = em.withMatcher("supervisorId", exact());
//                    break;
//                case "assessor":
//                    Assessor assessor = assessorRepository.findByUserId(userId).orElse(null);
//                    portfolio.setAssessorId(assessor.getId());
//                    em = em.withMatcher("assessorId", exact());
//                    break;
//                case "internal_verifier":
//                    InternalVerifier internalVerifier = internalVerifierRepository.findByUserId(userId).orElse(null);
//                    portfolio.setInternalVerifierId(internalVerifier.getId());
//                    em = em.withMatcher("internalVerifierId", exact());
//                case "centre_admin":
//                    CentreAdministrator centreAdministrator = centreAdministratorRepository.findByUserId(userId).orElse(null);
//                    portfolio.setCentreId(centreAdministrator.getCentreId());
//                    em = em.withMatcher("centreId", exact());
//                case "external_verifier":
//                    ExternalVerifier externalVerifier = externalVerifierRepository.findByUserId(userId).orElse(null);
//                    portfolio.setCentreId(externalVerifier.getId());
//                    em = em.withMatcher("externalVerifierId", exact());
//                case "administrator":
//                    break;
//                default:
//                    throw new BadRequestException("Valid role required in URI path");
//
//            }
//        } catch (Exception ex) {
//            throw new BadRequestException("Valid role required in URI path");
//        }
//
//        Example example = Example.of(portfolio, em);
//
//        Page<Trainee> traineesPage = portfolioRepository.findAll(example, pageable);
//
//        PagedResponse pagedResponse = new PagedResponse(true, "Log Books Retrieved Successfully",
//                traineesPage.getContent(), page, size, traineesPage.getTotalElements(), traineesPage.getTotalPages(), traineesPage.isLast());
//
//        return ResponseEntity.ok(pagedResponse);
//    }
    public ResponseEntity<?> getPortfoliosByCentre(UserDetails userDetails, Long centreId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public ResponseEntity<?> getPortfoliosByAssessorProgramme(User user, Long programmeId, int page, int size, String searchString, String column, String direction) {

        NinasUtil.validatePageNumberAndSize(page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), column);

        Assessor assessor = assessorRepository.findByUserAndProgramme(user, new Programme(programmeId)).orElseThrow();

        Page<Portfolio> portfolioPage = portfolioRepository.findBySearchStringContainsAndProgrammeAndCentreIn(searchString, assessor.getProgramme(), assessor.getCentres(), pageable);

        PagedResponse pagedResponse = new PagedResponse(true, "Portfolios retrieved successfully",
                portfolioPage.getContent(), page, size, portfolioPage.getTotalElements(), portfolioPage.getTotalPages(), portfolioPage.isLast());

        return ResponseEntity.ok(pagedResponse);
    }

    public ResponseEntity<?> getPortfoliosByAssessorAndProgramme(Long assessorId, int page, int size, String searchString, String column, String direction) {

        NinasUtil.validatePageNumberAndSize(page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), column);

        Assessor assessor = assessorRepository.findById(assessorId).orElseThrow();

        Page<Portfolio> portfolioPage = portfolioRepository.findBySearchStringContainsAndProgrammeAndCentreIn(searchString.toLowerCase(), assessor.getProgramme(), assessor.getCentres(), pageable);

        PagedResponse pagedResponse = new PagedResponse(true, "Portfolios retrieved successfully",
                portfolioPage.getContent(), page, size, portfolioPage.getTotalElements(), portfolioPage.getTotalPages(), portfolioPage.isLast());

        return ResponseEntity.ok(pagedResponse);
    }

    public ResponseEntity<?> getPortfoliosByInternalVerifierProgramme(User user, Long programmeId, int page, int size, String searchString, String column, String direction) {

        NinasUtil.validatePageNumberAndSize(page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), column);

        InternalVerifier internalVerifier = internalVerifierRepository.findByUserAndProgramme(user, new Programme(programmeId)).orElseThrow();

        Page<Portfolio> portfolioPage = portfolioRepository.findBySearchStringContainsAndProgrammeAndCentreIn(searchString, internalVerifier.getProgramme(), internalVerifier.getCentres(), pageable);

        PagedResponse pagedResponse = new PagedResponse(true, "Portfolios retrieved successfully",
                portfolioPage.getContent(), page, size, portfolioPage.getTotalElements(), portfolioPage.getTotalPages(), portfolioPage.isLast());

        return ResponseEntity.ok(pagedResponse);
    }

    public ResponseEntity<?> getPortfoliosByInternalVerifierAndProgramme(Long internalVerifierId, int page, int size, String searchString, String column, String direction) {

        NinasUtil.validatePageNumberAndSize(page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), column);

        InternalVerifier internalVerifier = internalVerifierRepository.findById(internalVerifierId).orElseThrow(() -> new BaseException("Internal verifier with id " + internalVerifierId + " not found", HttpStatus.NOT_FOUND));

        Page<Portfolio> portfolioPage = portfolioRepository.findBySearchStringContainsAndProgrammeAndCentreIn(searchString.toLowerCase(), internalVerifier.getProgramme(), internalVerifier.getCentres(), pageable);

        PagedResponse pagedResponse = new PagedResponse(true, "Portfolios retrieved successfully",
                portfolioPage.getContent(), page, size, portfolioPage.getTotalElements(), portfolioPage.getTotalPages(), portfolioPage.isLast());

        return ResponseEntity.ok(pagedResponse);
    }

    public ResponseEntity<?> getPortfoliosByCentreProgramme(User user, Long programmeId, int page, int size, String searchString, String column, String direction) {

        NinasUtil.validatePageNumberAndSize(page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), column);

        Programme programme = programmeRepository.getById(programmeId);

        Centre centre = centreRepository.findByAdmin(user).orElseThrow();

        Page<Portfolio> portfolioPage = portfolioRepository.findBySearchStringContainsAndProgrammeAndCentre(searchString, programme, centre, pageable);

        PagedResponse pagedResponse = new PagedResponse(true, "Portfolios retrieved successfully",
                portfolioPage.getContent(), page, size, portfolioPage.getTotalElements(), portfolioPage.getTotalPages(), portfolioPage.isLast());

        return ResponseEntity.ok(pagedResponse);
    }

    public ResponseEntity<?> getPortfolioById(Long id) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Porftfolio with id: (" + id + ") not found"));

        return ResponseEntity.ok(new BaseResponse(true, "Portfolio retrieved successfully", portfolio));

    }
}
