package schedule_manager;

import config.Config;
import utils.Utils;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.ceil;

public class ContentScheduler {
    private final Schedule schedule;
    private final List<String> people;
    private Map<String, Map<Type, Integer>> contentCountMap;
    private Map<DayOfWeek, Content> weeklyContent;
    private String currentWeeklySchedule;
    private final List<String> weeklySchedules;

    private boolean emptyOnly = false;

    Map<String, Integer> monthContentCounter;

    public ContentScheduler(Schedule schedule, List<String> people, List<String> weeklySchedules, int currentWeeklySchedule) {
        this.schedule = schedule;
        this.people = new ArrayList<>(people);
        this.weeklySchedules = new ArrayList<>(weeklySchedules);
        this.currentWeeklySchedule = weeklySchedules.get(currentWeeklySchedule);

        populateCountMap();
        this.weeklyContent = parseWeeklySchedule(this.currentWeeklySchedule);
    }

    public void setEmptyOnly(boolean emptyOnly) {
        this.emptyOnly = emptyOnly;
    }

    public void populateCountMap() {
        this.contentCountMap = new HashMap<>();
        for (String maker : people) {
            contentCountMap.put(maker, new HashMap<>());
            Map<Type, Integer> contentWeightMapPerson = contentCountMap.get(maker);
            for (Type type : Type.values()) {
                contentWeightMapPerson.put(type, 0);
            }
        }

        // Populate existing schedule counts
        for (LocalDate date : schedule.getSchedule().keySet()) {
            Content content = schedule.getSchedule().get(date);
            Map<Type, Integer> contentCountMapPerson = contentCountMap.get(content.getMaker());

            if (contentCountMapPerson != null) {
                int current = contentCountMapPerson.get(content.getType());
                contentCountMapPerson.put(content.getType(), current + 1);
            }
        }
    }

    private void assignContent(Content content) {
        Type type = content.getType();
        List<String> eligibleMakers = new ArrayList<>();
        int minTypeWeight = Integer.MAX_VALUE;

        int maxMonthlyContent = (int) ceil(31.0f / people.size());
        List<String> nonMaxPeople = monthContentCounter.keySet().stream().filter(maker -> monthContentCounter.get(maker) < maxMonthlyContent).toList();

        // First, find makers with the minimum weight for this specific content type
        for (String maker : nonMaxPeople) {
            Map<Type, Integer> makerContent = contentCountMap.get(maker);
            int typeWeight = makerContent.get(type);

            if (typeWeight < minTypeWeight) {
                minTypeWeight = typeWeight;
                eligibleMakers.clear();
                eligibleMakers.add(maker);
            } else if (typeWeight == minTypeWeight) {
                eligibleMakers.add(maker);
            }
        }

        // If multiple makers have the same minimum type weight,
        // choose based on overall content weight
        int minWeight = eligibleMakers.stream()
                .mapToInt(this::getWeight)
                .min()
                .orElse(0);

        List<String> minWeightMakers = eligibleMakers.stream()
                .filter(maker -> getWeight(maker) == minWeight)
                .collect(Collectors.toList());
        String chosenMaker = minWeightMakers.get(new Random().nextInt(minWeightMakers.size()));

        content.setMaker(chosenMaker);
        updateCount(content);
        schedule.addEntry(content.getDate(), content);
    }

    void updateCount(Content content) {
        int current;
        Map<Type, Integer> contentCountMapPerson = contentCountMap.get(content.getMaker());

        if (schedule.getSchedule().containsKey(content.getDate())) {
            Content toRemove = schedule.getSchedule().get(content.getDate());

            current = contentCountMapPerson.get(toRemove.getType());
            contentCountMapPerson.put(toRemove.getType(), current - 1);
        }

        current = contentCountMapPerson.get(content.getType());
        contentCountMapPerson.put(content.getType(), current + 1);
    }

