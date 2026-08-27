package com.medicine.medicine;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record MedicineRequest(
        @NotBlank(message = "药品编码不能为空") @Size(max = 64, message = "药品编码不能超过64个字符") String code,
        @NotBlank(message = "药品名称不能为空") @Size(max = 128, message = "药品名称不能超过128个字符") String name,
        @Size(max = 128, message = "通用名不能超过128个字符") String genericName,
        @Size(max = 128, message = "规格不能超过128个字符") String specification,
        @Size(max = 128, message = "生产厂家不能超过128个字符") String manufacturer,
        @Size(max = 64, message = "分类不能超过64个字符") String category,
        @NotBlank(message = "单位不能为空") @Size(max = 32, message = "单位不能超过32个字符") String unit,
        @NotNull(message = "采购价不能为空") @DecimalMin(value = "0.00", message = "采购价不能小于0") BigDecimal purchasePrice,
        @NotNull(message = "售价不能为空") @DecimalMin(value = "0.00", message = "售价不能小于0") BigDecimal salePrice,
        @NotNull(message = "初始库存不能为空") @Min(value = 0, message = "初始库存不能小于0") Integer stockQuantity,
        @NotNull(message = "库存预警值不能为空") @Min(value = 0, message = "库存预警值不能小于0") Integer stockThreshold,
        LocalDate expiryDate,
        @Size(max = 64, message = "批号不能超过64个字符") String batchNo,
        Boolean enabled
) { }
