/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.repos;

import org.cstemp.nsq.models.relational.Centre;
import org.cstemp.nsq.models.relational.InternalVerifier;
import org.cstemp.nsq.models.relational.Programme;
import org.cstemp.nsq.models.relational.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author chibuezeharry & MarcusDashe
 */
public interface InternalVerifierRepository extends JpaRepository<InternalVerifier, Long> {

    Optional<InternalVerifier> findByUser(User user);

    Page<InternalVerifier> findAllByCentresContaining(Centre centre, Pageable pageable);

    Boolean existsByUserAndProgramme(User user, Programme programme);

    Optional<InternalVerifier> findByUserAndProgramme(User user, Programme programme);

    public boolean existsByUserAndProgrammeAndCentresContaining(User user, Programme programme, Centre centre);

    public List<InternalVerifier> findAllByUser(User user);

    Long countByCentresContaining(Centre centre);
}
