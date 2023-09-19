/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.repos;

import org.cstemp.nsq.models.relational.Programme;
import org.cstemp.nsq.models.relational.RoleRequest;
import org.cstemp.nsq.models.relational.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.cstemp.nsq.models.relational.Centre;

/**
 *
 * @author chibuezeharry & Marcusdashe
 */
public interface RoleRequestRepository extends JpaRepository<RoleRequest, Long> {

    Page<RoleRequest> findAllByCentre(Centre centre, Pageable pageable);

    Page<RoleRequest> findAllByRequestor(User user, Pageable pageable);

    Page<RoleRequest> findAllByProgramme(Programme centre, Pageable pageable);
}
