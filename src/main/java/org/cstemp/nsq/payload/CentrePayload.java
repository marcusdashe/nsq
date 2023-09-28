/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.payload;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.cstemp.nsq.util.OptionType;
import org.cstemp.nsq.util.XCellDetail;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author chibuezeharry & MarcusDashe
 *
 */

public class CentrePayload {

    @Data
    public static class CentreRequest {

        @NotNull(message = "Name is required")
        @XCellDetail(indexed = true, label = "Centre Name")
        private String name;

        @NotNull(message = "Address is required")
        @XCellDetail(indexed = true, label = "Centre Address")
        private String address;

        @NotNull(message = "State is required")
        @XCellDetail(indexed = true)
        @OptionType("STATE")
        private String countryState;

        private List<Long> programmes = new ArrayList<>();

        @NotNull(message = "Capacity is required")
        @Min(10)
        @Max(10000)
        @XCellDetail(indexed = true)
        private Integer capacity;

        @XCellDetail(indexed = true)
        @OptionType("BOOLEAN")
        private Boolean active = false;
    }

    @Data
    public static class CentreResponse {

        private String id;

        private String name;

        private String address;

        private String city;

        private String state;
    }
}
