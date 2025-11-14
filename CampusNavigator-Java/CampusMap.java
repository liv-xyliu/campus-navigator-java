import events.Exam;
import events.Lecture;
import events.Seminar;
import place.*;
import utils.Constants;
import utils.Messages;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

class CampusMap {
    private final int rows;
    private final int cols;
    private final int sessionId;
    private final String mapName;
    private final String mapFile;
    private final String eventsFile;
    private final Map<String, List<EventRec>> eventsByPos = new HashMap<>();
    private final List<Record> records = new ArrayList<>();
    private final Map<String, Integer> placeEventIdCounter = new HashMap<>();
    private final Set<String> visitedNonEventPlaces = new HashSet<>();
    private final Set<String> originalEmptyEventHalls = new HashSet<>();
    private Place[][] grid;
    private Student student;
    private boolean startMarked = false;
    private boolean autoEnded = false;
    private boolean sessionCompleteArmed = false;

    CampusMap(int rows, int cols, int sessionId, String mapName, String mapFile, String eventsFile) {
        this.rows = rows;
        this.cols = cols;
        this.sessionId = sessionId;
        this.mapName = mapName;
        this.mapFile = mapFile;
        this.eventsFile = eventsFile;
        this.grid = new Place[rows][cols];
        this.student = new Student(1, 1, sessionId, mapName);
        initGrid();
        loadMap();
        loadEvents();
    }

    private static String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    void run() {
        boolean running = true;
        while (running) {
            Messages.printMainMenuCommands();
            try {
                String opt = Constants.keyboard.nextLine().trim();
                switch (opt) {
                    case "1":
                        visitCampus();
                        if (autoEnded) {
                            running = false;
                        }
                        break;
                    case "2":
                        printSchedule();
                        break;
                    case "3":
                        bookPlace();
                        break;
                    case "4":
                        printScoreHistory();
                        break;
                    case "5":
                        printPreviousSessionHistory();
                        break;
                    case "6":
                        System.out.println("Session abandoned.");
                        System.out.println("Goodbye for now. Visit Again.");
                        // Restore events write-back on explicit quit
                        writeBackUnattendedEvents();
                        exportSessionScores();
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please select a valid option.");
                }
            } catch (java.util.NoSuchElementException e) {
                // Handle EOF gracefully
                System.out.println("Session abandoned.");
                System.out.println("Goodbye for now. Visit Again.");
                writeBackUnattendedEvents();
                exportSessionScores();
                running = false;
            }
        }
    }

