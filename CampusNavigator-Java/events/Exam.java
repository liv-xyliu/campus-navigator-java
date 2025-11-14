package events;

public class Exam extends Event {
    public Exam(int id, String date, String startTime, String endTime, double score, String name) {
        super(id, name, date, startTime, endTime, score);
    }

    @Override
    public String getEventType() {
        return "Exam";
    }

    public String getName() {
        return super.getName();
    }

    public String toCsvRow(int row, int col) {
        return row + "," + col + "," + "exam" + "," + getDate() + "," + getStartTime() + "," + getEndTime() + "," + String.format("%.1f", getScore()) + "," + getName();
    }
}

