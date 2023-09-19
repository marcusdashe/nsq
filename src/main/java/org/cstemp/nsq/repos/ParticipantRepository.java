/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.cstemp.nsq.models.relational.Participant;

/**
 *
 * @author chibuezeharry & MarcusDashe
 */
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    Boolean existsByUser(Integer userId);

    List<Participant> findByUser(Integer userId);

    List<Participant> findByUserAndRole(Integer userId, String role);
}
