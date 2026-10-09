package com.example.fitpulse.Entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class WorkoutRecord {
    private Long id;
    private Long userId;
    private String title;
    private String targetMuscle;
    private Integer durationMinutes;
    private LocalDate workoutDate;
    private LocalDateTime createTime;

}
