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
@Data
public class BaseResponse<T> {

    private Boolean status = false;
    private String message = "";
    private T result;

    public BaseResponse(Boolean status, String message, T result) {
        this.status = status;
        this.result = result;

//        if message is null, then append status based "success" or "error";
        this.message = (message != null) ? message : this.message + (status ? " Saved Successfully" : " Could not save your data");
    }

    public Boolean getIsError() {
        return !status;
    }

    public Boolean getIsSuccess() {
        return status;
    }

}
