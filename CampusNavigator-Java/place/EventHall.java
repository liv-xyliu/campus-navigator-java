package place;

import events.Event;
import utils.Constants;
import utils.PlaceType;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class EventHall extends Place {
    private boolean booked;
    private ArrayList<Event> events = new ArrayList<>();

    public EventHall(String name, double score) {
        super(PlaceType.EVENT_HALL, name, score);
    }
    
    public EventHall(String name, double score, boolean restricted) {
        super(name, score, restricted);
    }

    public boolean isBooked() {
        return booked;
    }

    public void addEvent(Event e) {
        if (e != null) {
            events.add(e);
        }
    }

    @Override
    public void displayEvents() {
        if (events.isEmpty()) {
            System.out.println("Nothing happening here.");
            return;
        }
        System.out.println("Event Schedule:");
        System.out.println(Constants.SCHEDULE_FORMAT_LINE);
        for (Event e : events) {
            System.out.println(String.format(Constants.EVENT_SCHEDULE_FORMATTER, e.getEventType(), e.getDate(), e.getStartTime(), e.getEndTime(), e.getName()));
        }
        System.out.println(Constants.SCHEDULE_FORMAT_LINE);
    }

    @Override
    public void printSchedule() {
        if (isBooked()) {
            System.out.println("You have booked this place!");
        }
        displayEvents();
    }

    @Override
    public void removeEventById(int id) {
        events.removeIf(ev -> ev.getId() == id);
    }

    @Override
    public void writeEventsToCsv(PrintWriter pw, int row, int col) {
        if (events == null || events.isEmpty()) return;
        for (Event e : events) {
            pw.println(e.toCsvRow(row, col));
        }
    }

    @Override
    public char mapSymbol() {
        return isRestricted() ? 'X' : '@';
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
        return "You have successfully booked the event hall.";
    }

    @Override
    public boolean triggersEventSelection() {
        return true;
    }

    @Override
    public boolean isEventVenue() {
        return true;
    }

    @Override
    public boolean allowsEventType(String type) {
        return "lecture".equalsIgnoreCase(type) || "seminar".equalsIgnoreCase(type) || "exam".equalsIgnoreCase(type);
    }
    
    public List<Event> getEvents() {
        return events;
    }
}
