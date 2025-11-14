package place;

import utils.PlaceType;

/**
 * EmptySpace class represents empty walkable areas on the campus map
 */
public class EmptySpace extends Place {

    public EmptySpace() {
        super(PlaceType.EMPTY_PLACE, "Empty Space", 0.0);
    }

    @Override
    public void printSchedule() {
        System.out.println("This place does not have a schedule. Select a cafeteria, sport centre, lecture hall or event hall.");
    }

    @Override
    public char mapSymbol() {
        return '.';
    }
}
