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
import org.cstemp.nsq.models.Enums;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author chibuezeharry & MarcusDashe
 */

@Data
@Entity
@Table(name = "portfolio_item")
public class PortfolioItem extends BaseModel {

    @Transient
    private static final String TRACK_DELIMETER = ",,";

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    private String level = "";

    private String performanceCriteria;

    private String type;

    private Enums.LogType logType;

    private String evidenceType;

    private Enums.TimeSpan timeSpan;

    private String title;

    private String description;

    private String mediaUrl;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST}, mappedBy = "portfolioItem")
    private Assessment assessment;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "portfolioItem",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Track> tracks = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "portfolioItem",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Evidence> evidences = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "portfolioItem",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Remark> remarks = new ArrayList<>();

    private LocalDate logDate;

    @Transient
    public List<String> getTracks() {

        if (level == null) {
            return new ArrayList<>();
        }
        List<String> tracks = new ArrayList<>();

        for (String track : level.split(TRACK_DELIMETER)) {
            tracks.add(track);
        }

        return tracks;
    }

    @Transient
    public void setTracks(List<String> tracks) {
        if (tracks.isEmpty()) {
            return;
        }
        level = tracks.get(0);
        for (int index = 1; index < tracks.size(); index++) {
            level += TRACK_DELIMETER + tracks.get(index);
        }
    }
}
