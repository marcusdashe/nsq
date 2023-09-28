/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author MarcusDashe & ChibuezeHarry
 *
 * */
@Data
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException  extends RuntimeException{

    private final String message;

    public BadRequestException(String message) {
        this.message = message;
    }
    
}
