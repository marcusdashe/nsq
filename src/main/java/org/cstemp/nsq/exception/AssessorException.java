/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.exception;

/**
 *
 * @author chibuezeharry & MarcusDashe
 */
public class AssessorException extends Exception {

    /**
     * Creates a new instance of <code>SiteworxException</code> without detail
     * message.
     */
    public AssessorException() {
    }

    /**
     * Constructs an instance of <code>SiteworxException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public AssessorException(String msg) {
        super(msg);
    }
}
