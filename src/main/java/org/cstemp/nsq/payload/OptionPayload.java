/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.payload;

import lombok.Data;

/**
 *
 * @author chibuezeharry
 */
public class OptionPayload {

    @Data
    public static class OptionRequest {

        private String name;

        private String group;

        private String code;

        private String value;
    }

    @Data
    public static class OptionResponse {

        private String id;

        private String name;

        private String group;

        private String code;

        private String value;
    }
}
