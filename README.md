Campus Navigator (Java)

A console-based campus navigation and event booking system.  
The program loads a campus map from text files, lets a student move around the map, visit places, attend events, and keeps a score history across sessions.

Core Concepts

Campus Map
A 2D grid map loaded from file. Each cell has a type such as START, OPEN, BOUNDARY, RESTRICTED or PLACE.  
Map symbols used include #, ., X, S and C/L/G/@/E for different place types.

Places
Supported place types include CAFETERIA (C), LIBRARY (L), SPORTS_CENTRE (G), LECTURE or EVENT_HALL (@), and EMPTY (E).  
Each place has a name, a base score, a visited flag and additional information such as menus, facilities or scheduled events.

Events
Three event types are supported: LECTURE, SEMINAR and EXAM.  
Every event includes an ID, date, start time, end time and a score.

Student and Booking
The student is represented as P on the map and can move in four directions, visit places and attend events.  
Some venues such as sports centres and event halls support booking through a Bookable interface.

Score History and Sessions
Each program run starts a new session identified by a session ID.  
The system records moves, boundary hits, places visited, events attended and the accumulated score.  
Session histories are saved to a score file so that past sessions can be reloaded.

Project Structure

CampusNavigator-Java/
  data/           
  events/         
  exception/      
  interfaces/     
  place/          
  utils/          
  CampusMap.java
  CampusNavigatorEngine.java
  Record.java
  ScoreHistory.java
  Student.java
  README.md

How to Run

From the CampusNavigator-Java folder:
javac CampusNavigatorEngine.java
java CampusNavigatorEngine <map-file> <events-file> <session-id>
