package com.example.fitpulse.Dto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class WorkoutCreateDTO {

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotBlank(message = "训练名称不能为空")
    @Size(max = 50, message = "训练名称最多50个字符")
    private String title;

    @NotBlank(message = "训练部位不能为空")
    private String targetMuscle;

    @NotNull(message = "训练时长不能为空")
    @Min(value = 5, message = "训练时长不能少于 5 分钟")
    @Max(value = 360, message = "训练时长不能超过 360 分钟")
    private Integer durationMinutes;

    @NotNull(message = "训练日期不能为空")
    @PastOrPresent(message = "训练日期不能是未来的日期")
    private LocalDate workoutDate;

    // 重点：嵌套对象校验
    @NotEmpty(message = "至少包含一个训练动作")
    @Valid // 关键：必须加 @Valid，否则不会对 items 内部每个 WorkoutItemDTO 进行属性校验！
    private List<WorkoutItemDTO> items;
}