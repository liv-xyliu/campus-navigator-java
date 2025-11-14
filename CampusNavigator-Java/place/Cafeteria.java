package place;

import utils.Constants;
import utils.PlaceType;

public class Cafeteria extends Place {

    public Cafeteria(String name, double score) {
        super(PlaceType.CAFETERIA, name, score);
    }
    
    public Cafeteria(String name, double score, boolean restricted) {
        super(name, score, restricted);
    }

    @Override
    public void printSchedule() {
        System.out.println("Menu:");
        System.out.println(Constants.MENU_FORMAT_LINE);
        System.out.println(String.format(Constants.MENU_FORMATTER, "coffee", "$2.50"));
        System.out.println(String.format(Constants.MENU_FORMATTER, "tea", "$2.00"));
        System.out.println(String.format(Constants.MENU_FORMATTER, "sandwich", "$4.50"));
        System.out.println(String.format(Constants.MENU_FORMATTER, "muffin", "$2.75"));
        System.out.println(Constants.MENU_FORMAT_LINE);
    }

    @Override
    public char mapSymbol() {
        return isRestricted() ? 'X' : 'C';
    }

    @Override
    public String enterMessage() {
        return "You can eat here if you are hungry.";
    }

    @Override
    public boolean isScorableOnEnter() {
        return true;
    }
}

