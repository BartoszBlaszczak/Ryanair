package ryanair.model.route;

import java.util.List;

public class Route {

    private List<Section> sections;

    public Route() {}

    public Route(List<Section> sections) {
        this.sections = sections;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }
}
