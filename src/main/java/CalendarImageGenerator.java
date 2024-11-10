import schedule_manager.Content;
import schedule_manager.Schedule;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

public class CalendarImageGenerator {

    // Define constants for layout and styling
    private static final int CELL_SIZE = 140;
    private static final int HEADER_HEIGHT = 50;
    private static final int NUM_COLUMNS = 7; // 7 days of the week
    private static final int NUM_ROWS = 6; // Maximum 6 rows for a month
    private static final int CALENDAR_WIDTH = CELL_SIZE * NUM_COLUMNS;
    private static final int CALENDAR_HEIGHT = (CELL_SIZE * NUM_ROWS) + HEADER_HEIGHT;
    private static final int DATE_X_OFFSET = 20;
    private static final int CONTENT_Y_OFFSET = 40;

    private static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 26);
    private static final Font DATE_FONT = new Font("Arial", Font.BOLD, 21);

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
        return new BufferedImage(CALENDAR_WIDTH, CALENDAR_HEIGHT, BufferedImage.TYPE_INT_ARGB);
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
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, CALENDAR_WIDTH, CALENDAR_HEIGHT);
    }

    /**
     * Draws the days of the week header (Sun, Mon, Tue, ...) at the top of the calendar.
     */
    private void drawDaysOfWeekHeader(Graphics2D g2d) {
        String[] daysOfWeek = {"Domenica", "Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato"};
        g2d.setFont(HEADER_FONT);
        g2d.setColor(Color.BLACK);

        FontMetrics headerMetrics = g2d.getFontMetrics();
        for (int i = 0; i < NUM_COLUMNS; i++) {
            int x = i * CELL_SIZE + (CELL_SIZE - headerMetrics.stringWidth(daysOfWeek[i])) / 2;
            g2d.drawString(daysOfWeek[i], x, HEADER_HEIGHT / 3 * 2);
        }
    }

    /**
     * Draws the days of the month in their respective calendar cells.
     */
    private void drawCalendarDays(Graphics2D g2d, Map<LocalDate, Content> schedule, int year, Month month) {
        FontMetrics dateMetrics = g2d.getFontMetrics(DATE_FONT);

        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        int lengthOfMonth = month.length(firstDayOfMonth.isLeapYear());
        int startDay = firstDayOfMonth.getDayOfWeek().getValue() % 7;

        for (int day = 1; day <= lengthOfMonth; day++) {
            int x = (startDay + day - 1) % NUM_COLUMNS;
            int y = (startDay + day - 1) / NUM_COLUMNS;

            // Draw each calendar day with a border
            LocalDate currentDate = LocalDate.of(year, month, day);
            Content content = schedule.get(currentDate);

            if (content != null) {
                drawDayCell(g2d, x, y, day, dateMetrics, makerColors.get(content.getMaker()));
                drawContent(g2d, x, y, content, dateMetrics);
            } else {
                drawDayCell(g2d, x, y, day, dateMetrics, Color.LIGHT_GRAY);
            }
        }
    }

    /**
     * Draws the individual day cell including the day number.
     */
    private void drawDayCell(Graphics2D g2d, int x, int y, int day, FontMetrics dateMetrics, Color backgroundColor) {
        g2d.setColor(backgroundColor);
        g2d.setFont(HEADER_FONT);
        g2d.fillRect(x * CELL_SIZE, y * CELL_SIZE + HEADER_HEIGHT, CELL_SIZE, CELL_SIZE);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x * CELL_SIZE, y * CELL_SIZE + HEADER_HEIGHT, CELL_SIZE, CELL_SIZE);

        String dayString = String.valueOf(day);
        int dayX = x * CELL_SIZE + (CELL_SIZE - dateMetrics.stringWidth(dayString)) / 2;
        g2d.drawString(dayString, dayX, y * CELL_SIZE + HEADER_HEIGHT + CONTENT_Y_OFFSET);
    }

    /**
     * Draws the content in a cell if it exists (maker color and content type).
     */
    private void drawContent(Graphics2D g2d, int x, int y, Content content, FontMetrics dateMetrics) {
        String maker = content.getMaker();
        g2d.setFont(DATE_FONT);
        Color makerColor = makerColors.get(maker);

        // Set the background color to the maker's color and fill the area
        g2d.setColor(makerColor);

        g2d.setColor(Color.BLACK);
        String contentType = content.getType().name();
        int contentX = x * CELL_SIZE + (CELL_SIZE - dateMetrics.stringWidth(contentType)) / 2;
        g2d.drawString(contentType, contentX, y * CELL_SIZE + HEADER_HEIGHT + CONTENT_Y_OFFSET * 2);

        String contentMaker = content.getMaker();
        contentX = x * CELL_SIZE + (CELL_SIZE - dateMetrics.stringWidth(contentMaker)) / 2;
        g2d.drawString(contentMaker, contentX, y * CELL_SIZE + HEADER_HEIGHT + CONTENT_Y_OFFSET * 3);
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
