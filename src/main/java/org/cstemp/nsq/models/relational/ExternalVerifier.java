/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.models.relational;

import jakarta.persistence.*;
import lombok.Data;
import org.cstemp.nsq.models.BaseModel;

import java.util.List;

/**
 *
 * @author chibuezeharry & MarcusDashe
 */

@Data
@Entity
@Table(name = "external_verifiers")
public class ExternalVerifier extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "programme_id")
    private Programme programme;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ev_to_centre_map",
            joinColumns = {
                @JoinColumn(name = "ev_id")},
            inverseJoinColumns = {
                @JoinColumn(name = "centre_id")})
    private List<Centre> centres;

}
