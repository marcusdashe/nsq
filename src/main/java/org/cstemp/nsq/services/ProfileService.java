/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.services;

import lombok.extern.slf4j.Slf4j;
import org.cstemp.nsq.models.relational.AcademicDegree;
import org.cstemp.nsq.models.relational.Job;
import org.cstemp.nsq.models.relational.Profile;
import org.cstemp.nsq.models.relational.User;
import org.cstemp.nsq.payload.BaseResponse;
import org.cstemp.nsq.payload.ProfilePayload;
import org.cstemp.nsq.repos.*;
import org.cstemp.nsq.util.NinasUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author chibuezeharry & MarcusDashe
 */
@Service
@Slf4j
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private AcademicDegreeRepository academicDegreeRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private EvidenceRepository evidenceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TemplateService templateService;

    public ResponseEntity<?> getProfileByUser(User user) {

        Profile profile = profileRepository.findByUser(user).orElse(null);

        ProfilePayload.ProfileResponse profileResponse = new ProfilePayload.ProfileResponse(profile);

        return ResponseEntity.ok(new BaseResponse(true, null, profileResponse));

    }

    public ResponseEntity<?> getProfileByUserId(Long id) {

        User user = new User();

        user.setId(id);

        Profile profile = profileRepository.findByUser(user).orElse(null);

        ProfilePayload.ProfileResponse profileResponse = new ProfilePayload.ProfileResponse(profile);

        return ResponseEntity.ok(new BaseResponse(true, null, profileResponse));

    }
//
//    public ResponseEntity<?> getProfiles(int page, int size, String s, String column, String direction) {
//
//        NinasUtil.validatePageNumberAndSize(page, size);
//
//        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), column);
//
//        Page<Profile> profilesPage = profileRepository.findByFirstNameContainingOrLastNameContainingOrEmailContainingOrAddressContaining(s, s, s, s, pageable);
//
//        List<ProfileResponse> responses = profilesPage.map((profile) -> {
//            ProfileResponse response = new ProfileResponse();
//
//            BeanUtils.copyProperties(profile, response);
//
//            return response;
//        }).getContent();
//
//        PagedResponse pagedResponse = new PagedResponse(true, "Profiles Retrieved Successfully",
//                responses, page, size, profilesPage.getTotalElements(), profilesPage.getTotalPages(), profilesPage.isLast());
//
//        return ResponseEntity.ok(pagedResponse);
//    }
//

    public ResponseEntity<?> saveProfile(ProfilePayload.ProfileRequest profileRequest, User user) {

        Profile profile = profileRepository.findByUser(user).orElse(null);

        BeanUtils.copyProperties(profileRequest, profile);

        profile.getUser().setPhone(profileRequest.getPhone());

        Profile savedProfile = profileRepository.save(profile);

        ProfilePayload.ProfileResponse profileResponse = new ProfilePayload.ProfileResponse(savedProfile);

        return ResponseEntity.ok(new BaseResponse(true, null, profileResponse));
    }
