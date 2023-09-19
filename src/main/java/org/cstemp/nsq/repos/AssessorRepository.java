/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.repos;

import org.cstemp.nsq.models.relational.Assessor;
import org.cstemp.nsq.models.relational.Centre;
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
public interface AssessorRepository extends JpaRepository<Assessor, Long> {

    Optional<Assessor> findByUser(User user);

    Page<Assessor> findAllByCentresContaining(Centre centre, Pageable pageable);

    Boolean existsByUserAndProgramme(User user, Programme programme);

    Optional<Assessor> findByUserAndProgramme(User user, Programme programme);

    Optional<Assessor> findByUserAndCentresContaining(User user, Centre centre);

    public boolean existsByUserAndProgrammeAndCentresContaining(User user, Programme programme, Centre centre);

    public List<Assessor> findAllByUser(User user);

    Long countByCentresContaining(Centre centre);
}
