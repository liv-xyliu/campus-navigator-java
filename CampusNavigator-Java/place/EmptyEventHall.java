package place;

import utils.PlaceType;

/**
 * EmptyEventHall class represents an event hall with no events
 */
public class EmptyEventHall extends Place {

    public EmptyEventHall() {
        super(PlaceType.EMPTY_PLACE, "Empty Event Hall", 0.0);
    }

    @Override
    public void printSchedule() {
        System.out.println("This place does not have a schedule. Select a cafeteria, sport centre, lecture hall or event hall.");
    }

    @Override
    public char mapSymbol() {
        return 'E';
    }

    @Override
    public String enterMessage() {
        return "Nothing happening here.";
    }
}
