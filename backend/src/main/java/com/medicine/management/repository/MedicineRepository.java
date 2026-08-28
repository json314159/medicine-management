package com.medicine.management.repository;
import com.medicine.management.domain.entity.Medicine;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    boolean existsByCode(String code);
    Optional<Medicine> findByCode(String code);
    List<Medicine> findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(String name, String code);
}