    private void initGrid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i == 0 || j == 0 || i == rows - 1 || j == cols - 1) {
                    grid[i][j] = new Boundary(); // Boundary
                } else {
                    grid[i][j] = new EmptySpace(); // Empty space initially
                }
            }
        }
    }

    private void loadMap() {
        try (BufferedReader br = new BufferedReader(new FileReader(mapFile))) {
            String line;
            int lineNo = 0;
            while ((line = br.readLine()) != null) {
                lineNo++;
                if (lineNo == 1) continue; // header
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] t = line.split(",");
                if (t.length != 6) {
                    System.out.println("Invalid line for maps. Skipping this line " + lineNo + " from the maps file.");
                    continue;
                }
                
                int r, c;
                String type, name;
                double score;
                String restricted;
                
                // parse row
                try {
                    r = Integer.parseInt(t[0].trim());
                } catch (Exception e) {
                    System.out.println("Invalid row format. Skipping this line " + lineNo + " from the maps file.");
                    continue;
                }
                // parse column
                try {
                    c = Integer.parseInt(t[1].trim());
                } catch (Exception e) {
                    System.out.println("Invalid column format. Skipping this line " + lineNo + " from the maps file.");
                    continue;
                }

                // bounds check first (prioritize row/column errors over others)
                if (r <= 0 || r >= rows - 1) {
                    System.out.println("Invalid row format. Skipping this line " + lineNo + " from the maps file.");
                    continue;
                }
                if (c <= 0 || c >= cols - 1) {
                    System.out.println("Invalid column format. Skipping this line " + lineNo + " from the maps file.");
                    continue;
                }
                
                // Check for empty fields after row/column validation
                if (t[2].trim().isEmpty()) {
                    System.out.println("Place type cannot be empty. Skipping this line " + lineNo + " from the maps file.");
                    continue;
                }
                if (t[3].trim().isEmpty()) {
                    System.out.println("Place name cannot be empty. Skipping this line " + lineNo + " from the maps file.");
                    continue;
                }
                if (t[4].trim().isEmpty()) {
                    System.out.println("Score cannot be empty. Skipping this line " + lineNo + " from the maps file.");
                    continue;
                }
                if (t[5].trim().isEmpty()) {
                    System.out.println("Restricted field cannot be empty. Skipping this line " + lineNo + " from the maps file.");
                    continue;
                }
                
                type = t[2].trim();
                name = t[3].trim();
                // parse score
                try {
                    score = Double.parseDouble(t[4].trim());
                } catch (Exception e) {
                    System.out.println("Invalid score format. Skipping this line " + lineNo + " from the maps file.");
                    continue;
                }
                restricted = t[5].trim();
                
                // Check if place is already occupied
                if (grid[r][c] != null && !(grid[r][c] instanceof EmptySpace)) {
                    System.out.println("Map position [" + r + "," + c + "] already occupied. Exiting program.");
                    System.exit(0);
                }

                Place place = null;
                if ("CAFETERIA".equalsIgnoreCase(type)) {
                    if (score >= 0) {
                        System.out.println("Invalid score format. Skipping this line " + lineNo + " from the maps file.");
                        continue;
                    }
                    place = new Cafeteria(name, score);
                } else if ("EVENT_HALL".equalsIgnoreCase(type)) {
                    place = new EventHall(name, score);
                } else if ("LECTURE_HALL".equalsIgnoreCase(type)) {
                    place = new LectureHall(name, score);
                } else if ("SPORTS_CENTRE".equalsIgnoreCase(type)) {
                    place = new SportsCentre(name, score);
                } else if ("LIBRARY".equalsIgnoreCase(type)) {
                    place = new Library(name, score);
                } else {
                    System.out.println("Invalid place type format. Skipping this line " + lineNo + " from the maps file.");
                    continue;
                }

                if (restricted.equalsIgnoreCase("yes")) {
                    place.setRestricted(true);
                }

                grid[r][c] = place;
            }
        } catch (Exception e) {
            System.out.println("Unable to process file. Exiting program.");
        }
    }

    private void loadEvents() {
        try (BufferedReader br = new BufferedReader(new FileReader(eventsFile))) {
            String line;
            int lineNo = 0;
            int effectiveLineNo = 0;
            while ((line = br.readLine()) != null) {
                lineNo++;
                line = line.trim();
                if (line.isEmpty()) continue; // skip empty lines without counting
                effectiveLineNo++;
                if (effectiveLineNo == 1) continue; // header
                String[] t = line.split(",");
                if (t.length != 9) {
                    System.out.println("Invalid line for events. Skipping this line " + effectiveLineNo + " from the events file.");
                    continue;
                }
                
                // Check for empty fields first
                if (t[2].trim().isEmpty()) {
                    System.out.println("Event type cannot be empty. Skipping this line " + effectiveLineNo + " from the events file.");
                    continue;
                }
                if (t[3].trim().isEmpty()) {
                    System.out.println("Date cannot be empty. Skipping this line " + effectiveLineNo + " from the events file.");
                    continue;
                }
                if (t[4].trim().isEmpty()) {
                    System.out.println("Start time cannot be empty. Skipping this line " + effectiveLineNo + " from the events file.");
                    continue;
                }
                if (t[5].trim().isEmpty()) {
                    System.out.println("End time cannot be empty. Skipping this line " + effectiveLineNo + " from the events file.");
                    continue;
                }
                if (t[6].trim().isEmpty()) {
                    System.out.println("Score cannot be empty. Skipping this line " + effectiveLineNo + " from the events file.");
                    continue;
                }
                if (t[7].trim().isEmpty()) {
                    System.out.println("Event name cannot be empty. Skipping this line " + effectiveLineNo + " from the events file.");
                    continue;
                }
                
                String type = t[2].trim().toLowerCase();
                if (!(type.equals("lecture") || type.equals("seminar") || type.equals("exam"))) {
                    System.out.println("Invalid event type format. Skipping this line " + effectiveLineNo + " from the events file.");
                    continue;
                }
                
                int r, c;
                try {
                    r = Integer.parseInt(t[0].trim());
                    c = Integer.parseInt(t[1].trim());
                } catch (Exception e) {
                    System.out.println("Invalid row format. Skipping this line " + effectiveLineNo + " from the events file.");
                    continue;
                }
                if (r <= 0 || r >= rows - 1) {
                    System.out.println("Invalid row format. Skipping this line " + effectiveLineNo + " from the events file.");
                    continue;
                }
                if (c <= 0 || c >= cols - 1) {
                    System.out.println("Invalid column format. Skipping this line " + effectiveLineNo + " from the events file.");
                    continue;
                }
                
                String date = t[3].trim();
                if (!isValidDate(date)) {
                    System.out.println("Invalid date format. Skipping this line " + effectiveLineNo + " from the events file.");
                    continue;
                }

                String startTime = t[4].trim();
                if (!isValidTime(startTime)) {
                    System.out.println("Invalid start time format. Skipping this line " + effectiveLineNo + " from the events file.");
                    continue;
                }

                String endTime = t[5].trim();
                if (!isValidTime(endTime)) {
                    System.out.println("Invalid end time format. Skipping this line " + effectiveLineNo + " from the events file.");
                    continue;
                }

                double score = 0;
                try {
                    score = Double.parseDouble(t[6].trim());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid score format. Skipping this line " + effectiveLineNo + " from the events file.");
                    continue;
                }
                
                String eventName = t[7].trim();
                String speaker = t[8].trim();

                String key = r + "," + c;
                Place p = grid[r][c];

                if (p == null) {
                    System.out.println("No valid place at given location. Exiting program.");
                    System.exit(0);
                }

                if (p.isRestricted()) {
                    continue; // Skip restricted places silently
                }

                // Check if place can host events
                if (!p.isEventVenue()) {
                    System.out.println("Event cannot be added to the place. Exiting program.");
                    System.exit(0);
                }

                // Check event type allowed at this venue
                if (!p.allowsEventType(type)) {
                    System.out.println("Event cannot be added to the place. Exiting program.");
                    System.exit(0);
                }

                // assign real event id and store with EventRec
                int nextId = placeEventIdCounter.getOrDefault(key, 0) + 1;
                placeEventIdCounter.put(key, nextId);
                List<EventRec> list = eventsByPos.computeIfAbsent(key, k -> new ArrayList<>());
                list.add(new EventRec(nextId, type, date, startTime, endTime, score, eventName, speaker));

                // also attach to Place for schedule printing
                if (type.equals("lecture")) {
                    p.addEvent(new Lecture(nextId, date, startTime, endTime, score, eventName, speaker));
                } else if (type.equals("seminar")) {
                    p.addEvent(new Seminar(nextId, date, startTime, endTime, score, eventName, speaker));
                } else {
                    p.addEvent(new Exam(nextId, date, startTime, endTime, score, eventName));
                }
            }
        } catch (Exception e) {
            System.out.println("Unable to process file. Exiting program.");
        }
        markEmptyEventHalls();
    }


    private boolean isValidTime(String time) {
        if (time.length() != 5) {
            return false;
        }
        String[] t = time.split(":");
        if (t.length != 2) {
            return false;
        }
        try {
            int hour = Integer.parseInt(t[0].trim());
            int minute = Integer.parseInt(t[1].trim());
            if (hour >= 0 && hour <= 23 && minute >= 0 && minute <= 59) {
                return true;
            }
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidDate(String date) {
        if (date.length() != 10) {
            return false;
        }
        String[] splits = date.split("-");
        if (splits.length != 3) {
            return false;
        }
        if (splits[0].length() != 4 || splits[1].length() != 2 || splits[2].length() != 2) {
            return false;
        }
        try {
            int year = Integer.parseInt(splits[0]);
            int month = Integer.parseInt(splits[1]);
            int day = Integer.parseInt(splits[2]);
            if (year < 0 || month < 0 || day < 0) {
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void printSchedule() {
        printMap();
        while (true) {
            System.out.print("Enter row column indices in row,col format: ");
            try {
                String s = Constants.keyboard.nextLine().trim();
                String[] rc = s.split(",");
                int r = Integer.parseInt(rc[0].trim());
                int c = Integer.parseInt(rc[1].trim());
                Place p = grid[r][c];
                if (p != null) {
                    p.printSchedule();
                    if (!p.shouldRepeatScheduleSelection()) {
                        break;
                    }
                } else {
                    System.out.println("This place does not have a schedule. Select a cafeteria, sport centre, lecture hall or event hall.");
                }
            } catch (java.util.NoSuchElementException e) {
                break;
            } catch (Exception ignore) {
                System.out.println("This place does not have a schedule. Select a cafeteria, sport centre, lecture hall or event hall.");
            }
        }
    }

    private void showEventsAt(int r, int c) {
        String key = r + "," + c;
        List<EventRec> evs = eventsByPos.getOrDefault(key, Collections.emptyList());
        if (evs.isEmpty()) {
            System.out.println("Nothing happening here.");
            return;
        }
        while (true) {
            Place pPlace = grid[r][c];
            System.out.println(pPlace.eventSelectionPrompt());
            for (EventRec e : evs) {
                String head = capitalize(e.type) + ": " + e.id + ", ";
                if (e.type.equals("lecture")) {
                    System.out.print(head + "Course: " + e.name + " Lecturer: " + e.speaker + "  ");
                } else if (e.type.equals("seminar")) {
                    System.out.print(head + "Name: " + e.name + " Speakers: " + e.speaker.replace("#", ", ") + "  ");
                } else {
                    System.out.print(head + "Course: " + e.name + "  ");
                }
                System.out.println("Date: " + e.date + "  Time: " + e.start + " to " + e.end + "  Score: " + String.format("%.2f", e.score));
            }
            System.out.print("> ");
            String choice;
            try {
                choice = Constants.keyboard.nextLine().trim();
                int id = Integer.parseInt(choice);
                EventRec selected = null;
                for (EventRec e : evs) {
                    if (e.id == id) {
                        selected = e;
                        break;
                    }
                }
                if (selected == null) {
                    System.out.println("Incorrect event id. Please try again.");
                    continue;
                }
                evs.remove(selected);
                System.out.println("You attended event " + capitalize(selected.type) + " : \"" + selected.name + "\" and earned " + String.format("%.2f", selected.score) + " points.");
                Place p = grid[r][c];
                double combined = selected.score + (p != null ? p.getPositionScore() : 0.0);
                addRecord(p != null ? p.getPlaceName() : "Event Hall", selected.name, selected.date, selected.start + "-" + selected.end, combined);
                p.removeEventById(id);
                if (evs.isEmpty()) {
                    System.out.println("No more events here.");
                    grid[r][c] = new EmptyEventHall();
                    break;
                } else {
                    System.out.println("Do you want to visit other events? Press Y for Yes or any other key for No.");
                    try {
                        if (Constants.keyboard.nextLine().trim().equalsIgnoreCase("Y")) {
                            continue;
                        } else {
                            break;
                        }
                    } catch (java.util.NoSuchElementException e) {
                        break;
                    }
                }
            } catch (java.util.NoSuchElementException e) {
                break;
            } catch (Exception ignore) {
                System.out.println("Incorrect event id. Please try again.");
            }
        }
    }

    private void visitCampus() {
        boolean visiting = true;
        while (visiting) {
            printMap();
            Messages.printMovementOptions();
            String cmd;
            try {
                cmd = Constants.keyboard.nextLine().trim().toUpperCase();
                if (cmd.equals("Q")) {
                    System.out.println("Session paused.");
                    break;
                }
            } catch (java.util.NoSuchElementException e) {
                System.out.println("Session paused.");
                break;
            }
            int nr = student.getRow(), nc = student.getCol();
            if (cmd.equals("U")) nr--;
            else if (cmd.equals("D")) nr++;
            else if (cmd.equals("L")) nc--;
            else if (cmd.equals("R")) nc++;
            else {
                System.out.println("Invalid direction to move.");
                continue;
            }
            // Boundary and wall blocking
            if (nr < 0 || nr >= rows || nc < 0 || nc >= cols || grid[nr][nc] instanceof Boundary) {
                System.out.println("You have hit the edge of the map.");
                student.hitBoundary();
                continue;
            }
            // Restricted area blocking
            if (grid[nr][nc] != null && grid[nr][nc].isRestricted()) {
                System.out.println("You cannot enter that area.");
                student.hitBoundary();
                continue;
            }
            // Mark previous start as visited when leaving start for first time
            if (!startMarked && grid[student.getRow()][student.getCol()] instanceof EmptySpace) {
                // Create a special place to mark visited start position
                grid[student.getRow()][student.getCol()] = new VisitedStart();
                startMarked = true;
            }
            student.moveTo(nr, nc);
            Place currentPlace = grid[nr][nc];
            String key = nr + "," + nc;

            String msg = currentPlace.enterMessage();
            if (msg != null) {
                System.out.println(msg);
            }
            if (currentPlace.triggersEventSelection()) {
                showEventsAt(nr, nc);
            } else if (currentPlace.isScorableOnEnter() && !visitedNonEventPlaces.contains(key)) {
                addRecord(currentPlace.getPlaceName(), null, "-", "-", currentPlace.getPositionScore());
                visitedNonEventPlaces.add(key);
                currentPlace.setVisited(true);
            } else if (currentPlace instanceof EmptyEventHall && !visitedNonEventPlaces.contains(key)) {
                // Mark empty event halls as visited when entered
                visitedNonEventPlaces.add(key);
                currentPlace.setVisited(true);
            }

            // Auto end check: end immediately once completion is achieved
            if (isSessionComplete()) {
                System.out.println("No more places left to visit on campus. Please visit another time.");
                System.out.println("Goodbye for now. Visit Again.");
                // Persist scores and update events per spec
                writeBackUnattendedEvents();
                exportSessionScores();
                autoEnded = true;
                return;
            }

        }
    }

    // Check if all non-event places visited and all events attended
    private boolean isSessionComplete() {
        // Check if all events have been attended
        boolean allEventsVisited = true;
        for (List<EventRec> lst : eventsByPos.values()) {
            if (lst != null && !lst.isEmpty()) {
                allEventsVisited = false;
                break;
            }
        }
        // Count places that must be visited at least once: scorable + original empty event halls
        int requiredVisitTotal = 0;
        int visitedCount = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Place p = grid[i][j];
                if (p == null || p.isRestricted()) continue;
                String key = i + "," + j;
                boolean mustVisit = p.isScorableOnEnter() || originalEmptyEventHalls.contains(key);
                if (mustVisit) {
                    requiredVisitTotal++;
                    if (p.isVisited()) visitedCount++;
                }
            }
        }
        boolean allPlacesVisited = visitedCount >= requiredVisitTotal;
        return allEventsVisited && allPlacesVisited;
    }

    private void bookPlace() {
        printMap();
        boolean continueBooking = true;
        while (continueBooking) {
            System.out.print("Enter row column indices in row,col format: ");
            try {
                String s = Constants.keyboard.nextLine().trim();
                if (s.isEmpty()) continue;

                String[] rc = s.split(",");
                int r = Integer.parseInt(rc[0].trim());
                int c = Integer.parseInt(rc[1].trim());
                Place p = grid[r][c];

                if (p != null && p.isBookable()) {
                    System.out.println("Do you want to book this place? Press Y for Yes or any other key for No.");
                    String d = Constants.keyboard.nextLine().trim();
                    if (d.equalsIgnoreCase("Y")) {
                        if (p.book()) {
                            String msg = p.bookingSuccessMessage();
                            if (msg != null) System.out.println(msg);
                        }
                    }
                    continueBooking = false;
                } else {
                    System.out.println("Place cannot be booked. Select a sports centre or event hall.");
                    // keep prompting
                }
            } catch (java.util.NoSuchElementException e) {
                break;
            } catch (Exception ignore) {
                System.out.println("Place cannot be booked. Select a sports centre or event hall.");
            }
        }
    }

    private void printScoreHistory() {
        // Print header and records
        if (records.isEmpty()) {
            System.out.println("No scores yet.");
            return;
        }
        System.out.printf(Constants.SCORE_HEADER_FORMATTER,
                "Map Name", "Session Id", "Place Name", "Event Name",
                "Date", "Time", "Moves", "Hits", "Score");
        System.out.println(Constants.SCORE_HEADER_LINE);
        for (Record r : records) {
            System.out.print(r.print());
        }
    }

    private void addRecord(String place, String event, String date, String time, double score) {
        records.add(new Record(student.getMapName(), student.getSessionId(), place, event == null ? "-" : event, date, time, student.getMoves(), student.getHits(), score));
    }

    private void writeBackUnattendedEvents() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(eventsFile))) {
            pw.println("row_id,col_id,event_type,date,start_time,end_time,score,name,speaker");
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    Place p = grid[i][j];
                    if (p != null) {
                        p.writeEventsToCsv(pw, i, j);
                    }
                }
            }
            pw.flush();
        } catch (Exception ignore) {
        }
    }

    private void printMap() {
        System.out.println("Map Name: " + mapName);
        for (int i = 0; i < rows; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < cols; j++) {
                char ch;
                if (i == student.getRow() && j == student.getCol()) {
                    ch = 'P';
                } else if (grid[i][j] == null) {
                    ch = '#';
                } else {
                    ch = grid[i][j].mapSymbol();
                }
                sb.append(ch).append(' ');
            }
            System.out.println(sb.toString());
        }
    }

    private void printMovementOptions() {
        System.out.println("Enter direction:");
        System.out.println("U - Up.");
        System.out.println("D - Down.");
        System.out.println("L - Left.");
        System.out.println("R - Right.");
        System.out.println("Q - Quit to main menu.");
        System.out.print("> ");
    }

    private void markEmptyEventHalls() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                String key = r + "," + c;
                List<EventRec> evs = eventsByPos.getOrDefault(key, Collections.emptyList());
                Place p = grid[r][c];
                if (evs.isEmpty() && p != null && p.isEventVenue() && !p.isRestricted()) {
                    originalEmptyEventHalls.add(key);
                    grid[r][c] = new EmptyEventHall();
                }
            }
        }
    }

    private void printPreviousSessionHistory() {
        List<Record> all = new ArrayList<>();
        String[] candidates = new String[]{"session_scores.txt", "data/session_scores.txt"};
        boolean found = false;
        for (String p : candidates) {
            try (BufferedReader br = new BufferedReader(new FileReader(p))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) continue;
                    if (line.toLowerCase().startsWith("session_id") || line.toLowerCase().startsWith("map_name")) {
                        continue;
                    }
                    String[] parts = line.split(",");
                    if (parts.length < 9) continue;
                    try {
                        String map = parts[0].trim();
                        int sid = Integer.parseInt(parts[1].trim());
                        String place = parts[2].trim();
                        String event = parts[3].trim();
                        String date = parts[4].trim();
                        String time = parts[5].trim();
                        int mv = Integer.parseInt(parts[6].trim());
                        int ht = Integer.parseInt(parts[7].trim());
                        double sc = Double.parseDouble(parts[8].trim());
                        all.add(new Record(map, sid, place, event, date, time, mv, ht, sc));
                    } catch (Exception ignore) {
                    }
                }
                found = true;
                break;
            } catch (Exception ignore) { /* try next candidate */ }
        }
        if (!found || all.isEmpty()) {
            System.out.println("No session scores available.");
            return;
        }
        Set<Integer> idset = new TreeSet<>();
        for (Record r : all) idset.add(Integer.valueOf(r.backtofile().split(",")[1].trim()));
        System.out.println("Available Session IDs:");
        for (Integer id : idset) {
            System.out.println(" -> " + id);
        }
        System.out.print("Enter session ID to view score history or A to print score of all sessions: ");
        String inp;
        try {
            inp = Constants.keyboard.nextLine().trim();
        } catch (java.util.NoSuchElementException e) {
            return;
        }
        if (inp.equalsIgnoreCase("A")) {
            all.sort(Comparator.comparingInt(r -> Integer.parseInt(r.backtofile().split(",")[1].trim())));
            System.out.printf(Constants.SCORE_HEADER_FORMATTER,
                    "Map Name", "Session Id", "Place Name", "Event Name",
                    "Date", "Time", "Moves", "Hits", "Score");
            System.out.println(Constants.SCORE_HEADER_LINE);
            for (Record r : all) {
                System.out.print(r.print());
            }
            return;
        }
        try {
            int sel = Integer.parseInt(inp);
            if (!idset.contains(sel)) {
                System.out.println("No scores found for session ID: " + sel);
                return;
            }
            System.out.printf(Constants.SCORE_HEADER_FORMATTER,
                    "Map Name", "Session Id", "Place Name", "Event Name",
                    "Date", "Time", "Moves", "Hits", "Score");
            System.out.println(Constants.SCORE_HEADER_LINE);
            for (Record r : all) {
                if (Integer.parseInt(r.backtofile().split(",")[1].trim()) == sel) {
                    System.out.print(r.print());
                }
            }
        } catch (Exception e) {
            System.out.println("No scores found for session ID: " + inp);
        }
    }

    private void exportSessionScores() {
        if (records.isEmpty()) return;
        String[] candidates = new String[]{"data/session_scores.txt", "session_scores.txt"};
        for (String p : candidates) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(p, true))) {
                for (Record r : records) {
                    writer.println(r.backtofile());
                }
                writer.flush();
                break;
            } catch (Exception ignore) {
            }
        }
    }

    private static class EventRec {
        int id;
        String type, date, start, end, name, speaker;
        double score;

        EventRec(int id, String type, String date, String start, String end, double score, String name, String speaker) {
            this.id = id;
            this.type = type;
            this.date = date;
            this.start = start;
            this.end = end;
            this.score = score;
            this.name = name;
            this.speaker = speaker;
        }
    }
}
