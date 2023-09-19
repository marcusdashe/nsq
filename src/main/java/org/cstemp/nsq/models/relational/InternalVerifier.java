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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author chibuezeharry & MarcusDashe
 */

@Data
@Entity
@Table(name = "internal_verifiers")
public class InternalVerifier extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    private Programme programme;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "iv_to_centre_map",
            joinColumns = {
                @JoinColumn(name = "iv_id")},
            inverseJoinColumns = {
                @JoinColumn(name = "centre_id")})
    private List<Centre> centres = new ArrayList<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "internalVerifier")
    private List<Portfolio> portfolio = new ArrayList<>();
}