//
//    public ResponseEntity<?> getUnverifiedProfiles(int page, int size, String s, String column, String direction) {
//
//        NinasUtil.validatePageNumberAndSize(page, size);
//
//        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), column);
//
//        Profile profile = new Profile();
//        profile.setFirstName(s);
//        profile.setLastName(s);
//        profile.setAddress(s);
//        profile.setEmail(s);
//
//        ExampleMatcher em = ExampleMatcher.matching()
//                .withMatcher("firstName", contains())
//                .withMatcher("lastName", contains())
//                .withMatcher("address", contains())
//                .withMatcher("email", contains());
//
//        Example example = Example.of(profile, em);
//
//        Page<Profile> profilesPage = profileRepository.findAll(example, pageable);
//
//        List<ProfileResponse> responses = profilesPage.map((profile1) -> {
//
//            ProfileResponse response = new ProfileResponse();
//
//            BeanUtils.copyProperties(profile1, response);
//
//            return response;
//        }).getContent();
//
//        PagedResponse pagedResponse = new PagedResponse(true, "Profiles Retrieved Successfully",
//                responses, page, size, profilesPage.getTotalElements(), profilesPage.getTotalPages(), profilesPage.isLast());
//
//        return ResponseEntity.ok(pagedResponse);
//    }
//
//    public ResponseEntity<?> getProfileById(String id) {
//
//        Profile profile = profileRepository.findById(id).orElse(null);
//
//        if (profile == null) {
//
//        }
//
//        ProfileResponse profileResponse = new ProfileResponse();
//
//        BeanUtils.copyProperties(profile, profileResponse);
//
//        return ResponseEntity.ok(new BaseResponse(true, null, profileResponse));
//    }
//

    public ResponseEntity<?> addAcademicDegree(AcademicDegree academicDegree, User user) {

        Profile profile = profileRepository.findByUser(user).orElse(null);

        academicDegree.setProfile(profile);

        academicDegreeRepository.save(academicDegree);

        ProfilePayload.ProfileResponse profileResponse = new ProfilePayload.ProfileResponse(profile);

        return ResponseEntity.ok(new BaseResponse(true, null, profileResponse));
    }

    public ResponseEntity<?> updateAcademicDegree(Integer index, AcademicDegree newAcademicDegree, User user) {

        Profile profile = profileRepository.findByUser(user).orElse(null);

        AcademicDegree academicDegree = profile.getEducation().get(index);//academicDegreeRepository.save(academicDegree);

        academicDegree.setDegree(newAcademicDegree.getDegree());
        academicDegree.setCourse(newAcademicDegree.getCourse());
        academicDegree.setSchool(newAcademicDegree.getSchool());
        academicDegree.setLocation(newAcademicDegree.getLocation());
        academicDegree.setStartMonth(newAcademicDegree.getStartMonth());
        academicDegree.setStartYear(newAcademicDegree.getStartYear());
        academicDegree.setEndMonth(newAcademicDegree.getEndMonth());
        academicDegree.setEndYear(newAcademicDegree.getEndYear());
        academicDegree.setDescription(newAcademicDegree.getDescription());

        AcademicDegree savedAcademicDegree = academicDegreeRepository.save(academicDegree);

        profile.getEducation().set(index, savedAcademicDegree);

        ProfilePayload.ProfileResponse profileResponse = new ProfilePayload.ProfileResponse(profile);

        return ResponseEntity.ok(new BaseResponse(true, null, profileResponse));
    }

    public ResponseEntity<?> deleteAcademicDegree(Integer index, User user) {

        Profile profile = profileRepository.findByUser(user).orElse(null);

        AcademicDegree academicDegree = profile.getEducation().remove(index.intValue());

        academicDegreeRepository.delete(academicDegree);

        ProfilePayload.ProfileResponse profileResponse = new ProfilePayload.ProfileResponse(profile);

        return ResponseEntity.ok(new BaseResponse(true, null, profileResponse));
    }

    public ResponseEntity<?> addJob(Job job, User user) {

        Profile profile = profileRepository.findByUser(user).orElse(null);

        job.setProfile(profile);

        jobRepository.save(job);

        ProfilePayload.ProfileResponse profileResponse = new ProfilePayload.ProfileResponse(profile);

        return ResponseEntity.ok(new BaseResponse(true, null, profileResponse));
    }

    public ResponseEntity<?> updateJob(Integer index, Job newJob, User user) {

        Profile profile = profileRepository.findByUser(user).orElse(null);

        Job job = profile.getExperience().get(index);

        job.setPosition(newJob.getPosition());
        job.setCompany(newJob.getCompany());
        job.setLocation(newJob.getLocation());
        job.setStartMonth(newJob.getStartMonth());
        job.setStartYear(newJob.getStartYear());
        job.setEndMonth(newJob.getEndMonth());
        job.setEndYear(newJob.getEndYear());
        job.setCurrentlyWorking(newJob.getCurrentlyWorking());
        job.setDescription(newJob.getDescription());

        Job savedJob = jobRepository.save(job);

        profile.getExperience().set(index, savedJob);

        ProfilePayload.ProfileResponse profileResponse = new ProfilePayload.ProfileResponse(profile);

        return ResponseEntity.ok(new BaseResponse(true, null, profileResponse));
    }

    public ResponseEntity<?> deleteJob(Integer index, User user) {

        Profile profile = profileRepository.findByUser(user).orElse(null);

        Job job = profile.getExperience().remove(index.intValue());

        jobRepository.delete(job);

        ProfilePayload.ProfileResponse profileResponse = new ProfilePayload.ProfileResponse(profile);

        return ResponseEntity.ok(new BaseResponse(true, null, profileResponse));
    }

