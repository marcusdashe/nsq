/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.payload;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.cstemp.nsq.models.Enums;


import java.util.Date;
import java.util.List;

/**
 *
 * @author chibuezeharry
 */
public class PortfolioPayload {

    @Data
    public static class PortfolioRequest {

        @NotNull
        private Long centre;

        @NotNull
        private Long programme;

        @NotEmpty
        private List<Long> courses;
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
