package com.example.fitpulse.Controller;

import com.example.fitpulse.Common.Result;
import com.example.fitpulse.Dto.WorkoutCreateDTO;
import com.example.fitpulse.Entity.WorkoutRecord;
import com.example.fitpulse.Service.WorkoutService;

import com.example.fitpulse.Vo.WorkoutAnalyticsVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutService workoutService;

    @PostMapping//这个注释表示这是一个创建 workout 的请求
    public Result<Void> createWorkout(@Valid @RequestBody WorkoutCreateDTO dto) {
        workoutService.createWorkout(dto);
        return Result.success();
    }
    @GetMapping("/analytics")
    public Result<WorkoutAnalyticsVO> getAnalytics(@RequestParam Long userId) {
        WorkoutAnalyticsVO analytics = workoutService.getAnalytics(userId);
        return Result.success(analytics);
    }
    @DeleteMapping("/{recordId}")
    public Result<Void> deleteRecord(@PathVariable Long recordId){
        workoutService.deleteRecord(recordId);
        return Result.success();
    }
    @GetMapping("/list")
    public Result<List<WorkoutRecord>> listRecords(@RequestParam Long userId) {
        List<WorkoutRecord> list = workoutService.listRecords(userId);
        return Result.success(list);
    }
}