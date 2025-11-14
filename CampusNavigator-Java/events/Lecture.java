package events;

public class Lecture extends Event {
    private String speakers;

    public Lecture(int id, String date, String startTime, String endTime, double score, String name, String speakers) {
        super(id, name, date, startTime, endTime, score);
        this.speakers = speakers;
    }

    @Override
    public String getEventType() {
        return "Lecture";
    }

    public String getName() {
        return super.getName();
    }

    public String getSpeakers() {
        return speakers;
    }

    public String toCsvRow(int row, int col) {
        return row + "," + col + "," + "lecture" + "," + getDate() + "," + getStartTime() + "," + getEndTime() + "," + String.format("%.1f", getScore()) + "," + getName() + "," + getSpeakers();
    }
}

