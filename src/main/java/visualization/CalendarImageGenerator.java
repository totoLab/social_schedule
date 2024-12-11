package visualization;

import config.Config;
import schedule_manager.Content;
import schedule_manager.Schedule;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;

public class CalendarImageGenerator {

    final Config config;

    // Define constants for layout and styling
    private final int CELL_SIZE = 140;
    private final int TITLE_HEIGHT = 80;
    private final int HEADER_HEIGHT = 50;
    private final int CONTAINER_HEIGHT = TITLE_HEIGHT + HEADER_HEIGHT;
    private final int NUM_COLUMNS = 7; // 7 days of the week
    private final int NUM_ROWS = 6; // Maximum 6 rows for a month
    private final int CALENDAR_WIDTH = CELL_SIZE * NUM_COLUMNS;
    private final int CALENDAR_HEIGHT = (CELL_SIZE * NUM_ROWS) + CONTAINER_HEIGHT;
    private final int CONTENT_Y_OFFSET = 40;

    private String DEFAULT_FONT_NAME;
    private Font TITLE_FONT;
    private Font HEADER_FONT;
    private Font DATE_FONT;
    private Font CONTENT_FONT;

    private final Map<String, Color> makerColors = new HashMap<>();

    // Constructor initializes colors for makers
    public CalendarImageGenerator(Config config) {
        this.config = config;
        Map<String, String> formatting = config.getFormatting();
        setFormatting(formatting);
        initializeMakerColors();
    }

    void setFormatting(Map<String, String> formatting) {
        this.DEFAULT_FONT_NAME = formatting.get("font");
        this.TITLE_FONT = new Font(DEFAULT_FONT_NAME, Font.BOLD, 40);
        this.HEADER_FONT = new Font(DEFAULT_FONT_NAME, Font.BOLD, 26);
        this.DATE_FONT = new Font(DEFAULT_FONT_NAME, Font.BOLD, 16);
        this.CONTENT_FONT = new Font(DEFAULT_FONT_NAME, Font.BOLD, 23);
    }

