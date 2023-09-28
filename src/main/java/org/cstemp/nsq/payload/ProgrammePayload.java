package org.cstemp.nsq.payload;

/*
* @author marcusdashe
* */

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.cstemp.nsq.models.relational.Programme;
import org.springframework.beans.BeanUtils;


import java.util.Date;

public class ProgrammePayload {
    @Data
    public static class ProgrammeRequest{
        @NotBlank(message="Title is required")
        private String title;

        @NotBlank(message="Description is required")
        private String description;

        @NotBlank(message="Outcomes is required")
        private String outcomes;

        @NotBlank(message="Requirements is required")
        private String requirements;

        @NotNull(message="Fees is required")
        private double fee;

        @NotNull(message="Discounted fee is required")
        private double discountedFee = 0.0;

        @NotNull(message="Courses Limit is required")
        private short coursesLimit;

        @NotNull(message="Visibility is required")
        private boolean visibility;

        @NotNull(message="Start date is required")
        private Date startDate;

        @NotNull(message="End date is required")
        private Date endDate;
    }

    @Data
    public static class ProgrammeResponse{
        private Long id;
        private String title;

        private String description;

        private String outcomes;

        private String requirements;

        private double fee;

        private double discountedFee;

        private short coursesLimit;

        private boolean visibility;

        private Date startDate;

        private Date endDate;

        public ProgrammeResponse(){

        }

        public ProgrammeResponse(Programme programme){
            BeanUtils.copyProperties(programme, this);
        }
    }

}
