package com.example.fitpulse.Mapper;

import com.example.fitpulse.Entity.UserPersonalRecord;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserPersonalRecordMapper {
    // 1. 查询该用户某动作的历史最大记录
    @Select("SELECT * FROM user_personal_record WHERE user_id = #{userId} AND exercise_name = #{exerciseName} LIMIT 1")
    UserPersonalRecord selectByUserAndExercise(@Param("userId") Long userId, @Param("exerciseName") String exerciseName);

    // 2. 首次突破：插入新 PR
    @Insert("INSERT INTO user_personal_record(user_id, exercise_name, max_weight_kg) " +
            "VALUES(#{userId}, #{exerciseName}, #{maxWeightKg})")
    int insert(UserPersonalRecord record);

    // 3. 打破记录：更新最大重量
    @Update("UPDATE user_personal_record SET max_weight_kg = #{maxWeightKg} WHERE id = #{id}")
    int updateWeight(@Param("id") Long id, @Param("maxWeightKg") java.math.BigDecimal maxWeightKg);
}
