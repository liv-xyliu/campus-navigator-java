import java.util.*;

/**
 * Student class represents a student navigating the campus
 * Contains position, movement history, and score tracking
 */
public class Student {
    private int row;
    private int col;
    private int moves;
    private int hits;
    private int sessionId;
    private String mapName;
    private List<ScoreRecord> scoreHistory;

    public Student() {
        this.moves = 0;
        this.hits = 0;
        this.scoreHistory = new ArrayList<>();
    }

    public Student(int startRow, int startCol, int sessionId, String mapName) {
        this.row = startRow;
        this.col = startCol;
        this.moves = 0;
        this.hits = 0;
        this.sessionId = sessionId;
        this.mapName = mapName;
        this.scoreHistory = new ArrayList<>();
    }

    // Getters and setters
    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getMoves() {
        return moves;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public int getSessionId() {
        return sessionId;
    }

    public String getMapName() {
        return mapName;
    }

    public void incrementMoves() {
        this.moves++;
    }

    public void incrementHits() {
        this.hits++;
    }

    public void moveTo(int newRow, int newCol) {
        this.row = newRow;
        this.col = newCol;
        this.moves++;
    }

    public void hitBoundary() {
        this.hits++;
    }
    
    public void recordMove() {
        this.moves++;
    }
    
    public void recordHit() {
        this.hits++;
    }
    
    public void addScore(String placeName, String eventName, String date, String timeRange, double score) {
        ScoreRecord record = new ScoreRecord("", 0, placeName, eventName, date, timeRange, moves, hits, score);
        scoreHistory.add(record);
    }
    
    public List<ScoreRecord> getScoreHistory() {
        return scoreHistory;
    }
    
    public String getName() {
        return "Student";
    }
}
