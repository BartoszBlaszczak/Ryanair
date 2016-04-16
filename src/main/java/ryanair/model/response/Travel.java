package ryanair.model.response;

import java.util.List;

public class Travel {
    private Integer stops;
    private List<Flight> legs;

    public Travel(List<Flight> flights) {
        legs = flights;
        stops = flights.size()-1;
    }

    public Travel() {}

    public Integer getStops() {
        return stops;
    }

    public void setStops(Integer stops) {
        this.stops = stops;
    }

    public List<Flight> getLegs() {
        return legs;
    }

    public void setLegs(List<Flight> legs) {
        this.legs = legs;
    }
}