    /**
     * Initializes the maker colors by mapping each person to a specific color.
     */
    private void initializeMakerColors() {
        List<Map<String, String>> peopleColors = config.getPeopleColors();

        for (int i = 0; i < peopleColors.size(); i++) {
            Map<String, String> entry = peopleColors.get(i);
            makerColors.put(entry.get("name"), Color.decode("#" + entry.get("color")));
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
        drawTitle(g2d, year, month);
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
    private void drawTitle(Graphics2D g2d, int year, Month month) {
        g2d.setFont(TITLE_FONT);
        g2d.setColor(Color.BLACK);
        String[] months = Arrays.asList("Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre").toArray(new String[0]);

        String title = String.format("Calendario RCY %s %d", months[month.getValue() - 1], year);
        FontMetrics titleMetrics = g2d.getFontMetrics();
        int x = (CALENDAR_WIDTH - titleMetrics.stringWidth(title)) / 2;
        g2d.drawString(title, x, TITLE_HEIGHT / 3 * 2);
    }

    /**
     * Draws the days of the week header (Sun, Mon, Tue, ...) at the top of the calendar.
     */
    private void drawDaysOfWeekHeader(Graphics2D g2d) {
        String[] daysOfWeek = {"Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato", "Domenica"};

        // Get the configured first day of the week
        DayOfWeek configuredFirstDay = DayOfWeek.valueOf(config.getFirstWeekday().toUpperCase());

        // Shift the days of week array
        String[] shiftedDaysOfWeek = new String[7];
        for (int i = 0; i < 7; i++) {
            int shiftedIndex = (i + configuredFirstDay.getValue() - 1) % 7;
            shiftedDaysOfWeek[i] = daysOfWeek[shiftedIndex];
        }

        g2d.setFont(HEADER_FONT);
        g2d.setColor(Color.BLACK);

        FontMetrics headerMetrics = g2d.getFontMetrics();
        for (int i = 0; i < NUM_COLUMNS; i++) {
            int x = i * CELL_SIZE + (CELL_SIZE - headerMetrics.stringWidth(shiftedDaysOfWeek[i])) / 2;
            g2d.drawString(shiftedDaysOfWeek[i], x, TITLE_HEIGHT + HEADER_HEIGHT / 3 * 2);
        }
    }

    /**
     * Draws the days of the month in their respective calendar cells.
     */
    private void drawCalendarDays(Graphics2D g2d, Map<LocalDate, Content> schedule, int year, Month month) {
        FontMetrics dateMetrics = g2d.getFontMetrics(DATE_FONT);
        FontMetrics contentMetrics = g2d.getFontMetrics(CONTENT_FONT);

        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        int lengthOfMonth = month.length(firstDayOfMonth.isLeapYear());

        // Get the configured first day of the week
        DayOfWeek configuredFirstDay = DayOfWeek.valueOf(config.getFirstWeekday().toUpperCase());

        // Calculate the offset to align the calendar with the configured first day
        int offset = 7 - (configuredFirstDay.getValue() - firstDayOfMonth.getDayOfWeek().getValue() + 7) % 7;

        for (int day = 1; day <= lengthOfMonth; day++) {
            int x = (day - 1 + offset) % NUM_COLUMNS;
            int y = (day - 1 + offset) / NUM_COLUMNS;

            // Draw each calendar day with a border
            LocalDate currentDate = LocalDate.of(year, month, day);
            Content content = schedule.get(currentDate);

            if (content != null) {
                drawDayCell(g2d, x, y, day, dateMetrics, makerColors.get(content.getMaker()));
                drawContent(g2d, x, y, content, dateMetrics, contentMetrics);
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
        g2d.fillRect(x * CELL_SIZE, y * CELL_SIZE + CONTAINER_HEIGHT, CELL_SIZE, CELL_SIZE);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x * CELL_SIZE, y * CELL_SIZE + CONTAINER_HEIGHT, CELL_SIZE, CELL_SIZE);

        String dayString = String.valueOf(day);
        int dayX = x * CELL_SIZE + (CELL_SIZE - dateMetrics.stringWidth(dayString)) / 2;
        g2d.drawString(dayString, dayX, y * CELL_SIZE + CONTAINER_HEIGHT + CONTENT_Y_OFFSET);
    }

    /**
     * Draws the content in a cell if it exists (maker color and content type).
     */
    private void drawContent(Graphics2D g2d, int x, int y, Content content, FontMetrics dateMetrics, FontMetrics contentMetrics) {
        String maker = content.getMaker();
        g2d.setFont(DATE_FONT);
        Color makerColor = makerColors.get(maker);

        // Set the background color to the maker's color and fill the area
        g2d.setColor(makerColor);

        g2d.setColor(Color.BLACK);
        String contentType = content.getType().name();
        int contentX = x * CELL_SIZE + (CELL_SIZE - dateMetrics.stringWidth(contentType)) / 2;
        g2d.drawString(contentType, contentX, y * CELL_SIZE + CONTAINER_HEIGHT + CONTENT_Y_OFFSET * 2);

        g2d.setFont(CONTENT_FONT);
        String contentMaker = content.getMaker();
        contentX = x * CELL_SIZE + (CELL_SIZE - contentMetrics.stringWidth(contentMaker)) / 2;
        g2d.drawString(contentMaker, contentX, y * CELL_SIZE + CONTAINER_HEIGHT + CONTENT_Y_OFFSET * 3);
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

    static void generateImage() throws IOException {
        Schedule schedule = new Schedule("schedule_rcy.json");
        Config config = new Config("config_rcy.json");
        CalendarImageGenerator generator = new CalendarImageGenerator(config);

        YearMonth yearMonth = YearMonth.of(2025, 1);
        generator.generateCalendarImage(schedule.getSchedule(), yearMonth.getYear(), yearMonth.getMonth(), String.format("schedule_images/%s_%d_calendar.png",  yearMonth.getMonth().toString().toLowerCase(), yearMonth.getYear()));
    }
}
