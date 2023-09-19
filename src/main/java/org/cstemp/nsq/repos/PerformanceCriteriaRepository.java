/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.repos;

import org.cstemp.nsq.models.relational.AssessmentUnit;
import org.cstemp.nsq.models.relational.LearningOutcome;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.cstemp.nsq.models.relational.PerformanceCriteria;

/**
 *
 * @author chibuezeharry & MarcusDashe
 */
public interface PerformanceCriteriaRepository extends JpaRepository<PerformanceCriteria, Long> {

    List<PerformanceCriteria> findAllByAssessmentUnit(AssessmentUnit assessmentUnit);

    List<PerformanceCriteria> findAllByLearningOutcome(LearningOutcome courses);
}
