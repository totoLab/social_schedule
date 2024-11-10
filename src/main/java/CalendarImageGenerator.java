import schedule_manager.Content;
import schedule_manager.Schedule;

import java.awt.*;
        import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

public class CalendarImageGenerator {

    private static Map<String, Color> makerColors = new HashMap<>();

    private static void assignColors() {
        List<String> people = Arrays.asList("Antonio", "Sharon", "Desiree", "Sara", "Marta", "Caterina", "Alessia", "Ines");
        List<String> colors = Arrays.asList("044389", "F9B9F2", "FFAD05", "7CAFC4", "BC412B", "DBAD6A", "59A96A", "485696");
        for (int i = 0; i < people.size(); i++) {
            makerColors.put(people.get(i), Color.getColor(colors.get(i)));
        }
    }

    public static void generateCalendarImage(Map<LocalDate, Content> schedule, int year, Month month, String outputFilePath) throws IOException {
        // Define the size of the calendar (width, height)
        int cellSize = 80; // size of each cell (in pixels)
        int headerHeight = 40;
        int calendarWidth = cellSize * 7; // 7 columns (days of the week)
        int calendarHeight = (cellSize * 6) + headerHeight; // 6 rows (max days) plus header

        // Create a blank image
        BufferedImage image = new BufferedImage(calendarWidth, calendarHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Set rendering hints for better quality
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Fill background with white color
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, calendarWidth, calendarHeight);

        // Set color for the text
        g2d.setColor(Color.BLACK);

        // Draw the header with days of the week (Sun, Mon, Tue, ...)
        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        Font headerFont = new Font("Arial", Font.BOLD, 14);
        g2d.setFont(headerFont);

        for (int i = 0; i < 7; i++) {
            g2d.drawString(daysOfWeek[i], i * cellSize + 10, headerHeight - 10);
        }

        // Set font for calendar days
        Font dateFont = new Font("Arial", Font.PLAIN, 12);
        g2d.setFont(dateFont);

        // Get the first day of the month and calculate the number of days in the month
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        int lengthOfMonth = month.length(firstDayOfMonth.isLeapYear());
        int startDay = firstDayOfMonth.getDayOfWeek().getValue() % 7; // Convert to 0-6 scale

        // Draw the calendar cells
        for (int day = 1; day <= lengthOfMonth; day++) {
            int x = (startDay + day - 1) % 7;
            int y = (startDay + day - 1) / 7 + 1;

            // Draw the rectangle for the day
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillRect(x * cellSize, y * cellSize + headerHeight, cellSize, cellSize);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x * cellSize, y * cellSize + headerHeight, cellSize, cellSize);

            // Draw the day number
            g2d.drawString(String.valueOf(day), x * cellSize + 10, y * cellSize + headerHeight + 20);

            // Check if there's content for this day and draw it
            assignColors();

            LocalDate currentDate = LocalDate.of(year, month, day);
            Content content = schedule.get(currentDate);
            if (content != null) {
                String maker = content.getMaker();
                String colorHex = String.valueOf(makerColors.get(maker));
                Color makerColor = Color.decode(colorHex);

                // Set the color for the maker
                g2d.setColor(makerColor);
                g2d.fillRect(x * cellSize, y * cellSize + headerHeight + 25, cellSize, 10); // Small rectangle to represent the maker's color

                // Set the content type in the cell
                g2d.setColor(Color.BLACK);
                g2d.drawString(content.getType().name().substring(0, 3), x * cellSize + 10, y * cellSize + headerHeight + 40);
            }
        }

        // Save the image as a PNG file
        File outputFile = new File(outputFilePath);
        ImageIO.write(image, "PNG", outputFile);

        g2d.dispose();
        System.out.println("Calendar image saved to: " + outputFilePath);
    }

    public static void main(String[] args) throws IOException {
        Schedule schedule = new Schedule("schedule.json");
        // Generate the image for November 2024 and save it
        generateCalendarImage(schedule.getSchedule(), 2024, Month.NOVEMBER, "november_2024_calendar.png");
    }
}
