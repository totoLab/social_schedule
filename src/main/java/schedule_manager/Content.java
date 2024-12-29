package schedule_manager;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class Content {

    private final Type type;
    private final int weight;
    private final DayOfWeek day;
    private LocalDate date;
    private String maker;

    @JsonCreator
    public Content(
            @JsonProperty("type") Type type,
            @JsonProperty("date") LocalDate date,
            @JsonProperty("maker") String maker
    ) {
        this.type = type;
        this.weight = calculateWeight(type);
        this.day = date.getDayOfWeek();
        this.date = date;
        this.maker = maker;
    }

    public Content(Type type, DayOfWeek day) {
        this.type = type;
        this.weight = calculateWeight(type);
        this.day = day;
    }

    public Type getType() {
        return type;
    }

    @JsonIgnore
    public int getWeight() {
        return weight;
    }

    @JsonIgnore
    public DayOfWeek getDay() {
        return day;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    static int calculateWeight(Type type) {
        int weight = 0;
        switch (type) {
            case STORIA:
                weight = 1;
                break;
            case POST:
            case LOCANDINA:
            case TESTIMONIANZA:
                weight = 2;
                break;
            case REEL:
            case RIASSUNTO:
                weight = 3;
                break;
       }
       return weight;
    }


    @Override
    public String toString() {
        return String.format("(%s %s): %s - %s", day, date, type, maker);
    }

}