    /**
     * Parses a weekly schedule string and returns a list of content to make based on it.
     */
    public Map<DayOfWeek, Content> parseWeeklySchedule(String weeklyProgram) {
        Map<DayOfWeek, Content> contentList = new HashMap<>();
        String[] entries = weeklyProgram.split(", ");

        for (String entry : entries) {
            String[] parts = entry.split(" ");
            Type type = Type.valueOf(parts[0]);
            DayOfWeek dayOfWeek = DayOfWeek.valueOf(parts[1].toUpperCase());
            contentList.put(dayOfWeek, new Content(type, dayOfWeek));
        }
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            if (!contentList.containsKey(dayOfWeek)) {
                contentList.put(dayOfWeek, null);
            }
        }

        return contentList;
    }

    public void switchWeeklySchedule(String newWeeklySchedule) {
        if (!weeklySchedules.contains(newWeeklySchedule)) {
            throw new IllegalArgumentException("New weekly schedule must be in the list of weekly schedules");
        }
        this.currentWeeklySchedule = newWeeklySchedule;
        this.weeklyContent = parseWeeklySchedule(newWeeklySchedule);
    }

    public String getCurrentWeeklySchedule() {
        return currentWeeklySchedule;
    }

    private int getCount(String maker) {
        return contentCountMap.get(maker).values().stream().mapToInt(Integer::intValue).sum();
    }

    private int getWeight(String maker) {
        return contentCountMap.get(maker)
                .entrySet()
                .stream()
                .mapToInt(entry -> entry.getValue() * Content.calculateWeight(entry.getKey()))
                .sum();
    }

    public void printWeightDistribution() {
        System.out.println("Current weight distribution:");
        for (String person : people) {
            Map<Type, Integer> personContentCount = contentCountMap.get(person);
            StringBuilder typeBreakdown = new StringBuilder();
            for (Type type : Type.values()) {
                typeBreakdown.append(String.format("%s:%d ", type, personContentCount.get(type)));
            }

            String msg = String.format(
                    "%s: %s - total count: %d, total weight: %d",
                    person,
                    typeBreakdown,
                    getCount(person),
                    getWeight(person)
            );
            System.out.println(msg);
        }
    }

    public void generateFullMonthSchedule(YearMonth yearMonth) {
        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate firstDayOfMonth = yearMonth.atDay(1);

        monthContentCounter = new HashMap<>();
        for (String maker : people) { monthContentCounter.put(maker, 0); }

        int scheduleIndex = weeklySchedules.indexOf(currentWeeklySchedule);
        Map<DayOfWeek, Content> currentWeeklyContent = weeklyContent;

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = firstDayOfMonth.withDayOfMonth(day);
            if (this.emptyOnly && schedule.getSchedule().containsKey(date)) {
                continue;
            }

            if (date.getDayOfWeek() == DayOfWeek.MONDAY) {
                scheduleIndex = (scheduleIndex + 1) % weeklySchedules.size();
                currentWeeklySchedule = weeklySchedules.get(scheduleIndex);
                currentWeeklyContent = parseWeeklySchedule(currentWeeklySchedule);
            }

            DayOfWeek dayOfWeek = date.getDayOfWeek();
            final Content content = currentWeeklyContent.get(dayOfWeek);
            if (content != null) {
                final Content scheduledContent = new Content(content.getType(), dayOfWeek);
                scheduledContent.setDate(date);
                assignContent(scheduledContent);
            }
        }
    }

    public static void main(String[] args) {
        try {
            Schedule schedule = new Schedule("schedule_rcy.json");
            Config config = new Config("config_rcy.json");
            ContentScheduler contentScheduler = new ContentScheduler(schedule, config.getPeople(), config.getWeeklySchedules(), 0);
            // contentScheduler.populateCountMap();
            YearMonth dates = YearMonth.of(2025, 2);
            contentScheduler.generateFullMonthSchedule(dates);
            contentScheduler.printWeightDistribution();
            System.out.println(schedule.printScheduleMonth(dates));
            if (Utils.yesNo(String.format("Do you want to save %s?", schedule.getFilename()))) {
                schedule.saveToFile();
            }
        } catch (Exception e) {
            System.err.println("Couldn't update schedule correctly correctly: \n\t" + e.getMessage());
        }

    }
}
