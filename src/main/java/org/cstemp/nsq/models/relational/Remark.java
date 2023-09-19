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
 *
 * @author chibuezeharry & MarcusDashe
 */
@Data
@Entity
@Table(name = "remarks")
public class Remark extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "nsq_role")
    private String nsqRole;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "log_sheet_id")
    private PortfolioItem portfolioItem;

    private Boolean assessed;

    private Boolean valid;

    private Boolean sufficient;

    private String text;

    public String getName() {
        return user.getFirstname() + " " + user.getLastname();
    }

}
