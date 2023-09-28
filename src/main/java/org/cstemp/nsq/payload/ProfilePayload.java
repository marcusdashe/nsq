/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.payload;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.cstemp.nsq.models.relational.AcademicDegree;
import org.cstemp.nsq.models.relational.Certificate;
import org.cstemp.nsq.models.relational.Job;
import org.cstemp.nsq.models.relational.Profile;
import org.cstemp.nsq.util.OptionType;
import org.cstemp.nsq.util.XCellDetail;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author chibuezeharry
 */
public class ProfilePayload {

    @Data
    public static class ProfileRequest {

        @NotBlank(message = "Gender is required!")
        @XCellDetail(indexed = true)
        @OptionType("GENDER")
        private String gender;

        @NotBlank(message = "Your city is required!")
        @XCellDetail(indexed = true)
        private String city;

        @NotBlank(message = "Your address is required!")
        @XCellDetail(indexed = true)
        private String address;

        @NotBlank(message = "Your state of residence is required!")
        @XCellDetail(indexed = true, label = "State Of Residence")
        @OptionType("STATE")
        private String stateOfResidence;

        @NotBlank(message = "Your state of origin is required!")
        @XCellDetail(label = "State Of Origin")
        @OptionType("STATE")
        private String stateOfOrigin;

        @NotBlank(message = "Your nationality is required!")
        @OptionType("NATIONALITY")
        private String nationality;

        @NotBlank(message = "Your phone number is required!")
        @Pattern(regexp = "^\\(?(\\d{4})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message = "Invalid phone/fax format, should be as xxxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
        @XCellDetail(label = "Phone Number")
        private String phone;

        private String placeOfBirth;

        @XCellDetail(indexed = true, label = "Date Of Birth")
        private Date dateOfBirth;

        private String trade;

        @NotNull(message = "Years of experience required!")
        @XCellDetail(label = "Years Of Experience")
        private Integer yearsOfExperience;

        private String description;

    }

    @Data
    public static class ProfileResponse {

        private String id;

        private String firstName = "";

        private String lastName = "";

        private String email;

        private String phone = "";

        private String photoUrl;

        private String description;

        private String gender;

        private String city;

        private String address;

        private String stateOfResidence;

        private String stateOfOrigin;

        private String nationality;

        private String placeOfBirth;

        private Date dateOfBirth;

        private List<AcademicDegree> education = new LinkedList<>();

        private List<Job> experience = new LinkedList<>();

        private List<Certificate> certifications = new LinkedList<>();

        private String trade;

        private Integer yearsOfExperience;

        public String getName() {
            return firstName + " " + lastName;
        }

        public ProfileResponse(){

        }

        public ProfileResponse(Profile profile){

            BeanUtils.copyProperties(profile, this);

            BeanUtils.copyProperties(profile.getUser(), this);

            setPhone(profile.getUser().getPhone());

        }

        public LocalDate getDateOfBirth(){
            if(dateOfBirth == null){
                return null;
            }
            Date accessibleDate = new Date(dateOfBirth.getTime());
            return LocalDate.ofInstant(accessibleDate.toInstant(), ZoneId.systemDefault());
//
//                    LocalDate localDateOfBirth = LocalDate.ofInstant(profile.getDateOfBirth().toInstant(), ZoneId.systemDefault());
//
//            profileResponse.setPhone(profile.getUser().getPhone());
//            profileResponse.setDateOfBirth(localDateOfBirth);
        }

    }
}