//    public ResponseEntity<?> addCertificate(Certificate certificate, UserDetails userDetails) {
//
//        Profile profile = profileRepository.findById(userDetails.getId()).orElse(null);
//
//        profile.getCertifications().add(certificate);
//
//        Profile savedProfile = profileRepository.save(profile);
//
//        ProfileResponse profileResponse = new ProfileResponse();
//
//        BeanUtils.copyProperties(savedProfile, profileResponse);
//
//        return ResponseEntity.ok(new BaseResponse(true, null, profileResponse));
//    }
//
//    public ResponseEntity<?> updateCertificate(Integer index, Certificate certificate, UserDetails userDetails) {
//
//        Profile profile = profileRepository.findById(userDetails.getId()).orElse(null);
//
//        profile.getCertifications().set(index, certificate);
//
//        Profile savedProfile = profileRepository.save(profile);
//
//        ProfileResponse profileResponse = new ProfileResponse();
//
//        BeanUtils.copyProperties(savedProfile, profileResponse);
//
//        return ResponseEntity.ok(new BaseResponse(true, null, profileResponse));
//    }
//
//    public ResponseEntity<?> deleteCertificate(Integer index, UserDetails userDetails) {
//
//        Profile profile = profileRepository.findById(userDetails.getId()).orElse(null);
//
//        profile.getCertifications().remove(index.intValue());
//
//        Profile savedProfile = profileRepository.save(profile);
//
//        ProfileResponse profileResponse = new ProfileResponse();
//
//        BeanUtils.copyProperties(savedProfile, profileResponse);
//
//        return ResponseEntity.ok(new BaseResponse(true, null, profileResponse));
//    }
//
    public ResponseEntity<?> uploadCertificate(Integer index, MultipartFile file, User user) {

        Profile profile = profileRepository.findByUser(user).orElse(null);

        AcademicDegree academicDegree = profile.getEducation().get(index);

        if (academicDegree.getCertificateURL() != null) {
            NinasUtil.deleteFromCloudinary(academicDegree.getCertificateURL());
        }

        String response = NinasUtil.storeToCloudinary(file, "academic-degree-certificates/");

        academicDegree.setCertificateURL(response);

        academicDegreeRepository.save(academicDegree);

        return ResponseEntity.ok(new BaseResponse(true, "Certificate Saved Successfully", response));
    }

    public ResponseEntity<?> removeCertificate(Integer index, User user) {

        Profile profile = profileRepository.findByUser(user).orElse(null);

        AcademicDegree academicDegree = profile.getEducation().get(index);

        NinasUtil.deleteFromCloudinary(academicDegree.getCertificateURL());

        academicDegree.setCertificateURL(null);

        academicDegreeRepository.save(academicDegree);

        return ResponseEntity.ok(new BaseResponse(true, "Certificate Image Deleted Successfully", null));
    }
