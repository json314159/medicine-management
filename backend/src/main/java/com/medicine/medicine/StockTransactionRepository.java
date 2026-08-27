package com.medicine.medicine;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StockTransactionRepository extends JpaRepository<StockTransaction, Long> {
    List<StockTransaction> findTop20ByOrderByCreatedAtDesc();
}
