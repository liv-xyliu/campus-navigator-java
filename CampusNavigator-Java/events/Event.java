package events;

public abstract class Event {
    protected int id;
    protected String name;
    protected String date;
    protected String startTime;
    protected String endTime;
    protected double score;

    public Event(int id, String name, String date, String startTime, String endTime, double score) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.score = score;
    }

    public abstract String getEventType();
    
    public String getType() {
        return getEventType();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public double getScore() {
        return score;
    }

    @Override
    public String toString() {
        return String.format("%-3d | %-10s | %-10s | %5s - %5s | %.2f pts",
                id, getEventType(), date, startTime, endTime, score);
    }

    public abstract String toCsvRow(int row, int col);
}
