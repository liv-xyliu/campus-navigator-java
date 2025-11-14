package utils;

import java.util.Scanner;

public final class Constants {

    public static final char PLAYER_SYMBOL = 'P';
    public static final char CAFETERIA_SYMBOL = 'C';   // Used when PlaceType is EVENT but no events remain
    public static final char LIBRARY_SYMBOL = 'L';   // Used when PlaceType is EVENT but no events remain
    public static final char EMPTY_EVENT_SYMBOL = 'E';   // Used when PlaceType is EVENT but no events remain
    public static final char DEFAULT_SYMBOL = ' ';
    public static final String SCORE_SUMMARY_FORMATTER = "| %-15s | %-15s | %-30s | %-30s | %-10s | %-12s | %-6d | %-7d | %7.2f |\n";
    public static final String SCORE_HEADER_FORMATTER = "| %-15s | %-15s | %-30s | %-30s | %-10s | %-12s | %-6s | %-7s | %-7s |\n";
    public static final String SCORE_HEADER_LINE = "|" + "=".repeat(17) + "|" + "=".repeat(17) + "|" + "=".repeat(32) + "|" + "=".repeat(32) + "|" + "=".repeat(12) + "|" + "=".repeat(14) + "|" + "=".repeat(8) + "|" + "=".repeat(9) + "|" + "=".repeat(9) + "|";


    public static final String SCHEDULE_FORMAT_LINE = "-".repeat(66);
    public static final String EVENT_SCHEDULE_FORMATTER = "| %-7s | %10s | %5s-%5s | %25s |";
    public static final String MENU_FORMATTER = "| %10s | %6s |";
    public static final String MENU_FORMAT_LINE = "-".repeat(23);
    public static final String SPORTCENTRE_FORMAT_LINE = "-".repeat(23);
    public static final String SPORTCENTRE_FORMATTER = "| %-19s |";


    public static final Scanner keyboard = new Scanner(System.in);

}
