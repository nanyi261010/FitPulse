package com.example.fitpulse.Entity;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserPersonalRecord {
    private Long id;
    private Long userId;
    private String exerciseName;
    private BigDecimal maxWeightKg;
    private LocalDateTime updateTime;
}