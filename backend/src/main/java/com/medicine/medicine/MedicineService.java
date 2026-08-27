package com.medicine.medicine;

import com.medicine.common.BusinessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class MedicineService {
    private final MedicineRepository medicineRepository;
    private final StockTransactionRepository transactionRepository;

    public MedicineService(MedicineRepository medicineRepository, StockTransactionRepository transactionRepository) {
        this.medicineRepository = medicineRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional(readOnly = true)
    public List<Medicine> list(String keyword, Boolean lowStock) {
        List<Medicine> medicines = (keyword == null || keyword.isBlank())
                ? medicineRepository.findAll()
                : medicineRepository.findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(keyword.trim(), keyword.trim());
        return medicines.stream()
                .filter(item -> !Boolean.TRUE.equals(lowStock) || item.getStockQuantity() <= item.getStockThreshold())
                .sorted(Comparator.comparing(Medicine::getUpdatedAt).reversed())
                .toList();
    }

    @Transactional(readOnly = true)
    public Medicine get(Long id) { return find(id); }

    @Transactional
    public Medicine create(MedicineRequest request) {
        if (medicineRepository.existsByCode(request.code().trim())) throw new BusinessException("药品编码已存在");
        Medicine medicine = new Medicine();
        copy(request, medicine);
        try {
            return medicineRepository.save(medicine);
        } catch (DataIntegrityViolationException exception) {
            throw new BusinessException("药品编码已存在");
        }
    }

    @Transactional
    public Medicine update(Long id, MedicineRequest request) {
        Medicine medicine = find(id);
        medicineRepository.findByCode(request.code().trim())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> { throw new BusinessException("药品编码已存在"); });
        copy(request, medicine);
        return medicineRepository.save(medicine);
    }

    @Transactional
    public void delete(Long id) { medicineRepository.delete(find(id)); }

    @Transactional
    public Medicine adjustStock(Long id, StockAdjustmentRequest request) {
        Medicine medicine = find(id);
        int before = medicine.getStockQuantity();
        int after = switch (request.type()) {
            case IN -> before + request.quantity();
            case OUT -> before - request.quantity();
            case ADJUST -> request.quantity();
        };
        if (after < 0) throw new BusinessException("出库数量不能超过当前库存");
        medicine.setStockQuantity(after);
        StockTransaction transaction = new StockTransaction();
        transaction.setMedicine(medicine);
        transaction.setType(request.type());
        transaction.setQuantity(request.quantity());
        transaction.setBeforeQuantity(before);
        transaction.setAfterQuantity(after);
        transaction.setRemark(request.remark());
        transactionRepository.save(transaction);
        return medicineRepository.save(medicine);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> dashboard() {
        List<Medicine> all = medicineRepository.findAll();
        LocalDate today = LocalDate.now();
        LocalDate deadline = today.plusDays(30);
        List<Medicine> lowStocks = all.stream().filter(item -> item.getStockQuantity() <= item.getStockThreshold()).toList();
        List<Medicine> expirySoon = all.stream().filter(item -> item.getExpiryDate() != null && !item.getExpiryDate().isBefore(today) && !item.getExpiryDate().isAfter(deadline)).toList();
        BigDecimal inventoryValue = all.stream().map(item -> item.getPurchasePrice().multiply(BigDecimal.valueOf(item.getStockQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add);
        int totalStock = all.stream().mapToInt(Medicine::getStockQuantity).sum();
        return Map.of(
                "medicineCount", all.size(), "totalStock", totalStock, "inventoryValue", inventoryValue,
                "lowStockCount", lowStocks.size(), "expirySoonCount", expirySoon.size(),
                "lowStockMedicines", lowStocks, "expirySoonMedicines", expirySoon,
                "recentTransactions", transactionRepository.findTop20ByOrderByCreatedAtDesc().stream().map(this::toTransactionView).toList());
    }

    private Map<String, Object> toTransactionView(StockTransaction transaction) {
        return Map.of("id", transaction.getId(), "medicineName", transaction.getMedicine().getName(),
                "medicineCode", transaction.getMedicine().getCode(), "type", transaction.getType(),
                "quantity", transaction.getQuantity(), "beforeQuantity", transaction.getBeforeQuantity(),
                "afterQuantity", transaction.getAfterQuantity(), "remark", transaction.getRemark() == null ? "" : transaction.getRemark(), "createdAt", transaction.getCreatedAt());
    }
    private Medicine find(Long id) { return medicineRepository.findById(id).orElseThrow(() -> new BusinessException("药品不存在")); }
    private void copy(MedicineRequest request, Medicine medicine) {
        medicine.setCode(request.code().trim()); medicine.setName(request.name().trim()); medicine.setGenericName(request.genericName());
        medicine.setSpecification(request.specification()); medicine.setManufacturer(request.manufacturer()); medicine.setCategory(request.category());
        medicine.setUnit(request.unit().trim()); medicine.setPurchasePrice(request.purchasePrice()); medicine.setSalePrice(request.salePrice());
        medicine.setStockQuantity(request.stockQuantity()); medicine.setStockThreshold(request.stockThreshold()); medicine.setExpiryDate(request.expiryDate());
        medicine.setBatchNo(request.batchNo()); medicine.setEnabled(request.enabled() == null || request.enabled());
    }
}
