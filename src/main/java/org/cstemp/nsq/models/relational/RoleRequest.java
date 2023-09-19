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
import org.cstemp.nsq.models.relational.Centre;
import org.cstemp.nsq.models.relational.Programme;
import org.cstemp.nsq.models.relational.User;

import java.util.Date;

import static jakarta.persistence.TemporalType.TIMESTAMP;


/**
 *
 * @author chibuezeharry & MarcusDashe
 *
 * Represents a request to a centre to perform any of the given roles.
 * <br/> --| ASSESSOR <br/> --| INTERNAL_VERIFIER <br/> --| EXTERNAL_VERIFIER
 */

@Data
@Entity
@Table(name = "role_request", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"requestor_id", "centre_id", "programme_id"})})
public class RoleRequest extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @JoinColumn(name = "programme_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Programme programme;

    @JsonIgnore
    @JoinColumn(name = "centre_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Centre centre;

    @JsonIgnore
    @JoinColumn(name = "requestor_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User requestor;

    @Temporal(TIMESTAMP)
    private Date acceptDate;

    @Temporal(TIMESTAMP)
    private Date rejectDate;

    private String message;

    private String role = "INVALID";

    @Transient
    public String getUserFullName() {
        return requestor.getFirstname() + " " + requestor.getLastname();
    }

    @Transient
    public String getCentreName() {
        return centre.getName();
    }

    @Transient
    public String getProgrammeTitle() {
        return programme.getTitle();
    }

}
