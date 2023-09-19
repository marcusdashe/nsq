/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.repos;


import org.cstemp.nsq.models.relational.AssessmentUnit;
import org.cstemp.nsq.models.relational.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *
 * @author chibuezeharry and MarcusDashe
 */
public interface AssessmentUnitRepository extends JpaRepository<AssessmentUnit, Long> {

    List<AssessmentUnit> findAllByCourse(Course courses);
}
