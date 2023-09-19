/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.repos;

import org.cstemp.nsq.models.relational.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author chibuezeharry
 */
public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
