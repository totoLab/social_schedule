import config.Config;
import schedule_manager.ContentScheduler;
import schedule_manager.Schedule;
import visualization.CalendarImageGenerator;

import java.io.IOException;
import java.time.YearMonth;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        Config config = new Config("config.json");
        config.deserialize();
    }

    void wholeYear() throws IOException {

        Schedule schedule = new Schedule("schedule.json");
        Config config = new Config("config.json");

        ContentScheduler contentScheduler = new ContentScheduler(schedule, config.getPeople(), config.getWeeklySchedules().getFirst());

        int year = 2025;
        for (int i = 1; i < 13; i++) {
            YearMonth specifiedMonth = YearMonth.of(year, i);
            contentScheduler.generateFullMonthSchedule(specifiedMonth);
            contentScheduler.printWeightDistribution();
            System.out.println(schedule.printScheduleMonth(specifiedMonth));
            schedule.saveToFile();
        }

        CalendarImageGenerator generator = new CalendarImageGenerator(config);
        for (int i = 1; i < 13; i++) {
            YearMonth specifiedMonth = YearMonth.of(year, i);
            String filename = String.format("%s_%d_calendar.png", specifiedMonth.getMonth().toString().toLowerCase(), specifiedMonth.getYear());
            generator.generateCalendarImage(schedule.getSchedule(), specifiedMonth.getYear(), specifiedMonth.getMonth(), filename);
        }
    }
}
