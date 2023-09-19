/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.cstemp.nsq.models.relational.Option;

/**
 *
 * @author chibuezeharry
 */
public interface OptionRepository extends JpaRepository<Option, Long> {

    Optional<Boolean> existsByName(String name);

    Optional<Option> findByName(String name);

    List<Option> findAllByName(String name);

    List<Option> findByNameAndGroupName(String name, String group);

    List<Option> findByNameIn(Iterable<String> optionNames);

    Optional<Boolean> existsByNameAndGroupNameAndOptionValue(String name, String group, String value);

    Optional<Boolean> existsByNameAndOptionValue(String name, String value);
}
