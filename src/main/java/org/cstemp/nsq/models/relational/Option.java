/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.models.relational;

import jakarta.persistence.*;
import lombok.Data;


/**
 *
 * @author chibuezeharry & MarcusDashe
 */
@Data
@Entity
@Table(name = "system_options")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String groupName;

    private String code;

    private String optionValue;

}
