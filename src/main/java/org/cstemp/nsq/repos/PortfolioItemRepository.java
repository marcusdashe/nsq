/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.repos;


import org.cstemp.nsq.models.relational.Portfolio;
import org.cstemp.nsq.models.relational.PortfolioItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author chibuezeharry
 */
public interface PortfolioItemRepository extends JpaRepository<PortfolioItem, Long> {

    Page<PortfolioItem> findByPortfolioAndTitleContains(Portfolio portfolio, String s, Pageable pageable);

}
