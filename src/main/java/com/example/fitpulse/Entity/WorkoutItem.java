package com.example.fitpulse.Entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

/**
 * 对应表 workout_item（动作明细表）
 */
@Data
public class WorkoutItem {
    private Long id;
    private Long recordId;
    private String exerciseName;
    private Integer sets;
    private Integer reps;
    private BigDecimal weightKg; // 重量用 BigDecimal 保证精度，避免浮点数计算失真


}