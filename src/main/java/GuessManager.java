import java.util.ArrayList;
import java.util.Collections;

public class GuessManager {

    public final static int GUESS_INVALID = 0;
    public final static int GUESS_CORRECT = 1;
    public final static int GUESS_INCORRECT = 2;
    public final static int GUESS_REDUNDANT = 3;

    private String toGuess;
    private ArrayList<Character> guessedChars = new ArrayList<Character>();
    private int numWrongGuesses = 0;

    public GuessManager(String movie){
        toGuess = movie;
    }

    public int getWrongNumGuesses() {
        return numWrongGuesses;
    }

    public ArrayList<Character> getGuessedChars() {
        Collections.sort(guessedChars);
        return guessedChars;
    }

    public int newGuess(char c){
        if(!Character.isLetterOrDigit(c)){
            return GUESS_INVALID;
        }

        if(guessedChars.contains(Character.toLowerCase(c)) || guessedChars.contains(Character.toUpperCase(c))){
            return GUESS_REDUNDANT;
        } else {
            if(toGuess.contains(String.valueOf(Character.toLowerCase(c))) || toGuess.contains(String.valueOf(Character.toUpperCase(c)))){
                guessedChars.add(Character.toLowerCase(c));
                return GUESS_CORRECT;
            } else {
                guessedChars.add(Character.toLowerCase(c));
                numWrongGuesses++;
                return GUESS_INCORRECT;
            }
        }
    }

}
