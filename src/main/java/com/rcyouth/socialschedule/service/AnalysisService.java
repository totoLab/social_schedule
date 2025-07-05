package com.rcyouth.socialschedule.service;

import com.rcyouth.socialschedule.schedule_manager.Content;
import com.rcyouth.socialschedule.schedule_manager.Schedule;
import com.rcyouth.socialschedule.schedule_manager.Type;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnalysisService {

    private final ScheduleService scheduleService;

    public AnalysisService(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    public Map<String, Object> analyzePersonContent(String scheduleName, String personName) {
        Schedule schedule = new Schedule(scheduleName + ".json"); // Load the full schedule

        Map<Type, Integer> typeDistribution = new HashMap<>();
        int totalWeight = 0;

        for (Map.Entry<LocalDate, Content> entry : schedule.getSchedule().entrySet()) {
            Content content = entry.getValue();
            if (content.getMaker() != null && content.getMaker().equalsIgnoreCase(personName)) {
                typeDistribution.merge(content.getType(), 1, Integer::sum);
                totalWeight += Content.calculateWeight(content.getType());
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("personName", personName);
        result.put("typeDistribution", typeDistribution);
        result.put("totalWeight", totalWeight);
        return result;
    }

    public Map<String, Object> analyzeMonth(String scheduleName, int year, int month) {
        Schedule schedule = scheduleService.getScheduleByMonth(scheduleName, year, month);
        return performGeneralAnalysis(schedule.getSchedule());
    }

    public Map<String, Object> analyzeYear(String scheduleName, int year) {
        Schedule schedule = scheduleService.getScheduleByYear(scheduleName, year);
        return performGeneralAnalysis(schedule.getSchedule());
    }

    private Map<String, Object> performGeneralAnalysis(Map<LocalDate, Content> scheduleData) {
        Map<String, Object> analysisResult = new HashMap<>();
        Map<String, Integer> makerContentCount = new HashMap<>();
        Map<Type, Integer> typeContentCount = new HashMap<>();

        for (Map.Entry<LocalDate, Content> entry : scheduleData.entrySet()) {
            Content content = entry.getValue();
            if (content.getMaker() != null) {
                makerContentCount.merge(content.getMaker(), 1, Integer::sum);
            }
            typeContentCount.merge(content.getType(), 1, Integer::sum);
        }

        analysisResult.put("makerContentCount", makerContentCount);
        analysisResult.put("typeContentCount", typeContentCount);
        analysisResult.put("totalContentEntries", scheduleData.size());

        return analysisResult;
    }
}
