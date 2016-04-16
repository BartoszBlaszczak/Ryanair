package ryanair.model.schedule;

public class ScheduleDay {
    int day;
    ScheduleFlight[] flights;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public ScheduleFlight[] getFlights() {
        return flights;
    }

    public void setFlights(ScheduleFlight[] flights) {
        this.flights = flights;
    }
}
