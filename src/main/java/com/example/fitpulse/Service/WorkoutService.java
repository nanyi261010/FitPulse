package com.example.fitpulse.Service;

import com.example.fitpulse.Dto.WorkoutCreateDTO;
import com.example.fitpulse.Entity.WorkoutRecord;
import com.example.fitpulse.Vo.WorkoutAnalyticsVO;

import java.util.List;

public interface WorkoutService {
    void createWorkout(WorkoutCreateDTO dto);
    WorkoutAnalyticsVO getAnalytics(Long userId);

    void deleteRecord(Long recordId);
    List<WorkoutRecord> listRecords(Long userId);
}
