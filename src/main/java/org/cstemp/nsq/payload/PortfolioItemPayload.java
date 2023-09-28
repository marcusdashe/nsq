/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.payload;
import lombok.Data;
import org.cstemp.nsq.models.Enums;

import java.util.Date;

/**
 *
 * @author chibuezeharry & MarcusDashe
 */
public class PortfolioItemPayload {

    @Data
    public static class PortfolioItemRequest {

        private String type;

        private Enums.LogType logType;

        private Enums.TimeSpan timeSpan;

        private String title;

        private String description;

        private String evidenceType;

        private Date logDate;

    }

    @Data
    public static class AssessmentRequest {

        private Boolean valid;

        private Boolean sufficient;

        private String remark;

    }

    @Data
    public static class RemarkRequest {

        private String userId;

        private String remarkerRole;

        private String portfolioItemId;

        private String portfolioId;

        private String text;

    }

    private PortfolioItemPayload() {

    }

}
