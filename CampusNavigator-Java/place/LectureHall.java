package place;

import events.Event;
import utils.Constants;
import utils.PlaceType;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class LectureHall extends Place {
    private ArrayList<Event> events = new ArrayList<>();

    public LectureHall(String name, double score) {
        super(PlaceType.LECTURE_HALL, name, score);
    }
    
    public LectureHall(String name, double score, boolean restricted) {
        super(name, score, restricted);
    }

    public void addEvent(Event e) {
        if (e != null) {
            events.add(e);
        }
    }

    @Override
    public void displayEvents() {
        boolean hasTalks = false;
        for (Event e : events) {
            if (e.getEventType().equals("Lecture") || e.getEventType().equals("Seminar")) {
                hasTalks = true;
                break;
            }
        }
        if (!hasTalks) {
            System.out.println("Nothing happening here.");
            return;
        }
        System.out.println("Lecture Schedule:");
        System.out.println(Constants.SCHEDULE_FORMAT_LINE);
        for (Event e : events) {
            if (e.getEventType().equals("Lecture") || e.getEventType().equals("Seminar")) {
                System.out.println(String.format(Constants.EVENT_SCHEDULE_FORMATTER, e.getEventType(), e.getDate(), e.getStartTime(), e.getEndTime(), e.getName()));
            }
        }
        System.out.println(Constants.SCHEDULE_FORMAT_LINE);
    }

    @Override
    public void printSchedule() {
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
            String type = e.getEventType();
            if ("Lecture".equals(type) || "Seminar".equals(type)) {
                pw.println(e.toCsvRow(row, col));
            }
        }
    }

    @Override
    public char mapSymbol() {
        return isRestricted() ? 'X' : '@';
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
    public String eventSelectionPrompt() {
        return "Select a lecture id to visit:";
    }

    @Override
    public boolean allowsEventType(String type) {
        return "lecture".equalsIgnoreCase(type);
    }
    
    public List<Event> getEvents() {
        return events;
    }
}

