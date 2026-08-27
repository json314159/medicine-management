package com.medicine.medicine;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StockAdjustmentRequest(
        @NotNull(message = "操作类型不能为空") TransactionType type,
        @NotNull(message = "数量不能为空") @Min(value = 1, message = "数量必须大于0") Integer quantity,
        @Size(max = 255, message = "备注不能超过255个字符") String remark
) { }
