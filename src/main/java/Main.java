import config.Config;
import schedule_manager.ContentScheduler;
import schedule_manager.Schedule;
import visualization.CalendarImageGenerator;

import java.io.IOException;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        YearMonth startMonth = YearMonth.of(2025, 2);
        YearMonth endMonth = YearMonth.of(2025, 6);
        Schedule schedule = new Schedule("schedule_rcy.json");
        Config config = new Config("config_rcy.json");
        generateScheduleAndCalendar(config, schedule, startMonth, endMonth);
    }

    static void wholeYear(Config config, Schedule schedule, int year) throws IOException {
        YearMonth startMonth = YearMonth.of(year, 1);
        YearMonth endMonth = YearMonth.of(year, 12);
        generateScheduleAndCalendar(config, schedule, startMonth, endMonth);
    }

    static void generateScheduleAndCalendar(Config config, Schedule schedule, YearMonth startMonth, YearMonth endMonth) throws IOException {
        ContentScheduler contentScheduler = new ContentScheduler(schedule, config.getPeople(), config.getWeeklySchedules(), 0);

        // Validate input
        if (startMonth.isAfter(endMonth)) {
            throw new IllegalArgumentException("Start month must be before or equal to end month");
        }

        // Generate schedule for the specified period
        for (YearMonth currentMonth = startMonth; !currentMonth.isAfter(endMonth); currentMonth = currentMonth.plusMonths(1)) {
            contentScheduler.generateFullMonthSchedule(currentMonth);
            contentScheduler.printWeightDistribution();
            System.out.println(schedule.printScheduleMonth(currentMonth));
        }
        schedule.saveToFile();

        // Generate calendar images for the specified period
        CalendarImageGenerator generator = new CalendarImageGenerator(config);
        for (YearMonth currentMonth = startMonth; !currentMonth.isAfter(endMonth); currentMonth = currentMonth.plusMonths(1)) {
            String filename = String.format("schedule_images/%s_%d_calendar.png",
                    currentMonth.getMonth().toString().toLowerCase(),
                    currentMonth.getYear());
            generator.generateCalendarImage(
                    schedule.getSchedule(),
                    currentMonth.getYear(),
                    currentMonth.getMonth(),
                    filename
            );
        }
    }
}
