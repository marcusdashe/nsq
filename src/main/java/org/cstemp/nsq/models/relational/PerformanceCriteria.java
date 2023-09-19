/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.models.relational;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.cstemp.nsq.models.BaseModel;
import org.cstemp.nsq.models.relational.AssessmentUnit;
import org.cstemp.nsq.models.relational.LearningOutcome;


/**
 * @author chibuezeharry & MarcusDashe
 */


@Data
@Entity
@Table(name = "performance_criteria")
public class PerformanceCriteria extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer pcIndex;

    @Size(max = 255)
    private String title;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id")
    private AssessmentUnit assessmentUnit;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "learning_outcome_id")
    private LearningOutcome learningOutcome;
}
