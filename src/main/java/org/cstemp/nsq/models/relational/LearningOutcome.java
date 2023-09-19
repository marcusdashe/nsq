/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.models.relational;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.cstemp.nsq.models.BaseModel;

import java.util.List;

/**
 * @author chibuezeharry & MarcusDashe
 */
@Data
@Entity
@Table(name = "learning_outcomes")
public class LearningOutcome extends BaseModel {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    private Short loIndex;

    private String title;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id")
    private AssessmentUnit assessmentUnit;

    @OneToMany(mappedBy = "learningOutcome")
    private List<PerformanceCriteria> performanceCriteria;

    public LearningOutcome(){

    }

    public LearningOutcome(Long id){
        this.id = id;
    }


}
