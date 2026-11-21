import java.io.BufferedReader;

class Checker {

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