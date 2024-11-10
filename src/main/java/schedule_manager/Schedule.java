package schedule_manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.*;

public class Schedule {

    private Map<LocalDate, Content> schedule;
    private File file;

    public Schedule() {
        this.schedule = new HashMap<>();
    }

    public Schedule(String filepath) {
        this.file = new File(filepath);
        if (!file.exists()) {
            createEmptyScheduleFile();
        }
        deserialize();
    }

    private void createEmptyScheduleFile() {
        try {
            if (file.createNewFile()) {
                Files.writeString(file.toPath(), "{}"); // Write an empty JSON object
                System.out.println("Created new empty schedule file: " + file.getPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void serialize() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            String json = mapper.writeValueAsString(schedule);
            Files.writeString(file.toPath(), json);
            System.out.println("schedule_manager.Schedule successfully serialized to " + file.getPath());
        } catch (JsonProcessingException e) {
            System.err.println("Error serializing schedule: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error writing schedule to file: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void deserialize() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            String json = Files.readString(file.toPath());

            this.schedule = mapper.readValue(json, new TypeReference<Map<LocalDate, Content>>() {});

            System.out.println("Schedule successfully deserialized from " + file.getPath());
            System.out.println("Deserialized Schedule: " + schedule.toString());
        } catch (JsonProcessingException e) {
            System.err.println("Error deserializing schedule: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error reading schedule from file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Map<LocalDate, Content> getSchedule() {
        return schedule;
    }

    public void setSchedule(Map<LocalDate, Content> schedule) {
        this.schedule = schedule;
    }

    public void addEntry(LocalDate date, Content content) {
        this.schedule.put(date, content);
    }

    public void saveToFile() {
        if (file != null) {
            serialize();
        } else {
            System.err.println("File not specified. Cannot save the schedule.");
        }
    }

    @Override
    public String toString() {
        return "schedule_manager.Schedule{" +
                "schedule=" + schedule +
                '}';
    }

    public static void main(String[] args) {
        Schedule schedule = new Schedule("schedule.json");

    }
}
