/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.repos;

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
public interface CentreRepository extends JpaRepository<Centre, Long> {

    public Optional<Centre> findByAdmin(User user);

    public Page<Centre> findByNameContainingOrAddressContaining(String s, String s0, Pageable pageable);

    public List<Centre> findAllByProgrammesContaining(Programme programme);
}
