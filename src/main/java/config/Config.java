package config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Config {

    List<Map<String, String>> peopleColors;
    List<String> people;
    List<String> weeklySchedules;
    String firstWeekday;
    Map<String, String> formatting;

    @JsonIgnore
    File file;

    public Config(String filePath) {
        this.file = new File(filePath);
        if (!file.exists()) throw new IllegalArgumentException("Config file does not exist");
        deserialize();
    }

    @JsonCreator
    public Config(
            @JsonProperty("peopleColors") List<Map<String, String>> peopleColors,
            @JsonProperty("weeklySchedules") List<String> weeklySchedules,
            @JsonProperty("firstWeekday") String firstWeekday,
            @JsonProperty("formatting") Map<String, String> formatting) {

        this.peopleColors = peopleColors;
        this.people = peopleColors.stream().map(o -> o.get("name")).toList();
        this.weeklySchedules = weeklySchedules;
        this.firstWeekday = firstWeekday;
        this.formatting = formatting;
    }

    public void serialize() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);  // Pretty-print JSON output

        try {
            // Convert the Config object to JSON string
            String json = mapper.writeValueAsString(this);

            // Write JSON string to the specified file
            Files.writeString(file.toPath(), json);

            System.out.println("Config successfully serialized to " + file.getPath());
        } catch (JsonProcessingException e) {
            System.err.println("Error serializing config: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error writing config to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deserialize() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        try {
            // If the file does not exist, skip deserialization
            if (!file.exists()) {
                System.out.println("Config file not found. Using default configuration.");
                return;
            }

            // Read JSON content from the file
            String json = Files.readString(file.toPath());

            // Deserialize the JSON string into the current Config object
            Config configData = mapper.readValue(json, new TypeReference<Config>() {});

            // Copy deserialized data to the current object's fields
            this.peopleColors = configData.peopleColors;
            this.weeklySchedules = configData.weeklySchedules;
            this.firstWeekday = configData.firstWeekday;
            this.formatting = configData.formatting;
            this.people = this.peopleColors.stream().map(o -> o.get("name")).toList();

            System.out.println("Config successfully deserialized from " + file.getPath());
        } catch (JsonProcessingException e) {
            System.err.println("Error deserializing config: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error reading config from file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<String> getPeople() {
        return people;
    }

    public void setPeople(List<String> people) {
        this.people = people;
    }

    public List<Map<String, String>> getPeopleColors() {
        return peopleColors;
    }

    public void setPeopleColors(List<Map<String, String>> peopleColors) {
        this.peopleColors = peopleColors;
    }

    public List<String> getWeeklySchedules() {
        return weeklySchedules;
    }

    public void setWeeklySchedules(List<String> weeklySchedules) {
        this.weeklySchedules = weeklySchedules;
    }

    public String getFirstWeekday() {
        return firstWeekday;
    }

    public void setFirstWeekday(String firstWeekday) {
        this.firstWeekday = firstWeekday;
    }

    public Map<String, String> getFormatting() {
        return formatting;
    }

    public void setFormatting(Map<String, String> formatting) {
        this.formatting = formatting;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Config [people=");
        builder.append(people);
        builder.append(", weeklySchedules=");
        builder.append(weeklySchedules);
        builder.append(", firstWeekday=");
        builder.append(firstWeekday);
        builder.append(", formatting=");
        builder.append(formatting);

        return builder.toString();
    }

    static void generate_config() {
        List<String> people = Arrays.asList("Antonio", "Sharon", "Desiree", "Sara", "Marta", "Caterina", "Alessia", "Ines");
        List<String> colorHexValues = Arrays.asList("306BAD", "F9B9F2", "FFAD05", "7CAFC4", "BC412B", "DBAD6A", "59A96A", "DBEFBC");
        List<Map<String, String>> peopleMaps = new ArrayList<>();
        for (int i = 0; i < people.size(); i++) {
            Map<String, String> fields = new HashMap<>();
            fields.put("name", people.get(i));
            fields.put("color", colorHexValues.get(i));
            peopleMaps.add(fields);
        }

        String[] weeklySchedules = new String[] {
                "POST Monday, RIASSUNTO Tuesday, STORIA Wednesday, STORIA Thursday, LOCANDINA Friday, REEL Saturday, STORIA Sunday",
                "POST Monday, RIASSUNTO Tuesday, STORIA Wednesday, TESTIMONIANZA Thursday, LOCANDINA Friday, REEL Saturday, STORIA Sunday"
        };

        Config config = new Config("config.json");

        Map<String, String> formatting = new HashMap<>();
        formatting.put("font", "Cantarell");
        config.setFormatting(formatting);

        config.setFirstWeekday("Tuesday");

        config.setPeopleColors(peopleMaps);
        config.setWeeklySchedules(Arrays.asList(weeklySchedules));
        config.serialize();
    }

    public static String getPathFromResource(String resource) throws URISyntaxException {
        return Paths.get(Thread.currentThread().getContextClassLoader().getResource(resource).toURI()).toString();
    }

    public static void main(String[] args) throws URISyntaxException {
        String resourceName = "config_rcy.json";
        String path = getPathFromResource(resourceName);
        Config config = new Config(path);
    }
}
