package place;

import utils.Constants;
import utils.PlaceType;
import interfaces.Bookable;

public class SportsCentre extends Place implements Bookable {
    private boolean booked;

    public SportsCentre(String name, double score) {
        super(PlaceType.SPORTS_CENTRE, name, score);
    }
    
    public boolean isBooked() {
        return booked;
    }

    @Override
    public char mapSymbol() {
        return isRestricted() ? 'X' : 'G';
    }

    @Override
    public boolean isBookable() {
        return true;
    }

    @Override
    public boolean book() {
        if (booked) return false;
        this.booked = true;
        return true;
    }

    @Override
    public String bookingSuccessMessage() {
        return "You have successfully booked the sports centre.";
    }

    @Override
    public void printSchedule() {
        if (isBooked()) {
            System.out.println("You have booked this sports centre!");
        }
        System.out.println("Facilities:");
        System.out.println(Constants.SPORTCENTRE_FORMAT_LINE);
        System.out.println(String.format(Constants.SPORTCENTRE_FORMATTER, "Gymnasium"));
        System.out.println(String.format(Constants.SPORTCENTRE_FORMATTER, "Swimming Pool"));
        System.out.println(String.format(Constants.SPORTCENTRE_FORMATTER, "Basketball Court"));
        System.out.println(String.format(Constants.SPORTCENTRE_FORMATTER, "Tennis Court"));
        System.out.println(String.format(Constants.SPORTCENTRE_FORMATTER, "Fitness Studio"));
        System.out.println(Constants.SPORTCENTRE_FORMAT_LINE);
    }

    @Override
    public String enterMessage() {
        return "You can exercise here to keep fit.";
    }

    @Override
    public boolean isScorableOnEnter() {
        return true;
    }
}

