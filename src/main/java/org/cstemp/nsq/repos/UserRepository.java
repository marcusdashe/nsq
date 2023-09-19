/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.repos;


import org.cstemp.nsq.models.relational.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 *
 * @author chibuezeharry & Marcusdashe
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    Optional<User> findByEmailOrPhone(String email, String phone);
//    Optional<User> findByEmailOrPhoneAndStatus(String email, String phone, Boolean status);

    Boolean existsByPhone(String phone);

    Boolean existsByEmail(String email);
//    List<User> findByRolesContaining(Role role);

    Page<User> findByFirstnameContainingOrLastnameContainingOrEmailContaining(String s, String s0, String s1, Pageable pageable);
//    Page<User> findByFirstNameContainingOrLastNameContainingOrEmailContainingAndRolesContaining(String s, String s0, String s1, Role role, Pageable pageable);

    Boolean existsByEmailOrPhone(String email, String phone);

}
