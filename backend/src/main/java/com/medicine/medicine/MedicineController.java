package com.medicine.medicine;

import com.medicine.common.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "${app.cors-origin:http://localhost:5173}")
public class MedicineController {
    private final MedicineService medicineService;
    public MedicineController(MedicineService medicineService) { this.medicineService = medicineService; }
    @GetMapping("/medicines")
    public ApiResponse<List<Medicine>> list(@RequestParam(required = false) String keyword, @RequestParam(required = false) Boolean lowStock) { return ApiResponse.ok(medicineService.list(keyword, lowStock)); }
    @GetMapping("/medicines/{id}")
    public ApiResponse<Medicine> get(@PathVariable Long id) { return ApiResponse.ok(medicineService.get(id)); }
    @PostMapping("/medicines")
    public ApiResponse<Medicine> create(@Valid @RequestBody MedicineRequest request) { return ApiResponse.ok("药品已新增", medicineService.create(request)); }
    @PutMapping("/medicines/{id}")
    public ApiResponse<Medicine> update(@PathVariable Long id, @Valid @RequestBody MedicineRequest request) { return ApiResponse.ok("药品已更新", medicineService.update(id, request)); }
    @DeleteMapping("/medicines/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) { medicineService.delete(id); return ApiResponse.ok("药品已删除", null); }
    @PostMapping("/medicines/{id}/stock")
    public ApiResponse<Medicine> adjust(@PathVariable Long id, @Valid @RequestBody StockAdjustmentRequest request) { return ApiResponse.ok("库存已更新", medicineService.adjustStock(id, request)); }
    @GetMapping("/dashboard")
    public ApiResponse<Map<String, Object>> dashboard() { return ApiResponse.ok(medicineService.dashboard()); }
}
