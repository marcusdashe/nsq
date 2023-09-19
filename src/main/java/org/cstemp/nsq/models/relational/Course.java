/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.models.relational;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.cstemp.nsq.models.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chibuezeharry & MarcusDashe
 */

@Data
@Entity
@Table(name = "course")
public class Course extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 255)
    private String title;

    @Column(name = "short_description")
    private String shortDescription;

    private String description;

    @Size(max = 50)
    private String level;

    @JsonIgnore
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User creator;

    @Size(max = 255)
    private String thumbnailUrl;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programme_id")
    Programme programme;

    private Boolean mandatory;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
    List<AssessmentUnit> assessmentUnits = new ArrayList<>();

    public Course() {

    }

    public Course(Long id) {
        this.id = id;
    }

}
