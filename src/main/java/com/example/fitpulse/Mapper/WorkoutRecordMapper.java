package com.example.fitpulse.Mapper;

import com.example.fitpulse.Entity.WorkoutRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WorkoutRecordMapper {
    @Insert("INSERT INTO workout_record(user_id, title, target_muscle, duration_minutes, workout_date) " +
            "VALUES(#{userId}, #{title}, #{targetMuscle}, #{durationMinutes}, #{workoutDate})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insert(WorkoutRecord record);
    @Select("SELECT * FROM workout_record WHERE user_id=#{userId} ORDER BY workout_date DESC")
    List<WorkoutRecord> selectByUserId(Long userId);
    //根据id删除
    @Delete("DELETE FROM workout_record WHERE id=#{id}")
    int deleteRecord(Long id);
}
