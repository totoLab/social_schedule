package com.rcyouth.socialschedule.service;

import com.rcyouth.socialschedule.model.Schedule;
import com.rcyouth.socialschedule.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalysisService {

    private final ScheduleRepository scheduleRepository;

    public AnalysisService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public Map<String, Object> analyzePersonContent(long personId, LocalDate startDate, LocalDate endDate) {
        List<Schedule> schedules = scheduleRepository.findByPersonIdAndScheduledDateBetween(personId, startDate, endDate);

        Map<String, Integer> typeDistribution = new HashMap<>();
        for (Schedule schedule : schedules) {
            typeDistribution.merge(schedule.getContentType().getName(), 1, Integer::sum);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("personId", personId);
        result.put("typeDistribution", typeDistribution);
        return result;
    }

    public Map<String, Object> analyzePersonYearlyContent(Long personId, int year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        return analyzePersonContent(personId, startDate, endDate);
    }

    public Map<String, Object> analyzePersonMonthlyContent(Long personId, int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        return analyzePersonContent(personId, startDate, endDate);
    }

    public Map<String, Object> analyzeMonth(int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        List<Schedule> schedules = scheduleRepository.findByScheduledDateBetween(startDate, endDate);
        return performGeneralAnalysis(schedules);
    }

    public Map<String, Object> analyzeYear(int year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = startDate.plusYears(1).minusDays(1);
        List<Schedule> schedules = scheduleRepository.findByScheduledDateBetween(startDate, endDate);
        return performGeneralAnalysis(schedules);
    }

    private Map<String, Object> performGeneralAnalysis(List<Schedule> schedules) {
        Map<String, Object> analysisResult = new HashMap<>();
        Map<String, Integer> makerContentCount = new HashMap<>();
        Map<String, Integer> typeContentCount = new HashMap<>();

        for (Schedule schedule : schedules) {
            makerContentCount.merge(schedule.getPerson().getName(), 1, Integer::sum);
            typeContentCount.merge(schedule.getContentType().getName(), 1, Integer::sum);
        }

        analysisResult.put("makerContentCount", makerContentCount);
        analysisResult.put("typeContentCount", typeContentCount);
        analysisResult.put("totalContentEntries", schedules.size());

        return analysisResult;
    }
}