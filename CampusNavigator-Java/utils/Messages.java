package utils;

public final class Messages {

    public static void printWelcome() {
        System.out.println("Welcome to Campus Navigator.");
    }

    public static void printMainMenuCommands() {
        System.out.println("\n=== Campus Navigator ===");
        System.out.println("1. Visit the campus.");
        System.out.println("2. Print schedule.");
        System.out.println("3. Book a place.");
        System.out.println("4. View score history.");
        System.out.println("5. View score history from the previous session.");
        System.out.println("6. Abandon the session and exit.");
        System.out.print("> ");
    }

    public static void printMovementOptions() {
        System.out.println("Enter direction:");
        System.out.println("U - Up.");
        System.out.println("D - Down.");
        System.out.println("L - Left.");
        System.out.println("R - Right.");
        System.out.println("Q - Quit to main menu.");
        System.out.print("> ");
    }

}
