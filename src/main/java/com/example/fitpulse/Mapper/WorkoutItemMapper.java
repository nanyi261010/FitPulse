package com.example.fitpulse.Mapper;

import com.example.fitpulse.Entity.WorkoutItem;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface WorkoutItemMapper {
    @Insert(
            "INSERT INTO workout_item(record_id, exercise_name, sets, reps, weight_kg) " +
            "VALUES(#{recordId}, #{exerciseName}, #{sets}, #{reps}, #{weightKg})")
    int insert(WorkoutItem item);
    @Select("SELECT * FROM workout_item WHERE record_id = #{recordId}")
    List<WorkoutItem> selectByRecordId(Long recordId);
    @Select("<script>" +

            "SELECT * FROM workout_item WHERE record_id IN " +

            "<foreach collection='recordIds' item='id' open='(' separator=',' close=')'>" +

            "#{id}" +

            "</foreach>" +

            "</script>")

    List<WorkoutItem> selectByRecordIds(@Param("recordIds") List<Long> recordIds);


}
