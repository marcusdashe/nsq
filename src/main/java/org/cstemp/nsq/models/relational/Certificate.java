/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.models.relational;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;


/**
 *
 * @author chibuezeharry & MarcusDashe
 */
@Data
@Entity
@Table(name = "professional_certifications")
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String body;

    private String certificateNumber;

    private String registrationNumber;

    private String issueDate;

    private String expiryDate;

    private String certificateValidationLink;

    private String certificateURL;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

}
