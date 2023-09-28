/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

/**
 *
 * @author MarcusDashe & ChibuezeHarry
 *
 * */

@Getter
@Setter
@ToString
@Slf4j
public class BaseException extends RuntimeException {
    
    private HttpStatus status;

    private Object errors;

    public BaseException(String message) {
        super(message);
        status = HttpStatus.BAD_REQUEST;
        log.info(message);
    }
    
    public BaseException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        log.info(message);
    }
    
}
