import schedule_manager.ContentScheduler;
import schedule_manager.Schedule;

import java.io.IOException;
import java.time.Month;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

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
