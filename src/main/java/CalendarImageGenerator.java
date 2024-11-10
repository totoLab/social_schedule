import schedule_manager.Content;
import schedule_manager.Schedule;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

public class CalendarImageGenerator {

    private final Map<String, Color> makerColors = new HashMap<>();

    // Constructor initializes colors for makers
    public CalendarImageGenerator() {
        initializeMakerColors();
    }

    /**
     * Initializes the maker colors by mapping each person to a specific color.
     */
    private void initializeMakerColors() {
        List<String> people = Arrays.asList("Antonio", "Sharon", "Desiree", "Sara", "Marta", "Caterina", "Alessia", "Ines");
        List<String> colorHexValues = Arrays.asList("044389", "F9B9F2", "FFAD05", "7CAFC4", "BC412B", "DBAD6A", "59A96A", "485696");

        for (int i = 0; i < people.size(); i++) {
            makerColors.put(people.get(i), Color.decode("#" + colorHexValues.get(i)));
        }
    }

    /**
     * Generates the calendar image for the given month and year.
     */
    public void generateCalendarImage(Map<LocalDate, Content> schedule, int year, Month month, String outputFilePath) throws IOException {
        BufferedImage image = createBlankImage();
        Graphics2D g2d = image.createGraphics();

        // Set up rendering hints for better quality
        configureGraphics(g2d);

        // Draw the calendar components
        drawBackground(g2d);
        drawDaysOfWeekHeader(g2d);
        drawCalendarDays(g2d, schedule, year, month);

        // Save the generated image
        saveImage(image, outputFilePath);

        g2d.dispose();
        System.out.println("Calendar image saved to: " + outputFilePath);
    }

    /**
     * Creates a blank image to draw the calendar.
     */
    private BufferedImage createBlankImage() {
        final int cellSize = 80;
        final int headerHeight = 40;
        final int calendarWidth = cellSize * 7;
        final int calendarHeight = (cellSize * 6) + headerHeight;
        return new BufferedImage(calendarWidth, calendarHeight, BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * Configures the graphics context for drawing with antialiasing.
     */
    private void configureGraphics(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    /**
     * Fills the background of the calendar with a white color.
     */
    private void drawBackground(Graphics2D g2d) {
        final int calendarWidth = 560;
        final int calendarHeight = 480;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, calendarWidth, calendarHeight);
    }

    /**
     * Draws the days of the week header (Sun, Mon, Tue, ...) at the top of the calendar.
     */
    private void drawDaysOfWeekHeader(Graphics2D g2d) {
        final int cellSize = 80;
        final int headerHeight = 40;
        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        Font headerFont = new Font("Arial", Font.BOLD, 14);
        g2d.setFont(headerFont);

        FontMetrics headerMetrics = g2d.getFontMetrics();
        for (int i = 0; i < 7; i++) {
            int x = i * cellSize + (cellSize - headerMetrics.stringWidth(daysOfWeek[i])) / 2;
            g2d.drawString(daysOfWeek[i], x, headerHeight - 10);
        }
    }

    /**
     * Draws the days of the month in their respective calendar cells.
     */
    private void drawCalendarDays(Graphics2D g2d, Map<LocalDate, Content> schedule, int year, Month month) {
        final int cellSize = 80;
        final int headerHeight = 40;

        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        int lengthOfMonth = month.length(firstDayOfMonth.isLeapYear());
        int startDay = firstDayOfMonth.getDayOfWeek().getValue() % 7;

        Font dateFont = new Font("Arial", Font.PLAIN, 12);
        g2d.setFont(dateFont);
        FontMetrics dateMetrics = g2d.getFontMetrics();

        for (int day = 1; day <= lengthOfMonth; day++) {
            int x = (startDay + day - 1) % 7;
            int y = (startDay + day - 1) / 7 + 1;

            // Draw each calendar day with a border
            drawDayCell(g2d, x, y, cellSize, headerHeight, day, dateMetrics);

            // Draw the content for each day if it exists
            LocalDate currentDate = LocalDate.of(year, month, day);
            Content content = schedule.get(currentDate);
            if (content != null) {
                drawContent(g2d, x, y, cellSize, headerHeight, content, dateMetrics);
            }
        }
    }

    /**
     * Draws the individual day cell including the day number.
     */
    private void drawDayCell(Graphics2D g2d, int x, int y, int cellSize, int headerHeight, int day, FontMetrics dateMetrics) {
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(x * cellSize, y * cellSize + headerHeight, cellSize, cellSize);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x * cellSize, y * cellSize + headerHeight, cellSize, cellSize);

        String dayString = String.valueOf(day);
        int dayX = x * cellSize + (cellSize - dateMetrics.stringWidth(dayString)) / 2;
        g2d.drawString(dayString, dayX, y * cellSize + headerHeight + 20);
    }

    /**
     * Draws the content in a cell if it exists (maker color and content type).
     */
    private void drawContent(Graphics2D g2d, int x, int y, int cellSize, int headerHeight, Content content, FontMetrics dateMetrics) {
        String maker = content.getMaker();
        Color makerColor = makerColors.get(maker);

        // Draw a small rectangle representing the maker's color
        g2d.setColor(makerColor);
        g2d.fillRect(x * cellSize, y * cellSize + headerHeight + 25, cellSize, 10);

        // Draw the content type abbreviation
        String contentType = content.getType().name().substring(0, 3);
        int contentX = x * cellSize + (cellSize - dateMetrics.stringWidth(contentType)) / 2;
        g2d.drawString(contentType, contentX, y * cellSize + headerHeight + 40);
    }

    /**
     * Saves the generated calendar image to a file.
     */
    private void saveImage(BufferedImage image, String outputFilePath) throws IOException {
        File outputFile = new File(outputFilePath);

        // Check if the parent directory exists, and create it if necessary
        File parentDir = outputFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs(); // Create the parent directory if it does not exist
        }

        // Save the image as PNG
        ImageIO.write(image, "PNG", outputFile);
    }

    public static void main(String[] args) throws IOException {
        Schedule schedule = new Schedule("schedule.json");
        CalendarImageGenerator generator = new CalendarImageGenerator();

        // Generate and save the calendar image for December 2024
        generator.generateCalendarImage(schedule.getSchedule(), 2024, Month.DECEMBER, "december_2024_calendar.png");
    }
}