//
//    public ResponseEntity<?> uploadCertification(Integer index, MultipartFile file, UserDetails userDetails) {
//
//        Profile profile = profileRepository.findById(userDetails.getId()).orElse(null);
//
//        if (profile.getCertifications().get(index).getCertificateURL() != null) {
//            NinasUtil.deleteFromCloudinary(profile.getEducation().get(index).getCertificateURL());
//        }
//
//        String response = NinasUtil.storeToCloudinary(file, "listing-previews/");
//
//        profile.getEducation().get(index).setCertificateURL(response);
//
//        profileRepository.save(profile);
//
//        return ResponseEntity.ok(new BaseResponse(true, "Certificate Saved Successfully", response));
//    }
//
//    public ResponseEntity<?> removeCertification(Integer index, UserDetails userDetails) {
//
//        Profile profile = profileRepository.findById(userDetails.getId()).orElse(null);
//
//        NinasUtil.deleteFromCloudinary(profile.getEducation().get(index).getCertificateURL());
//
//        profile.getCertifications().get(index).setCertificateURL(null);
//
//        profileRepository.save(profile);
//
//        return ResponseEntity.ok(new BaseResponse(true, "Certificate Image Deleted Successfully", null));
//    }
//
//    public ResponseEntity<?> uploadJobEvidence(Integer evidenceIndex, Integer index, MultipartFile file, UserDetails userDetails) {
//
//        Profile profile = profileRepository.findByUser(userDetails.getUser()).orElse(null);
//
//        Job job = profile.getExperience().get(index).;
//
//
//        String oldURL = job.getEvidenceURLs().get(evidenceIndex);
//
//        if (oldURL != null) {
//            NinasUtil.deleteFromCloudinary(profile.getExperience().get(index).getEvidenceURLs().get(evidenceIndex));
//        }
//
//        String response = NinasUtil.storeToCloudinary(file, "listing-previews/");
//
//        profile.getExperience().get(index).getEvidenceURLs().set(evidenceIndex, response);
//
//        profileRepository.save(profile);
//
//        return ResponseEntity.ok(new BaseResponse(true, "CAB Evidence Image Saved Successfully", response));
//    }
//
//    public ResponseEntity<?> removeJobEvidence(Integer evidenceIndex, Integer index, UserDetails userDetails) {
//
//        Profile profile = profileRepository.findById(userDetails.getId()).orElse(null);
//
//        NinasUtil.deleteFromCloudinary(profile.getExperience().get(index).getEvidenceURLs().get(evidenceIndex));
//
//        profile.getExperience().get(index).getEvidenceURLs().set(evidenceIndex, null);
//
//        profileRepository.save(profile);
//
//        return ResponseEntity.ok(new BaseResponse(true, "Job Evidence Image Deleted Successfully", null));
//
//    }
//
//    public ResponseEntity<?> getProfileTemplate() {
//
//        ByteArrayOutputStream stream;
//
//        try {
//            XSSFWorkbook workbook = templateService.generateSpreadSheetTemplate(ProfileRequest.class);
//
//            stream = new ByteArrayOutputStream();
//
//            workbook.write(stream);
//
//            return ResponseEntity.ok()
//                    .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
//                    .header("Content-Disposition", "download; filename=ProfilesTemplate.xlsx")
//                    .body(stream.toByteArray());
//        } catch (IOException ex) {
//
//            throw new BaseException("The template could not be retrieved", HttpStatus.EXPECTATION_FAILED);
//        }
//    }
//
//    public ResponseEntity<?> uploadProfiles(MultipartFile file) throws Exception {
//
//        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
//
//        List<ProfileRequest> list = templateService.importSpreadSheet(ProfileRequest.class, workbook.getSheetAt(0));
//
//        List<Profile> profiles = new ArrayList<>();
//
//        list.forEach((empR) -> {
//
//            Profile profile = new Profile();
//
//            BeanUtils.copyProperties(empR, profile);
//
//            profiles.add(profile);
//
//        });
//
//        List<Profile> savedProfiles = profileRepository.saveAll(profiles);
//
//        BaseResponse baseResponse = new BaseResponse(true, "Profiles Imported Successfully", savedProfiles);
//
//        return ResponseEntity.ok(baseResponse);
//    }
}
