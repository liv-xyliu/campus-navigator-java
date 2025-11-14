package place;

import utils.PlaceType;

/**
 * Boundary class represents boundary walls on the campus map
 */
public class Boundary extends Place {

    public Boundary() {
        super(PlaceType.EMPTY_PLACE, "Boundary", 0.0);
        setRestricted(true);
    }

    @Override
    public void printSchedule() {
        System.out.println("This place does not have a schedule. Select a cafeteria, sport centre, lecture hall or event hall.");
    }

    @Override
    public char mapSymbol() {
        return '#';
    }
}
