package place;

import events.Event;
import utils.PlaceType;

import java.io.PrintWriter;

public abstract class Place {
    private PlaceType placeType;
    private String placeName;
    private double positionScore;
    private boolean visited;
    private boolean restricted;

    public Place() {
    }

    public Place(PlaceType placeType, String name, double score) {
        this.placeType = placeType;
        this.placeName = name;
        this.positionScore = score;
        this.restricted = false;
        this.visited = false;
    }
    
    public Place(String name, double score, boolean restricted) {
        this.placeName = name;
        this.positionScore = score;
        this.restricted = restricted;
        this.visited = false;
    }

    public String getPlaceName() {
        return this.placeName;
    }
    
    public String getName() {
        return this.placeName;
    }

    public double getPositionScore() {
        return this.positionScore;
    }
    
    public double getScore() {
        return this.positionScore;
    }

    public boolean isRestricted() {
        return this.restricted;
    }

    public void setRestricted(boolean restricted) {
        this.restricted = restricted;
    }

    public boolean isVisited() {
        return this.visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public void printSchedule() {
        System.out.println("This place does not have a schedule. Select a cafeteria, sport centre, lecture hall or event hall.");
    }

    public void displayEvents() {
        System.out.println("Nothing happening here.");
    }

    public void removeEventById(int id) {
    }

    public void writeEventsToCsv(PrintWriter pw, int row, int col) {
    }

    public char mapSymbol() {
        return '.';
    }

    public boolean isBookable() {
        return false;
    }

    public boolean isBooked() {
        return false;
    }

    public boolean book() {
        return false;
    }

    public String bookingSuccessMessage() {
        return null;
    }

    public String enterMessage() {
        return null;
    }

    public boolean isScorableOnEnter() {
        return false;
    }

    public boolean triggersEventSelection() {
        return false;
    }

    public boolean isEventVenue() {
        return false;
    }

    public String eventSelectionPrompt() {
        return "Select an event id to visit:";
    }

    public boolean allowsEventType(String type) {
        return false;
    }

    public void addEvent(Event e) { /* no-op by default */ }

    public boolean shouldRepeatScheduleSelection() {
        return false;
    }
}
