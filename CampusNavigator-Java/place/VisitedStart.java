package place;

import utils.PlaceType;

/**
 * VisitedStart class represents a visited starting position on the campus map
 */
public class VisitedStart extends Place {

    public VisitedStart() {
        super(PlaceType.EMPTY_PLACE, "Visited Start", 0.0);
    }

    @Override
    public void printSchedule() {
        System.out.println("This place does not have a schedule. Select a cafeteria, sport centre, lecture hall or event hall.");
    }

    @Override
    public char mapSymbol() {
        return 'S';
    }
}
