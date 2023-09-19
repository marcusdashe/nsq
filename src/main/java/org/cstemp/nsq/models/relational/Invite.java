/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.models.relational;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import org.cstemp.nsq.models.BaseModel;
import org.cstemp.nsq.models.relational.Centre;

/**
 *
 * @author chibuezeharry & MarcusDashe
 */
@Data
@Entity
@Table(name = "invite")
public class Invite extends BaseModel {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @NotNull
    private String email;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "centre_id")
    private Centre centre;

    String role;

    private Boolean accepted;
}
