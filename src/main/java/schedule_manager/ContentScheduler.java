package schedule_manager;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.*;

public class ContentScheduler {
    private final Schedule schedule;
    private final List<String> people;
    private Map<String, Map<Type, Integer>> contentWeightMap;
    private final Map<DayOfWeek, Content> weeklyContent;

    public ContentScheduler(Schedule schedule, List<String> people, String program) {
        this.schedule = schedule;
        this.people = new ArrayList<>(people);
        populateWeightsMap();
        this.weeklyContent = parseWeeklySchedule(program);

    }

    /*
     *
     */
    public void populateWeightsMap() {
        this.contentWeightMap = new HashMap<>();
        for (String maker : people) {
            if (!contentWeightMap.containsKey(maker)) {
                contentWeightMap.put(maker, new HashMap<>());
            }
            Map<Type, Integer> contentWeightMapPerson = contentWeightMap.get(maker);
            for (Type type : Type.values()) {
                contentWeightMapPerson.put(type, 0);
            }
        }
        for (LocalDate date : schedule.getSchedule().keySet()) {
            Content content = schedule.getSchedule().get(date);
            Map<Type, Integer> contentWeightMapPerson = contentWeightMap.get(content.getMaker());
            int current = contentWeightMapPerson.get(content.getType());
            contentWeightMapPerson.put(content.getType(), current + 1);
        }
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

    private int getCount(String maker) {
        return contentWeightMap.get(maker).values().stream().mapToInt(Integer::intValue).sum();
    }

    private int getWeight(String maker) {
        return contentWeightMap.get(maker)
                .entrySet()
                .stream()
                .mapToInt(entry -> entry.getValue() * Content.calculateWeight(entry.getKey()))
                .sum();
    }


    private void assignContent(Content content) {
        Type type = content.getType();
        List<String> chosenMakers = new ArrayList<>();
        int minWeight = Integer.MAX_VALUE;

        for (String maker : people) {
            Map<Type, Integer> makerContent = contentWeightMap.get(maker);
            int weight = makerContent.get(type);

            if (weight == minWeight) {
                chosenMakers.add(maker);
            } else if (weight < minWeight) {
                minWeight = weight;
                chosenMakers.clear();
                chosenMakers.add(maker);
            }
        }

        minWeight = Integer.MAX_VALUE;
        String chosenMaker = null;
        if (chosenMakers.size() > 1) {
            for (String maker : chosenMakers) {
                int weight = getCount(maker);
                if (weight < minWeight) {
                    minWeight = weight;
                    chosenMaker = maker;
                }
            }
        } else {
            chosenMaker = chosenMakers.getFirst();
        }

        content.setMaker(chosenMaker);
        Map<Type, Integer> makerContent = contentWeightMap.get(chosenMaker);
        schedule.addEntry(content.getDate(), content);
        makerContent.put(type, makerContent.get(type) + 1);
    }


    public void generateFullMonthSchedule(YearMonth yearMonth) {
        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate firstDayOfMonth = yearMonth.atDay(1);

        // Loop through each day of the month
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = firstDayOfMonth.withDayOfMonth(day);
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            final Content content = weeklyContent.get(dayOfWeek);
            if (content != null) {
                final Content scheduledContent = new Content(content.getType(), dayOfWeek);
                scheduledContent.setDate(date);
                assignContent(scheduledContent);
            }
        }
    }

    public void printWeightDistribution() {
        System.out.println("Current weight distribution:");
        for (String person : people) {
            String msg = String.format(
                    "%s: %s - total count: %d, total weight: %d", person, contentWeightMap.get(person), getCount(person), getWeight(person)
            );
            System.out.println(msg);
        }
    }

    public static void main(String[] args) {
        Schedule schedule = new Schedule("schedule.json");

        List<String> people = Arrays.asList("Antonio", "Sharon", "Desiree", "Sara", "Marta", "Caterina", "Alessia", "Ines");
        String weeklySchedule = "POST Monday, RIASSUNTO Tuesday, STORIA Wednesday, STORIA Thursday, LOCANDINA Friday, REEL Saturday, STORIA Sunday";

        ContentScheduler contentScheduler = new ContentScheduler(schedule, people, weeklySchedule);
        YearMonth specifiedMonth;
        specifiedMonth = YearMonth.of(2024, Month.DECEMBER);
        contentScheduler.generateFullMonthSchedule(specifiedMonth);

        specifiedMonth = YearMonth.of(2025, Month.FEBRUARY);
        contentScheduler.generateFullMonthSchedule(specifiedMonth);

        specifiedMonth = YearMonth.of(2025, Month.MAY);
        contentScheduler.generateFullMonthSchedule(specifiedMonth);
        contentScheduler.populateWeightsMap();
        contentScheduler.printWeightDistribution();
        /*
        System.out.println(schedule.printScheduleMonth(specifiedMonth));
        schedule.saveToFile();
        */
    }
}
