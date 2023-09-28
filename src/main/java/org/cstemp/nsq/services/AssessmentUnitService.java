/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cstemp.nsq.exception.BadRequestException;
import org.cstemp.nsq.exception.BaseException;
import org.cstemp.nsq.models.relational.*;
import org.cstemp.nsq.payload.AssessmentPayload;
import org.cstemp.nsq.payload.BaseResponse;
import org.cstemp.nsq.repos.AssessmentUnitRepository;
import org.cstemp.nsq.repos.LearningOutcomeRepository;
import org.cstemp.nsq.repos.PerformanceCriteriaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AssessmentUnitService {

    @Autowired
    private AssessmentUnitRepository assessmentUnitRepository;

    private LearningOutcomeRepository learningOutcomeRepository;

    private PerformanceCriteriaRepository performanceCriteriaRepository;

    @Autowired
    private TemplateService templateService;

    public ResponseEntity<?> getAssessmentUnitsByCourse(Long courseId) {

        Course course = new Course(courseId);

        List<AssessmentUnit> assessmentUnits = assessmentUnitRepository.findAllByCourse(course);

        return ResponseEntity.ok(new BaseResponse(true, "Assessment Units retrieved successfully", assessmentUnits));

    }

    public ResponseEntity<?> addAssessmentUnit(AssessmentUnit assessmentUnit, Long courseId) {

        assessmentUnit.setCourse(new Course(courseId));

        AssessmentUnit savedAssessmentUnit = assessmentUnitRepository.save(assessmentUnit);

        BaseResponse baseResponse = new BaseResponse<>(true, "Assessment Unit added successfully", savedAssessmentUnit);

        return ResponseEntity.ok(baseResponse);
    }

    public ResponseEntity<?> updateAssessmentUnit(AssessmentUnit assessmentUnit, Long courseId) {

        assessmentUnit.setCourse(new Course(courseId));

        AssessmentUnit savedAssessmentUnit = assessmentUnitRepository.save(assessmentUnit);

        BaseResponse baseResponse = new BaseResponse<>(true, "Assessment Unit updated successfully", savedAssessmentUnit);

        return ResponseEntity.ok(baseResponse);
    }

    public ResponseEntity<?> deleteAssessmentUnit(Long assessmentUnitId) {

        AssessmentUnit assessmentUnit = assessmentUnitRepository.findById(assessmentUnitId)
                .orElseThrow(() -> new BaseException("Assessment Unit Not Found", HttpStatus.NOT_FOUND));

        learningOutcomeRepository.deleteAll(assessmentUnit.getLearningOutcomes());

        performanceCriteriaRepository.deleteAll(assessmentUnit.getPerformanceCriteria());

        assessmentUnitRepository.delete(assessmentUnit);

        BaseResponse baseResponse = new BaseResponse<>(true, "Assessment Unit deleted successfully", null);

        return ResponseEntity.ok(baseResponse);
    }

    public ResponseEntity<?> addLearningOutcome(LearningOutcome learningOutcome, Long unitId) {

        learningOutcome.setAssessmentUnit(new AssessmentUnit(unitId));

        LearningOutcome savedLearningOutcome = learningOutcomeRepository.save(learningOutcome);

        BaseResponse baseResponse = new BaseResponse(true, "Learning Outcome added Successfully", savedLearningOutcome);

        return ResponseEntity.ok(baseResponse);
    }

    public ResponseEntity<?> updateLearningOutcome(LearningOutcome newLearningOutcome, Long learningOutcomeId) {

        LearningOutcome oldLearningOutcome = learningOutcomeRepository.findById(learningOutcomeId)
                .orElseThrow(() -> new BaseException("Learning Outcome Not Found"));

        BeanUtils.copyProperties(newLearningOutcome, oldLearningOutcome);

        oldLearningOutcome.setId(learningOutcomeId);

        LearningOutcome savedLearningOutcome = learningOutcomeRepository.save(oldLearningOutcome);

        BaseResponse baseResponse = new BaseResponse<>(true, "Learning Outcome updated successfully", savedLearningOutcome);

        return ResponseEntity.ok(baseResponse);
    }

    public ResponseEntity<?> deleteLearningOutcome(Long learningOutcomeId) {

        LearningOutcome learningOutcome = learningOutcomeRepository.findById(learningOutcomeId)
                .orElseThrow(() -> new BaseException("Learning Outcome Not Found", HttpStatus.NOT_FOUND));

        performanceCriteriaRepository.deleteAll(learningOutcome.getPerformanceCriteria());

        learningOutcomeRepository.delete(learningOutcome);

        BaseResponse baseResponse = new BaseResponse<>(true, "Learning Outcome deleted successfully", null);

        return ResponseEntity.ok(baseResponse);
    }

    public ResponseEntity<?> addPerformanceCriteria(PerformanceCriteria performanceCriteria, Long learningOutcomeId) throws Throwable {

        AssessmentUnit assessmentUnit = learningOutcomeRepository.findById(learningOutcomeId)
                .orElseThrow(() -> new BadRequestException("Learning Outcome Id Not Found")).getAssessmentUnit();

        performanceCriteria.setLearningOutcome(new LearningOutcome(learningOutcomeId));

        performanceCriteria.setAssessmentUnit(assessmentUnit);

        PerformanceCriteria savedPerformanceCriteria = performanceCriteriaRepository.save(performanceCriteria);

        BaseResponse baseResponse = new BaseResponse(true, "Performance Criteria Saved Successfully", savedPerformanceCriteria);

        return ResponseEntity.ok(baseResponse);
    }

    public ResponseEntity<?> updatePerformanceCriteria(PerformanceCriteria newPerformanceCriteria, Long performanceCriteriaId) {

        PerformanceCriteria oldPerformanceCriteria = performanceCriteriaRepository.findById(performanceCriteriaId)
                .orElseThrow(() -> new BaseException("Learning Outcome Not Found"));

        BeanUtils.copyProperties(newPerformanceCriteria, oldPerformanceCriteria);

        oldPerformanceCriteria.setId(performanceCriteriaId);

        PerformanceCriteria savedPerformanceCriteria = performanceCriteriaRepository.save(oldPerformanceCriteria);

        BaseResponse baseResponse = new BaseResponse<>(true, "Performance Criteria updated successfully", savedPerformanceCriteria);

        return ResponseEntity.ok(baseResponse);
    }

    public ResponseEntity<?> deletePerformanceCriteria(Long id) {

        performanceCriteriaRepository.deleteById(id);

        BaseResponse baseResponse = new BaseResponse<>(true, "Performance Criteria deleted successfully", null);

        return ResponseEntity.ok(baseResponse);
    }

    public ResponseEntity getTemplate(String type) {

        Class aClass = type != "pc" ? type != "lo" ? AssessmentPayload.AssessmentUnit.class : AssessmentPayload.LearningOutcome.class : AssessmentPayload.PerformanceCriteria.class;

        ByteArrayOutputStream stream;

        try {
            XSSFWorkbook workbook = templateService.generateSpreadSheetTemplate(aClass);

            stream = new ByteArrayOutputStream();

            workbook.write(stream);

            return ResponseEntity.ok()
                    .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .header("Content-Disposition", "download; filename=" + aClass.getSimpleName() + ".xlsx")
                    .body(stream.toByteArray());
        } catch (IOException ex) {

            throw new BaseException("The template could not be retrieved", HttpStatus.EXPECTATION_FAILED);
        }
    }

    public ResponseEntity<?> upload(MultipartFile file, String type) throws Exception {

        Class aClass = !"pc".equals(type) ? !"lo".equals(type) ? AssessmentPayload.AssessmentUnit.class : AssessmentPayload.LearningOutcome.class : AssessmentPayload.PerformanceCriteria.class;

        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());

        List importedList = templateService.importSpreadSheet(aClass, workbook.getSheetAt(0));

        List list = new ArrayList<>();

//        cast the objects into their proper type
        importedList.forEach((imported) -> {

            Object obj;

            try {
                obj = aClass.getDeclaredConstructor().newInstance();
            } catch (Exception ex) {
                throw new BaseException(ex.getMessage());
            }

            BeanUtils.copyProperties(imported, obj);

            list.add(obj);
        });

        if (type == "pc") {
            performanceCriteriaRepository.saveAll(list);
        } else if (type == "lo") {
            learningOutcomeRepository.saveAll(list);
        } else {
            assessmentUnitRepository.saveAll(list);
        }

        BaseResponse baseResponse = new BaseResponse(true, aClass.getSimpleName() + "s Imported Successfully", list);

        return ResponseEntity.ok(baseResponse);
    }

    public ResponseEntity<?> getAssessments() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public ResponseEntity<?> getAssessmentUnitsByAssessor(User user) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public ResponseEntity<?> getAssessmentsByAssessorWithId(Long assessorId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public ResponseEntity<?> deleteAssessment(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
