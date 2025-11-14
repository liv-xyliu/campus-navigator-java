import utils.Messages;

import java.io.File;

public class CampusNavigatorEngine {
    private static final int REQUIRED_ARGS = 5;

    public static void main(String[] args) {
        if (!validateArgs(args)) return;
        int rows = Integer.parseInt(args[0]);
        int cols = Integer.parseInt(args[1]);
        int sessionId = Integer.parseInt(args[2]);
        String mapFile = args[3];
        String eventsFile = args[4];
        mapFile = mapFile.replace("[", "").replace("]", "");
        eventsFile = eventsFile.replace("[", "").replace("]", "");
        String mapName = extractMapName(mapFile);

        if (!new File(mapFile).exists() || !new File(eventsFile).exists()) {
            System.out.println("Unable to process file. Exiting program.");
            return;
        }

        Messages.printWelcome();
        CampusMap navigator = new CampusMap(rows, cols, sessionId, mapName, mapFile, eventsFile);
        navigator.run();
    }

    private static boolean validateArgs(String[] args) {
        if (args == null || args.length != REQUIRED_ARGS) {
            System.out.println("Invalid number of Command Line Arguments. Usage: java CampusNavigatorEngine <rows> <cols> <session id> <location file> <events file>");
            return false;
        }
        try {
            int rows = Integer.parseInt(args[0]);
            int cols = Integer.parseInt(args[1]);
            if (rows < 4 || cols < 4) {
                System.out.println("Error: Rows and columns must be at least 4 to allow proper map layout.");
                return false;
            }
            Integer.parseInt(args[2]);
        } catch (Exception e) {
            System.out.println("Unable to process file. Exiting program.");
            return false;
        }
        return true;
    }

    private static String extractMapName(String mapFilePath) {
        String name = new File(mapFilePath).getName();
        int dot = name.lastIndexOf('.');
        return dot > 0 ? name.substring(0, dot) : name;
    }
}


