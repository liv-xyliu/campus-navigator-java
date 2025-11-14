package place;

import utils.PlaceType;

/**
 * Library class represents a library place on campus
 * Students can study here
 */
public class Library extends Place {

    public Library(String name, double score) {
        super(PlaceType.LIBRARY, name, score);
    }
    
    public Library(String name, double score, boolean restricted) {
        super(name, score, restricted);
    }

    @Override
    public void printSchedule() {
        System.out.println("This place does not have a schedule. Select a cafeteria, sport centre, lecture hall or event hall.");
    }

    @Override
    public char mapSymbol() {
        return isRestricted() ? 'X' : 'L';
    }

    @Override
    public String enterMessage() {
        return "You can study here.";
    }

    @Override
    public boolean isScorableOnEnter() {
        return true;
    }

    @Override
    public boolean shouldRepeatScheduleSelection() {
        return true;
    }
}
