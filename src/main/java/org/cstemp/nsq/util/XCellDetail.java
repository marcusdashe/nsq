/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.util;

import java.lang.annotation.*;

/**
 *
 * @author chibuezeharry
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface XCellDetail {
    String value() default "";

    String label() default "";

    boolean indexed() default false;
}
