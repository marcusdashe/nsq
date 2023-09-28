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
import org.cstemp.nsq.models.Enums;
import org.cstemp.nsq.util.OptionType;
import org.cstemp.nsq.util.XCellDetail;

import java.time.LocalDate;
import java.util.Date;

/**
 *
 * @author chibuezeharry
 */
public class TraineePayload {

    @Data
    public static class TraineeRequest {

        @NotBlank(message = "Your Unique Learner Number is required!")
        @XCellDetail(indexed = true, label = "Unique Learner Number")
        private String uniqueLearnerNumber;

        @ NotBlank(message = "Gender is required!")
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

        private String placeOfBirth;

        @XCellDetail(indexed = true, label = "Date Of Birth")
        private LocalDate dateOfBirth;

        @NotBlank(message = "Please select a trade!")
        @XCellDetail(indexed = true)
        @OptionType("TRADE")
        private String trade;

        @NotBlank(message = "Please select a centre!")
        @XCellDetail(indexed = true, label = "Centre Name")
        private String centreId;

        @NotNull(message = "Years of experience required!")
        @Pattern(regexp = "[0-1]{0,1}[0-9]", message = "Years of experience must be below 20")
        @XCellDetail(label = "Years Of Experience")
        private String yearsOfExperience;

        private String description;

    }

    @Data
    public static class LogSheetRequest {

        private Enums.LogType logType;

        private Enums.TimeSpan sheetType;

        private String title;

        private String description;

        private Date logDate;

    }
}
