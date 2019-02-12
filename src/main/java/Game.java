import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Game {

    private static final int MODE_UNDEFINED = 0;
    private static final int MODE_HOLLYWOOD = 1;
    private static final int MODE_BOLLYWOOD = 2;

    public static final int MAX_WRONG_GUESSES = 5;

    private Scanner scanner;
    private int mode;
    private String movieTitle;
    private String incompleteTitle;
    private GuessManager guessManager;

    public void init(){
        scanner = new Scanner(System.in);
        System.out.println("Welcome to Hollywood - Bollywood!");
        System.out.println("Choose your game mode: Hollywood / Bollywood");
        mode = tellMode(scanner.nextLine());
        while(mode == 0){
            mode = tellMode(scanner.nextLine());
        }
        movieTitle = getRandomMovie(mode);
        guessManager = new GuessManager(movieTitle);
        incompleteTitle = getStringToGuess(movieTitle).trim();
        //System.out.println(toMovieFormat(incompleteTitle) + " (" +movieTitle+ ")");
        System.out.println(toMovieFormat(incompleteTitle));
    }

    public void play(){
        while(guessManager.getWrongNumGuesses() != MAX_WRONG_GUESSES && !isCompletelyGuessed(incompleteTitle, movieTitle)){
            String guess = scanner.nextLine();
            if(guess.length() != 1){
                System.out.println("Please enter one character at a time");
                continue;
            }
            char g = guess.charAt(0);
            int guessCode = guessManager.newGuess(g);
            switch(guessCode){
                case GuessManager.GUESS_INVALID:
                    System.out.println("Please enter a valid letter or number, no symbols or special characters are allowed.");
                    break;
                case GuessManager.GUESS_CORRECT:
                    incompleteTitle = addCharacter(incompleteTitle, g);
                    break;
                case GuessManager.GUESS_INCORRECT:
                    System.out.println("Invalid Guess, please try again.");
                    break;
                case GuessManager.GUESS_REDUNDANT:
                    System.out.println("You've already guessed this letter/number, please try again.");
                    break;
            }
            //System.out.println(toMovieFormat(incompleteTitle) + " (" + movieTitle +")");
            System.out.println(toMovieFormat(incompleteTitle));
            System.out.println("Wrong Guesses: " + guessManager.getWrongNumGuesses() + "/" + MAX_WRONG_GUESSES + " | Guesses so far: " +guessManager.getGuessedChars());
        }

        if(guessManager.getWrongNumGuesses() != MAX_WRONG_GUESSES && incompleteTitle.equalsIgnoreCase(movieTitle)){
            System.out.println("Congrats, you got the movie right!");
        } else {
            System.out.println("You lost, Try again?");

        }
    }

    private boolean isCompletelyGuessed(String incompleteTitle, String movieTitle){
        String temp = incompleteTitle.replaceAll("[|]", " ");
        return incompleteTitle.equalsIgnoreCase(movieTitle);
    }

    private int tellMode(String s){
        //if no input - undef
        if(s.length() < 1){
            return MODE_UNDEFINED;
        }

        //if 1 char input
        if(s.length() == 1){
            if(s.equalsIgnoreCase("h")){
                return MODE_HOLLYWOOD;
            } else if(s.equalsIgnoreCase("b")){
                return MODE_BOLLYWOOD;
            } else {
                return MODE_UNDEFINED;
            }
        }

        s = s.toLowerCase();
        if(s.contains("holly")){
            return MODE_HOLLYWOOD;
        } else if(s.contains("bolly")){
            return MODE_BOLLYWOOD;
        }
        System.out.println("Invalid Input, Please try again.");
        return MODE_UNDEFINED;
    }

    private String getRandomMovie(int mode){
        if(mode == MODE_UNDEFINED){
            System.out.println("Invalid Input, Please try again.");
            return null;
        }

        Random random = new Random();
        APIHandler apiHandler = new APIHandler();
        String response = "";

        try {
            response = apiHandler.getResponse(mode);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> movieList = apiHandler.getParsedMovieList(response);
        int randomIndex = random.nextInt(movieList.size());

        if(mode == MODE_HOLLYWOOD){
            System.out.println("You've chosen Hollywood, here is your movie:");
        } else {
            System.out.println("You've chosen Bollywood, here is your movie:");
        }
        return movieList.get(randomIndex);
    }

    private String getStringToGuess(String movieTitle){
        if(movieTitle == null){
            return null;
        }
        movieTitle = movieTitle.replaceAll("\\d", "□");
        movieTitle = movieTitle.replaceAll("\\s", "|");
        movieTitle = movieTitle.replaceAll("\\w", "_");
        return movieTitle;
    }

    private String toMovieFormat(String movieTitle){
        StringBuilder builder = new StringBuilder();
        for(char c: movieTitle.toCharArray()){
            builder.append(c).append(" ");
        }
        return builder.toString();
    }

    private String addCharacter(String incompleteMovieTitle, char c){
        if(incompleteMovieTitle.contains(Character.toString(c))){
            return incompleteMovieTitle;
        }

        char[] incompleteMovieTitleArray = incompleteMovieTitle.toCharArray();
        for(int i = 0; i < movieTitle.length(); i++){
            if(Character.isUpperCase(movieTitle.charAt(i))){
                if(movieTitle.charAt(i) == Character.toUpperCase(c)){
                    incompleteMovieTitleArray[i] = Character.toUpperCase(c);
                }
            } else if (Character.isLowerCase(movieTitle.charAt(i))){
                if(movieTitle.charAt(i) == Character.toLowerCase(c)){
                    incompleteMovieTitleArray[i] = Character.toLowerCase(c);
                }
                if(movieTitle.charAt(i) == c){
                    incompleteMovieTitleArray[i] = c;
                }

            } else if(movieTitle.charAt(i) == c){
                incompleteMovieTitleArray[i] = c;
            }
        }
        return String.valueOf(incompleteMovieTitleArray);
    }
}
