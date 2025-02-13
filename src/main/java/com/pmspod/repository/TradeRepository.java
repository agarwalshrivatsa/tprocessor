package com.pmspod.repository;

import com.pmspod.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {

    Trade findByExternalOrderId(String externalOrderId);
    
}
