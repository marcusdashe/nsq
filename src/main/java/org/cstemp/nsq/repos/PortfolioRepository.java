/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.repos;

import org.cstemp.nsq.models.relational.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 *
 * @author chibuezeharry & MarcusDashe
 */
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    Page<Portfolio> findAllByAssessor(Assessor assessor, Pageable pageable);

    List<Portfolio> findAllByUser(User user);

    Page<Portfolio> findBySearchStringContainsAndProgrammeAndCentreIn(String searchString, Programme programme, List<Centre> centres, Pageable pageable);

    Page<Portfolio> findBySearchStringContainsAndProgrammeAndCentre(String searchString, Programme programme, Centre centre, Pageable pageable);

    Page<Portfolio> findBySearchStringContainsAndProgramme(String searchString, Programme programme, Pageable pageable);

    Page<Portfolio> findAllByProgramme(Programme programme, Pageable pageable);

    Long countByCentre(Centre centre);

}
