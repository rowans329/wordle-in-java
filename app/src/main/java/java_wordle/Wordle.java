package java_wordle;

import java.io.*;
import java.util.*;

public class Wordle {
    public static List<String> loadFile(String path) {
        List<String> lines = new ArrayList<>();

        try {
            File file = new File(path);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                lines.add(reader.nextLine());
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occured");
        }

        return lines;
    }

    public static void main(String[] args) {
        final String ANSI_GREEN = "\u001B[32m";
        final String ANSI_YELLOW = "\u001B[33m";
        final String ANSI_RED = "\u001B[31m";
        final String ANSI_RESET = "\u001B[0m";

        String allowedWordsPath = "words/allowed_words.txt";
        String possibleWordsPath = "words/possible_words.txt";

        List<String> allowedWords = loadFile(allowedWordsPath);
        List<String> possibleWords = loadFile(possibleWordsPath);

        Random rand = new Random();
        Scanner scanner = new Scanner(System.in);

        boolean done = false;

        while (!done) {
            String word = possibleWords.get(rand.nextInt(possibleWords.size()));

            boolean guessed = false;

            for (int i=1; i < 7; i++) {
                String guess = "";
                boolean validGuess = false;

                while (!validGuess) {
                    System.out.print(i + " ");

                    guess = scanner.nextLine().toLowerCase();

                    if (guess.length() != 5) {
                        System.out.println(ANSI_RED + "Invalid word: must be 5 letters" + ANSI_RESET);
                        continue;
                    }

                    boolean valid = true;

                    for (int j=0; j < guess.length(); j++) {
                        if (!Character.isAlphabetic(guess.charAt(j))) {
                            System.out.println(ANSI_RED + "Invalid word: contains invalid character '" + guess.charAt(j) + "'" + ANSI_RESET);
                            valid = false;
                            break;
                        }
                    }

                    if (!valid) {
                        continue;
                    }

                    if (!allowedWords.contains(guess)) {
                        System.out.println(ANSI_RED + "Word not found in wordlist" + ANSI_RESET);
                        continue;
                    }
                    
                    validGuess = true;
                }

                for (int k=0; k < 5; k++) {
                    char wordChar = word.charAt(k);
                    char guessChar = guess.charAt(k);
                    String color = "";
                    if (guessChar == wordChar) {
                        color = ANSI_GREEN;
                    } else if (word.contains(guessChar + "")) {
                        color = ANSI_YELLOW;
                    }
                    System.out.print(color + Character.toUpperCase(guessChar) + ANSI_RESET + " ");
                }

                System.out.println();

                if (guess.equals(word)) {
                    guessed = true;

                    System.out.print(ANSI_GREEN + "You won! Play again? [y/n] " + ANSI_RESET);
                    if (!scanner.nextLine().equalsIgnoreCase("y")) {
                        done = true;
                    }

                    break;
                }
            }

            if (!guessed) {
                System.out.print("The word was " + word + ". Try again? [y/n] ");
                if (!scanner.nextLine().equalsIgnoreCase("y")) {
                    done = true;
                }
            }
        }

        scanner.close();
    }
}
