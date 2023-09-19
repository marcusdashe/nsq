/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.models.relational;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cstemp.nsq.models.BaseModel;
import org.cstemp.nsq.models.relational.*;

import java.util.Date;
import java.util.List;

/**
 * @author chibuezeharry & MarcusDashe
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "programmes")
public class Programme extends BaseModel {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 75)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)

    private String description;

    private String outcomes;

    private String requirements;

    @Basic(optional = false)
    @NotNull
    @Column(name = "fee")
    private double fee;

    @Basic(optional = false)
    @NotNull
    @Column(name = "discounted_fee")
    private double discountedFee;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "programme")
    private List<Course> courses;

    @Column(name = "courses_limit")
    private short coursesLimit;

    @Column(name = "public_enrolment")
    private boolean publicEnrolment = true;

    private boolean visibility;

    @Basic(optional = false)
    @NotNull
    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Basic(optional = false)
    @NotNull
    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "programmes")
    private List<Centre> centres;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "programme")
    private List<Assessor> assessors;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "programme")
    private List<InternalVerifier> internalVerifiers;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "programme")
    private List<ExternalVerifier> externalVerifiers;

    public Programme(Long id) {
        this.id = id;
    }

}
