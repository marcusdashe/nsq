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

/**
 *
 * @author chibuezeharry & MarcusDashe
 */
public interface LearningOutcomeRepository extends JpaRepository<LearningOutcome, Long> {

    List<LearningOutcome> findAllByAssessmentUnit(AssessmentUnit assessmentUnit);
}
