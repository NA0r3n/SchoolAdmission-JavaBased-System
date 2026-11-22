import java.io.BufferedReader;

/**
 * Strict console-input helpers. Each method keeps asking until the answer is valid,
 * so a typo can never be mistaken for a real choice.
 */
class Checker {

    // Strict yes/no prompt used for confirmations (submit application, delete record).
    // Accepts y / yes / n / no in any case; anything else re-prompts.
    public static boolean confirm(BufferedReader input, String question) throws Exception {
        while (true) {
            System.out.print(question);
            String line = input.readLine();
            String answer = (line == null) ? "" : line.trim();

            if (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes")) {
                return true;
            }
            if (answer.equalsIgnoreCase("n") || answer.equalsIgnoreCase("no")) {
                return false;
            }
            System.out.println("  [!] Please enter y or n.");
        }
    }

    // Method to validate true/false input
    public static boolean getBooleanInput(BufferedReader input, String question) throws Exception {

        while (true) {

            System.out.print(question);

            String answer = input.readLine().trim();

            if (answer.equalsIgnoreCase("true")) {
                return true;
            }

            if (answer.equalsIgnoreCase("false")) {
                return false;
            }

            System.out.println("Invalid input! Please enter only 'true' or 'false'.");
        }
    }
}
