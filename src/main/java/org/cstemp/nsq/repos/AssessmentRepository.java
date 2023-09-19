/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.repos;


import org.cstemp.nsq.models.relational.Assessment;
import org.cstemp.nsq.models.relational.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author chibuezeharry and MarcusDashe
 */
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {

    public Page<Assessment> findByAssessorAndTitleContaining(User user, String s, Pageable pageable);

}
