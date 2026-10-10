package com.example.fitpulse.Service;

import com.example.fitpulse.Dto.WorkoutCreateDTO;
import com.example.fitpulse.Dto.WorkoutItemDTO;
import com.example.fitpulse.Entity.UserPersonalRecord;
import com.example.fitpulse.Entity.WorkoutItem;
import com.example.fitpulse.Entity.WorkoutRecord;
import com.example.fitpulse.Mapper.UserPersonalRecordMapper;
import com.example.fitpulse.Mapper.WorkoutItemMapper;
import com.example.fitpulse.Mapper.WorkoutRecordMapper;
import com.example.fitpulse.Vo.WorkoutAnalyticsVO;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutServiceImpl implements WorkoutService {
    private final WorkoutRecordMapper workoutRecordMapper;
    private final WorkoutItemMapper workoutItemMapper;
    private final UserPersonalRecordMapper userPersonalRecordMapper;
    @Override
    @Transactional(rollbackFor = Exception.class) // 事务保护：任意一步报错全回滚
    public void createWorkout(WorkoutCreateDTO dto) {
        // 1. DTO 转 Entity 并插入主表
        WorkoutRecord record = new WorkoutRecord();
        record.setUserId(dto.getUserId());
        record.setTitle(dto.getTitle());
        record.setTargetMuscle(dto.getTargetMuscle());
        record.setDurationMinutes(dto.getDurationMinutes());
        record.setWorkoutDate(dto.getWorkoutDate());

        workoutRecordMapper.insert(record); // 执行后，record.getId() 已被自动赋值
        for (WorkoutItemDTO itemDTO : dto.getItems()) {
            WorkoutItem item = new WorkoutItem();
            item.setRecordId(record.getId()); // 绑定刚刚生成的主表主键
            item.setExerciseName(itemDTO.getExerciseName());
            item.setSets(itemDTO.getSets());
            item.setReps(itemDTO.getReps());
            item.setWeightKg(itemDTO.getWeightKg());

            workoutItemMapper.insert(item);
            updatePersonalRecordIfBroken(dto.getUserId(),itemDTO);
}}
    @Override
    public WorkoutAnalyticsVO getAnalytics(Long userId) {
        // 1. 查询该用户所有的打卡主记录
        List<WorkoutRecord> records = workoutRecordMapper.selectByUserId(userId);

        WorkoutAnalyticsVO vo = new WorkoutAnalyticsVO();
        if (records == null || records.isEmpty()) {
            // 如果该用户一次卡都没打过，直接返回全空看板
            vo.setTotalWorkouts(0);
            vo.setTotalDurationMinutes(0);
            vo.setTotalVolumeKg(BigDecimal.ZERO);
            vo.setMuscleDurationStats(Collections.emptyMap());
            vo.setMaxVolumeWorkoutTitle("无记录");
            vo.setMaxVolumeWorkoutAmount(BigDecimal.ZERO);
            return vo;
        }

        // 2. 基础指标统计
        vo.setTotalWorkouts(records.size());

        // 【Stream 核心 1】：求和汇总所有训练时长
        int totalMinutes = records.stream()
                .mapToInt(WorkoutRecord::getDurationMinutes)
                .sum();
        vo.setTotalDurationMinutes(totalMinutes);

        // 【Stream 核心 2】：按肌肉部位分组，统计各部位训练总时长 (groupingBy + summingInt)
        Map<String, Integer> muscleStats = records.stream()
                .collect(Collectors.groupingBy(
                        WorkoutRecord::getTargetMuscle,
                        Collectors.summingInt(WorkoutRecord::getDurationMinutes)
                ));
        vo.setMuscleDurationStats(muscleStats);

        // 3. 提取所有打卡记录的主键 ID，批量查出动作明细
        List<Long> recordIds = records.stream()
                .map(WorkoutRecord::getId)
                .toList();
        List<WorkoutItem> items = workoutItemMapper.selectByRecordIds(recordIds);

        // 【Stream 核心 3】：计算全生命周期的总训练负荷（总容量 = 组数 * 次数 * 重量）
        BigDecimal totalVolume = items.stream()
                .map(item -> item.getWeightKg()
                        .multiply(BigDecimal.valueOf(item.getSets()))
                        .multiply(BigDecimal.valueOf(item.getReps())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        vo.setTotalVolumeKg(totalVolume);

        // 【Stream 核心 4】：按训练记录 ID 分组计算每次打卡的总容量，并找出最大容量的一天
        // 4.1 先按 recordId 分组计算出每次训练的容量
        Map<Long, BigDecimal> volumePerRecord = items.stream()
                .collect(Collectors.groupingBy(
                        WorkoutItem::getRecordId,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                item -> item.getWeightKg()
                                        .multiply(BigDecimal.valueOf(item.getSets()))
                                        .multiply(BigDecimal.valueOf(item.getReps())),
                                BigDecimal::add
                        )
                ));

        // 4.2 找出最大容量对应的 recordId
        volumePerRecord.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .ifPresent(maxEntry -> {
                    Long topRecordId = maxEntry.getKey();
                    // 在主记录列表里找出它的标题
                    records.stream()
                            .filter(r -> r.getId().equals(topRecordId))
                            .findFirst()
                            .ifPresent(topRecord -> {
                                vo.setMaxVolumeWorkoutTitle(topRecord.getTitle());
                                vo.setMaxVolumeWorkoutAmount(maxEntry.getValue());
                            });
                });

        return vo;
    }
    @Override
    @Transactional
    public void deleteRecord(Long recordId) {
        workoutItemMapper.deleteByRecordId(recordId);
        workoutRecordMapper.deleteRecord(recordId);
    }
    @Override
    public List<WorkoutRecord> listRecords(Long userId) {
        return workoutRecordMapper.selectByUserId(userId);
    }

    private void updatePersonalRecordIfBroken(Long userId,WorkoutItemDTO itemDTO) {
        UserPersonalRecord currentPr=userPersonalRecordMapper.selectByUserAndExercise(userId,itemDTO.getExerciseName());
        if(currentPr==null){
            UserPersonalRecord newPr= new UserPersonalRecord();
            newPr.setUserId(userId);
            newPr.setExerciseName(itemDTO.getExerciseName());
            newPr.setMaxWeightKg(itemDTO.getWeightKg());
            userPersonalRecordMapper.insert(newPr);
        }else if (itemDTO.getWeightKg().compareTo(currentPr.getMaxWeightKg()) > 0) {
            // 场景 B：本次重量突破历史记录（BigDecimal 比较必须用 compareTo > 0）
            userPersonalRecordMapper.updateWeight(currentPr.getId(), itemDTO.getWeightKg());
        }
        // 场景 C：未突破历史重量，直接忽略

    }
}
