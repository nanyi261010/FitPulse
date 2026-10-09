package com.example.fitpulse.Dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WorkoutItemDTO {
    @NotBlank(message = "动作名称不能为空")

    private String exerciseName;



    @NotNull(message = "训练组数不能为空")

    @Min(value = 1, message = "训练组数至少要有 1 组")

    private Integer sets;



    @NotNull(message = "每组次数不能为空")

    @Min(value = 1, message = "每组次数至少为 1 次")

    private Integer reps;



    @NotNull(message = "训练重量不能为空")
    @DecimalMin(value = "0.0", message = "训练重量不能为负数")

    private BigDecimal weightKg;

}
