/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.payload;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

/**
 *
 * @author chibuezeharry
 */
public class UtilPayload {

    @Data
    public static class NotificationResponse {

        private String receiverName;

        private String senderName;

        private String type;

        private String title;

        private String description;

        private Boolean accepted;

        private Boolean read;

        private LocalDate assessmentDate;

        private LocalDate acceptanceDeadline;

        private Instant createdAt;

        private Instant updatedAt;
    }

}
