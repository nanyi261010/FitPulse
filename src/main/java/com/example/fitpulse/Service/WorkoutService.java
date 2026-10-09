package com.example.fitpulse.Service;

import com.example.fitpulse.Dto.WorkoutCreateDTO;
import com.example.fitpulse.Vo.WorkoutAnalyticsVO;

public interface WorkoutService {
    void createWorkout(WorkoutCreateDTO dto);
    WorkoutAnalyticsVO getAnalytics(Long userId);
}
