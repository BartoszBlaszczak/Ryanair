package ryanair.model;

import org.springframework.format.annotation.DateTimeFormat;
import ryanair.InterconnectingFlightsController;

import java.time.LocalDateTime;

public class Request {

    @DateTimeFormat(pattern = InterconnectingFlightsController.DATE_TIME_FORMAT_WITH_T_SEPARATOR)
    LocalDateTime departureDateTime;
    String departure;
    @DateTimeFormat(pattern = InterconnectingFlightsController.DATE_TIME_FORMAT_WITH_T_SEPARATOR)
    LocalDateTime arrivalDateTime;
    String arrival;

    public LocalDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(LocalDateTime departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public LocalDateTime getArrivalDateTime() {
        return arrivalDateTime;
    }

    public void setArrivalDateTime(LocalDateTime arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

}
