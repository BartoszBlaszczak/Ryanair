package ryanair;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ryanair.model.Request;
import ryanair.model.response.Travel;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Bartosz Błaszczak
 */
@RestController
@RequestMapping("/interconnections")
public class InterconnectingFlightsController {

    public static final String DATE_TIME_FORMAT_WITH_T_SEPARATOR = "yyyy-MM-dd'T'HH:mm";

    @Autowired
    private TravelService travelService;

    @RequestMapping(method = GET)
    public List<Travel> travels(Request request) {
        return travelService.getTravels(request);
    }
}