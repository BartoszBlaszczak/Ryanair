package ryanair;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ryanair.model.Request;
import ryanair.model.response.Flight;
import ryanair.model.response.Travel;
import ryanair.model.route.Route;
import ryanair.model.schedule.Schedule;
import ryanair.model.schedule.ScheduleDay;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Service
public class TravelService {

    @Autowired
    RouteService routeService;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String URL_GET_SCHEDULES = "https://api.ryanair.com/timetable/3/schedules/%s/%s/years/%d/months/%d";
    private static final int STARTING_CHANGE_NUMBER = 0;
    private static final int DEFAULT_CHANGE_WAIT_HOURS = 2;

    public List<Travel> getTravels(Request request) {
        List<Route> routes = routeService.getTravelRoutes(request.getDeparture(), request.getArrival());
        return routes.stream().flatMap(route -> getTravelsStream(route, request.getDepartureDateTime(), request.getArrivalDateTime())).collect(toList());
    }

    private Stream<Travel> getTravelsStream(Route route, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {

        List<List<Flight>> availableSectionsFlights = getSectionsFlights(route, departureDateTime, arrivalDateTime);
        List<List<Flight>> flightsResult = new ArrayList<>();
        populateFlightsResult(availableSectionsFlights, STARTING_CHANGE_NUMBER, new ArrayList<>(), departureDateTime, arrivalDateTime, flightsResult);
        return flightsResult.stream().map(Travel::new);
    }

    private List<List<Flight>> getSectionsFlights(Route route, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        List<List<Flight>> sectionsFlights = new ArrayList<>();

        route.getSections().forEach(section -> sectionsFlights.add(
                getMonths(departureDateTime, arrivalDateTime).stream().map(month ->
                        stream(getRyanairScheduleFlights(section.getAirportFrom(), section.getAirportTo(), month))
                                .filter(flight -> flight.getDepartureDateTime().isAfter(departureDateTime))
                                .filter(flight -> flight.getArrivalDateTime().isBefore(arrivalDateTime))
                ).flatMap(flightList -> flightList).collect(toList())
        ));
        return sectionsFlights;
    }

    private List<YearMonth> getMonths(LocalDateTime from, LocalDateTime to) {
        List<YearMonth> months = new ArrayList<>();
        for (int year = from.getYear(); year <= to.getYear(); year++)
            for (int month = from.getMonthValue(); month <= to.getMonthValue(); month++) {
                YearMonth yearMonth = YearMonth.of(year, month);
                if (yearMonth.compareTo(YearMonth.now()) >= 0)
                    months.add(yearMonth);
            }
        return months;
    }

    private void populateFlightsResult(List<List<Flight>> availableSectionsFlights, int sectionNumber, List<Flight> travelFlights,
                                       LocalDateTime departureDateTime, LocalDateTime arrivalDateTime, List<List<Flight>> flightsResult) {

        List<Flight> sectionValidFlights = availableSectionsFlights.get(sectionNumber).stream()
                .filter(flight -> flight.getDepartureDateTime().isAfter(departureDateTime))
                .filter(flight -> flight.getArrivalDateTime().isBefore(arrivalDateTime)).collect(toList());

        for (Flight flight : sectionValidFlights) {
            List<Flight>  newTravelFlights = new ArrayList<>(travelFlights);
            newTravelFlights.add(flight);

            if (sectionNumber == availableSectionsFlights.size() - 1)
                flightsResult.add(newTravelFlights);
            else populateFlightsResult(availableSectionsFlights, sectionNumber+1, newTravelFlights,
                        flight.getArrivalDateTime().plusHours(DEFAULT_CHANGE_WAIT_HOURS), arrivalDateTime, flightsResult);
        }
    }

    private Flight[] getRyanairScheduleFlights(String departure, String arrival, YearMonth yearMonth) {
        try {
            ScheduleDay[] scheduleDays = restTemplate.getForEntity(getSchedulesUrl(departure, arrival, yearMonth), Schedule.class).getBody().getDays();
            return stream(scheduleDays).flatMap(day -> stream(day.getFlights()).map(flight ->
                    new Flight(departure, arrival, yearMonth, day.getDay(), flight))).toArray(Flight[]::new);
        }
        catch (HttpClientErrorException e) {return new Flight[0];}
    }

    private String getSchedulesUrl(String departure, String arrival, YearMonth yearMonth) {
        return String.format(URL_GET_SCHEDULES, departure, arrival, yearMonth.getYear(), yearMonth.getMonthValue());
    }
}
