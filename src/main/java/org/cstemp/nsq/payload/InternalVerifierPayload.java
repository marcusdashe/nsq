/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.payload;


import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.cstemp.nsq.util.XCellDetail;
import java.util.List;

/**
 *
 * @author chibuezeharry & MarcusDashe
 */


public class InternalVerifierPayload {

    @Data
    public static class InternalVerifierRequest {

        @NotNull(message = "Programme must be set")
        private Long programmeId;

        @NotNull(message = "Centre must be set")
        private Long centreId;

        private String message;

    }

    @Data
    public static class Invite {

        @XCellDetail(indexed = true, label = "Assessor Email Address")
        private String email;

    }

    @Data
    public static class Invites {

        private Long centreId;

        private List<String> emails;

    }

    private InternalVerifierPayload() {

    }
}
