/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.models.relational;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author chibuezeharry & MarcusDashe
 */

@Data
@Entity
@Table(name = "job_profiles")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String position;

    private String company;

    private String location;

    private String startDay;

    private String startMonth;

    private String startYear;

    private String endMonth;

    private String endYear;

    private String description;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "job")
    private List<Evidence> evidenceURLs;

    private Boolean currentlyWorking;

    public Job() {
        evidenceURLs = new ArrayList<>();
        evidenceURLs.add(null);
        evidenceURLs.add(null);
        evidenceURLs.add(null);
    }

}
