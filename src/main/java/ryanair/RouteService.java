package ryanair;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ryanair.model.route.Route;
import ryanair.model.route.Section;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.stream;

@Service
public class RouteService {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final int MAX_CHANGES = 1;
    private static final String URL_GET_ROUTES = "https://api.ryanair.com/core/3/routes/";

    public List<Route> getTravelRoutes(String departure, String arrival) {
        List<Route> routes = new ArrayList<>();
        createTravelRoutes(routes,  getRyanairSections(), new ArrayList<>(), 0, departure, arrival);
        return routes;
    }

    private void createTravelRoutes(List<Route> routes, Section[] allSections, List<Section> routeSections, int changeNumber, String departureAirport, String arrivalAirport) {

        Section[] startingSections = stream(allSections).filter(section -> section.getAirportFrom().equals(departureAirport)).toArray(Section[]::new);

        for (Section section : startingSections) {
            if (section.getAirportTo().equals(arrivalAirport)) {
                List<Section> validSections = new ArrayList<>(routeSections); validSections.add(section);
                routes.add(new Route(validSections));
            }
            else if (changeNumber <= MAX_CHANGES-1) {
                List<Section> newRouteSections = new ArrayList<>(routeSections); newRouteSections.add(section);
                createTravelRoutes(routes, allSections, newRouteSections, changeNumber+1, section.getAirportTo(), arrivalAirport);
            }
        }
    }

    private Section[] getRyanairSections() {
        return restTemplate.getForEntity(URL_GET_ROUTES, Section[].class).getBody();
    }
}
