package com.rcyouth.socialschedule.service;

import com.rcyouth.socialschedule.config.Config;
import com.rcyouth.socialschedule.schedule_manager.Content;
import com.rcyouth.socialschedule.schedule_manager.ContentScheduler;
import com.rcyouth.socialschedule.schedule_manager.Schedule;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

@Service
public class ScheduleService {

    private final ConfigService configService;
    private final Map<String, Schedule> inMemorySchedules = new HashMap<>();

    public ScheduleService(ConfigService configService) {
        this.configService = configService;
    }

    public Schedule generateSchedule(String scheduleName, int year, int startMonth, int endMonth) {
        Config config = configService.getGlobalConfig();
        Schedule schedule = new Schedule(); // Create a new empty schedule for generation
        ContentScheduler contentScheduler = new ContentScheduler(schedule, config.getPeople(), config.getWeeklySchedules(), 0);

        YearMonth startYearMonth = YearMonth.of(year, startMonth);
        YearMonth endYearMonth = YearMonth.of(year, endMonth);

        if (startYearMonth.isAfter(endYearMonth)) {
            throw new IllegalArgumentException("Start month must be before or equal to end month");
        }

        for (YearMonth currentMonth = startYearMonth; !currentMonth.isAfter(endYearMonth); currentMonth = currentMonth.plusMonths(1)) {
            contentScheduler.generateFullMonthSchedule(currentMonth);
        }
        inMemorySchedules.put(scheduleName, schedule);
        return schedule;
    }

    public Schedule saveSchedule(String scheduleName, Schedule scheduleToSave) {
        // Assuming scheduleName will be used to determine the file path, e.g., scheduleName.json
        // For now, we'll just use a fixed name for simplicity, but this can be extended.
        Schedule schedule = new Schedule(scheduleName + ".json");
        schedule.setSchedule(scheduleToSave.getSchedule());
        schedule.saveToFile();
        inMemorySchedules.remove(scheduleName);
        return schedule;
    }

    public Schedule getScheduleByMonth(String scheduleName, int year, int month) {
        Schedule schedule = new Schedule(scheduleName + ".json");
        Map<LocalDate, Content> monthSchedule = new HashMap<>();
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        int daysInMonth = yearMonth.lengthOfMonth();

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = firstDayOfMonth.withDayOfMonth(day);
            if (schedule.getSchedule().containsKey(date)) {
                monthSchedule.put(date, schedule.getSchedule().get(date));
            }
        }
        Schedule resultSchedule = new Schedule();
        resultSchedule.setSchedule(monthSchedule);
        return resultSchedule;
    }

    public Schedule getScheduleByYear(String scheduleName, int year) {
        Schedule schedule = new Schedule(scheduleName + ".json");
        Map<LocalDate, Content> yearSchedule = new HashMap<>();
        for (Map.Entry<LocalDate, Content> entry : schedule.getSchedule().entrySet()) {
            if (entry.getKey().getYear() == year) {
                yearSchedule.put(entry.getKey(), entry.getValue());
            }
        }
        Schedule resultSchedule = new Schedule();
        resultSchedule.setSchedule(yearSchedule);
        return resultSchedule;
    }

    public Map<String, Schedule> getInMemorySchedules() {
        return inMemorySchedules;
    }
}
