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

import java.util.Date;
import java.util.List;

import static jakarta.persistence.TemporalType.TIMESTAMP;


/**
 *
 * @author chibuezeharry & MarcusDashe
 */

@Data
@Entity
@Table(name = "portfolio", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "centre_id", "course_id"})})
public class Portfolio extends BaseModel {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    String photoUrl;

    String name;

    String phone;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programme_id")
    private Programme programme;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "centre_id")
    private Centre centre;

    private String searchString = "";

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "portfolio")
    private List<PortfolioItem> portfolioItems;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "portfolio")
    private List<Assessment> assessments;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "portfolio")
    private List<Track> tracks;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessor_id")
    private Assessor assessor;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "internal_verifier_id")
    private InternalVerifier internalVerifier;

    @Temporal(TIMESTAMP)
    private Date acceptedDate;

    private Boolean active = false;

    private Boolean completed = false;

    private Boolean abandoned = false;

    public void setUser(User user) {
        this.photoUrl = user.getPhotoUrl();
        this.name = user.getFullName();
        this.phone = user.getPhone();
        this.user = user;
    }

    @Transient
    public String getProgrammeTitle() {
        return programme.getTitle();
    }

    @Transient
    public String getCourseTitle() {
        return course.getTitle();
    }

    @Transient
    public String getCentreName() {
        return centre.getName();
    }

    @Transient
    public String getAssigned() {
        return assessor == null ? null : assessor.getUser().getEmail();
    }

    @Transient
    public Integer getItemsCount() {
        return portfolioItems.size();
    }

    @Transient
    public Integer getAssessedItemsCount() {
        return assessments.size();
    }

    @PrePersist
    public void saveSearchString() {

        searchString = programme.getTitle() + " " + user.getFullName();

        if (centre != null) {
            searchString += " " + centre.getName();
        }
    }

    @PreUpdate
    public void updateSearchString() {

        searchString = programme.getTitle() + " " + user.getFullName();

        if (centre != null) {
            searchString += " " + centre.getName();
        }
    }
}
