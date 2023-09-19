/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.repos;

import org.cstemp.nsq.models.relational.PortfolioItem;
import org.cstemp.nsq.models.relational.Remark;
import org.cstemp.nsq.models.relational.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author chibuezeharry & MarcusDashe
 */
public interface RemarkRepository extends JpaRepository<Remark, Long> {

    List<Remark> findAllByPortfolioItem(PortfolioItem portfolioItem);

    Optional<Remark> findByPortfolioItemAndUser(PortfolioItem portfolioItem, User user);
}
