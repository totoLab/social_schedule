package com.rcyouth.socialschedule.service;

import com.rcyouth.socialschedule.config.Config;
import com.rcyouth.socialschedule.schedule_manager.Schedule;
import com.rcyouth.socialschedule.visualization.CalendarImageGenerator;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Month;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

@Service
public class ImageService {

    private final ConfigService configService;
    private final ScheduleService scheduleService;

    public ImageService(ConfigService configService, ScheduleService scheduleService) {
        this.configService = configService;
        this.scheduleService = scheduleService;
    }

    public String generateCalendarImage(String scheduleName, int year, int month) throws IOException {
        Config config = configService.getGlobalConfig();
        Schedule schedule = scheduleService.getScheduleByMonth(scheduleName, year, month);

        CalendarImageGenerator generator = new CalendarImageGenerator(config);
        YearMonth yearMonth = YearMonth.of(year, month);
        String filename = String.format("schedule_images/%d.%s_%d_calendar.png",
                yearMonth.getMonthValue(),
                yearMonth.getMonth().toString().toLowerCase(),
                yearMonth.getYear());

        generator.generateCalendarImage(
                schedule.getSchedule(),
                yearMonth.getYear(),
                yearMonth.getMonth(),
                filename
        );
        return filename;
    }
}
