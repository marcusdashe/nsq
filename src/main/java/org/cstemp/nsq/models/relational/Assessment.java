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


/**
 * @author chibuezeharry & MarcusDashe
 */
@Data
@Entity
@Table(name = "assessment")
public class Assessment extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @JsonIgnore
    @JoinColumn(name = "trainee_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User trainee;

    @JsonIgnore
    @JoinColumn(name = "assessor_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User assessor;

    private Boolean valid;

    private Boolean sufficient;

    private String remark;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_item_id")
    private PortfolioItem portfolioItem;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    public Assessment() {

    }

    public Assessment(Long id) {
        this.id = id;
    }

}
