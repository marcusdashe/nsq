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

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author chibuezeharry & MarcusDashe
 */
@Data
@Entity
@Table(name = "nsq_centres")
public class Centre extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 1, max = 255)
    private String name = "";

    private String address = "";

    @Size(min = 1, max = 55)
    private String countryState;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "centre_to_programme_map",
            joinColumns = {
                @JoinColumn(name = "centre_id")},
            inverseJoinColumns = {
                @JoinColumn(name = "programme_id")})
    private Set<Programme> programmes = new HashSet<>();

    private Integer capacity = 0;

    private Boolean active = false;

    private Boolean autoAcceptAssessor = true;

    private Boolean autoAssignAssessor;

    private Boolean autoAcceptInternalVerifier = true;

    private Boolean autoAssignInternalVerifier;

    @JsonIgnore
    @JoinColumn(name = "user_id")
    @OneToOne(fetch = FetchType.LAZY)
    private User admin;

    public Centre() {

    }

    public Centre(Long id) {
        this.id = id;
    }

    @Transient
    public Long traineeCount;

    @Transient
    public Long assessorCount;

    @Transient
    public Long internalVerifierCount;

}
