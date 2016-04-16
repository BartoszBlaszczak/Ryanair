package ryanair.model.schedule;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalTime;

public class ScheduleFlight {

    @JsonDeserialize(using = JsonTimeDeserializer.class)
    LocalTime departureTime;
    @JsonDeserialize(using = JsonTimeDeserializer.class)
    LocalTime arrivalTime;

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}
