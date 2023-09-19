/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.repos;

import org.cstemp.nsq.models.relational.Profile;
import org.cstemp.nsq.models.relational.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

/**
 *
 * @author chibuezeharry & MarcusDashe
 */

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findByStateOfResidence(String state);

    Optional<Profile> findByUser(User user);
}
