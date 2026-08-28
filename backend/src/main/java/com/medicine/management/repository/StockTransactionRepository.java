package com.medicine.management.repository;
import com.medicine.management.domain.entity.StockTransaction;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StockTransactionRepository extends JpaRepository<StockTransaction, Long> {
    List<StockTransaction> findTop20ByOrderByCreatedAtDesc();
}
