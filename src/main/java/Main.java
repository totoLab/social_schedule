import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import schedule_manager.ContentScheduler;
import schedule_manager.Schedule;
import visualization.CalendarImageGenerator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.YearMonth;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        Config config = new Config("config.json");
        config.deserialize();
    }

    void wholeYear() throws IOException {

        Schedule schedule = new Schedule("schedule.json");

        List<String> people = Arrays.asList("Antonio", "Sharon", "Desiree", "Sara", "Marta", "Caterina", "Alessia", "Ines");
        String weeklySchedule = "POST Monday, RIASSUNTO Tuesday, STORIA Wednesday, STORIA Thursday, LOCANDINA Friday, REEL Saturday, STORIA Sunday";

        ContentScheduler contentScheduler = new ContentScheduler(schedule, people, weeklySchedule);

        int year = 2025;
        for (int i = 1; i < 13; i++) {
            YearMonth specifiedMonth = YearMonth.of(year, i);
            contentScheduler.generateFullMonthSchedule(specifiedMonth);
            contentScheduler.printWeightDistribution();
            System.out.println(schedule.printScheduleMonth(specifiedMonth));
            schedule.saveToFile();
        }

        CalendarImageGenerator generator = new CalendarImageGenerator();
        for (int i = 1; i < 13; i++) {
            YearMonth specifiedMonth = YearMonth.of(year, i);
            String filename = String.format("%s_%d_calendar.png", specifiedMonth.getMonth().toString().toLowerCase(), specifiedMonth.getYear());
            generator.generateCalendarImage(schedule.getSchedule(), specifiedMonth.getYear(), specifiedMonth.getMonth(), filename);
        }
    }
}
