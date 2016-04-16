package ryanair.model.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ryanair.model.schedule.ScheduleFlight;

import java.time.LocalDateTime;
import java.time.YearMonth;

public class Flight {

    private String departureAirport;
    private String arrivalAirport;
    @JsonSerialize(using = JsonLocalDateTimeSerializer.class)
    private LocalDateTime departureDateTime;
    @JsonSerialize(using = JsonLocalDateTimeSerializer.class)
    private LocalDateTime arrivalDateTime;

    public Flight(String departureAirport, String arrivalAirport, YearMonth yearMonth, int day, ScheduleFlight flight) {
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        departureDateTime = LocalDateTime.of(yearMonth.atDay(day), flight.getDepartureTime());
        arrivalDateTime = LocalDateTime.of(yearMonth.atDay(day), flight.getArrivalTime());
    }

    public Flight() {}

    public LocalDateTime getArrivalDateTime() {
        return arrivalDateTime;
    }

    public void setArrivalDateTime(LocalDateTime arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
    }

    public LocalDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(LocalDateTime departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }
}
