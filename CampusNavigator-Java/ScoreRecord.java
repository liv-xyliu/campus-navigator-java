public class ScoreRecord {
    private String mapName;
    private int sessionId;
    private String placeName;
    private String eventName;
    private String date;
    private String timeRange;
    private int moves;
    private int hits;
    private double score;

    public ScoreRecord(String mapName, int sessionId, String placeName, String eventName,
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


    public String getDate() {
        return date;
    }

    public int getMoves() {
        return moves;
    }

    public int getHits() {
        return hits;
    }

    public double getScore() {
        return score;
    }
}
