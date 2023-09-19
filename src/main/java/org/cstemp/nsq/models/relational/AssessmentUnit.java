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

import java.util.ArrayList;
import java.util.List;

/**
 * @author chibuezeharry & MarcusDashe
 */

@Data
@Entity
@Table(name = "assessment_unit")
public class AssessmentUnit extends BaseModel {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    private Integer unitIndex;

    private String title;

    @Size(max = 55)
    private String unitRef;

    private Integer credit;

    private Integer glh;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    private Integer qcfLevel;

    @Size(max = 55)
    private String remarks;

    private Integer totalCredit;

    private String requirements;

    private String objectives;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "assessmentUnit")
    private List<LearningOutcome> learningOutcomes = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "assessmentUnit")
    private List<PerformanceCriteria> performanceCriteria = new ArrayList<>();

     public AssessmentUnit(){

     }
     public AssessmentUnit(Long id){
         this.id = id;
     }

}
