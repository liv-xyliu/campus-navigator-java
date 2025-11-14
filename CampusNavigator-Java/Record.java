import utils.Constants;

public class Record {
    private String mapName;
    private int sessionId;
    private String placeName;
    private String eventName;
    private String date;
    private String timeRange;
    private int moves;
    private int hits;
    private double score;

    public Record(String mapName, int sessionId, String placeName, String eventName,
                  String date, String timeRange, int moves, int hits, double score) {
        this.mapName = mapName;
        this.sessionId = sessionId;
        this.placeName = placeName;
        this.eventName = eventName;
        this.date = date;
        this.timeRange = timeRange;
        this.moves = moves;
        this.hits = hits;
        this.score = score;
    }


    public String print() {
        return String.format(Constants.SCORE_SUMMARY_FORMATTER, mapName, sessionId, placeName, eventName, date, timeRange, moves, hits, score);
    }

    public String backtofile() {
        return mapName + ", " + sessionId + ", " + placeName + ", " + eventName + ", " + date + ", " + timeRange + ", " + moves + ", " + hits + ", " + String.format("%.2f", score);
    }
}

