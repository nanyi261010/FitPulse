package com.example.fitpulse.Vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class WorkoutAnalyticsVO {
    /**
     * 累计打卡总次数
     */
    private Integer totalWorkouts;

    /**
     * 累计训练总时长 (分钟)
     */
    private Integer totalDurationMinutes;

    /**
     * 累计总训练负荷/容量 (kg) = 各动作 组数 * 次数 * 重量 之和
     */
    private BigDecimal totalVolumeKg;//累计总训练负荷/容量 (kg)
    private Map<String, Integer> muscleDurationStats;// 动作时长统计
    private String maxVolumeWorkoutTitle;// 最大负荷训练标题
    private BigDecimal maxVolumeWorkoutAmount;// 最大负荷训练金额
}
