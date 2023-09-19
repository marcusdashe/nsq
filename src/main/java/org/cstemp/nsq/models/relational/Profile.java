package org.cstemp.nsq.models.relational;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cstemp.nsq.models.BaseModel;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author chibuezeharry & MarcusDashe
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "nsq_individual_profiles")
public class Profile extends BaseModel {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    private String gender;

    private String city;

    private String address;

    private String stateOfResidence;

    private String stateOfOrigin;

    private String nationality;

    private String placeOfBirth;

    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

//    private String roles;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profile")
    private List<AcademicDegree> education = new LinkedList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profile")
    private List<Job> experience = new LinkedList<>();

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profile")
//    private List<Certificate> certifications = new LinkedList<>();

    private String trade;

    private Integer yearsOfExperience;

//    @Transient
//    public List<String> getRolesList() {
//
//        return Collections.arrayToList(roles.split(","));
//    }

//    public void addRole(String role) {
//        if ("".equals(this.roles)) {
//            this.roles = role;
//        } else {
//            this.roles += "," + role;
//        }
//    }

    @Override
    public int hashCode() {
        return id.intValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Profile other = (Profile) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
