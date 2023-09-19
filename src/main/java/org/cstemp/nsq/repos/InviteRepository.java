/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.repos;

import org.cstemp.nsq.models.relational.Centre;
import org.cstemp.nsq.models.relational.Invite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author chibuezeharry & MarcusDashe
 */
public interface InviteRepository extends JpaRepository<Invite, Long> {

    Optional<Invite> findAllByEmail(String email);

    List<Invite> findAllByCentre(Centre centre);
}
