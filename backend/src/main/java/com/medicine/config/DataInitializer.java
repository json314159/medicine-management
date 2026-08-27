package com.medicine.config;

import com.medicine.medicine.Medicine;
import com.medicine.medicine.MedicineRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initSampleData(MedicineRepository repository) {
        return args -> {
            if (repository.count() > 0) return;
            repository.save(create("AMOX-001", "阿莫西林胶囊", "阿莫西林", "0.25g×24粒", "华北制药", "抗感染药", "盒", "8.50", "15.80", 16, 20, LocalDate.now().plusDays(180), "AM202608"));
            repository.save(create("IBUP-001", "布洛芬缓释胶囊", "布洛芬", "0.3g×20粒", "中美史克", "解热镇痛药", "盒", "12.00", "22.50", 46, 15, LocalDate.now().plusDays(22), "IB202607"));
            repository.save(create("VITC-001", "维生素C片", "维生素C", "100mg×100片", "东北制药", "维生素", "瓶", "6.80", "12.00", 90, 30, LocalDate.now().plusDays(365), "VC202606"));
        };
    }
    private Medicine create(String code, String name, String genericName, String specification, String manufacturer, String category, String unit, String purchasePrice, String salePrice, int stock, int threshold, LocalDate expiry, String batch) {
        Medicine medicine = new Medicine();
        medicine.setCode(code); medicine.setName(name); medicine.setGenericName(genericName); medicine.setSpecification(specification); medicine.setManufacturer(manufacturer); medicine.setCategory(category); medicine.setUnit(unit);
        medicine.setPurchasePrice(new BigDecimal(purchasePrice)); medicine.setSalePrice(new BigDecimal(salePrice)); medicine.setStockQuantity(stock); medicine.setStockThreshold(threshold); medicine.setExpiryDate(expiry); medicine.setBatchNo(batch); medicine.setEnabled(true);
        return medicine;
    }
}
