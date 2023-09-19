/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.repos;

import org.cstemp.nsq.models.relational.Centre;
import org.cstemp.nsq.models.relational.ExternalVerifier;
import org.cstemp.nsq.models.relational.Programme;
import org.cstemp.nsq.models.relational.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 *
 * @author chibuezeharry & MarcusDashe
 */
public interface ExternalVerifierRepository extends JpaRepository<ExternalVerifier, Long> {

    Optional<ExternalVerifier> findByUser(User user);

    Optional<ExternalVerifier> findByUserAndProgramme(User user, Programme programme);

    Boolean existsByUserAndProgramme(User user, Programme programme);

    Page<ExternalVerifier> findAllByCentresContaining(Centre centre, Pageable pageable);
}
