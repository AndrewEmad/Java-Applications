package defaultPackage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Andrew
 */
import java.util.Vector;
import UI.*;
public class GameController {

    public static Vector<String> getGames(String categoryName) {
        return GameDBModel.fetchGames(categoryName);
    }

    public static int playGame(String gameName) throws Exception {
        int score = 0, userAnswer;
        Game game = GameDBModel.fetchGame(gameName);
        if (game == null) {
            throw new Exception("Game not found !");
        }
        Vector<Question> questions = QuestionDBModel.fetchQuestions(gameName);
        if (questions == null) {
            throw new Exception("The game has no questions !");
        }
        game.setQuestions(questions);
        for (int i = 0; i < game.getNumOfQuestions(); i++) {
            userAnswer = Home.displayQuestion(game.getNextQuestion(), game.getNumOfQuestions(), i);
            if (userAnswer < 0) { //user cancelled the game
                return score;
            }
            if (game.getAnswer() == userAnswer) {
                score++;
            }
        }
        return score;
    }

    public static Vector<String> getPendingGames() {
        return GameDBModel.fetchPendingGames();
    }

    public static boolean confirmGame(String gameName) {
        return GameDBModel.setConfirmed(gameName);
    }

    public static boolean createGame(String name, int level, String category, String teacherName, boolean confirmed, Vector<Question> questions) throws Exception {
        Game game = new Game();
        boolean succeeded;
        game.setInfo(name, level, category, teacherName, confirmed, questions);
        succeeded = GameDBModel.saveGame(game);
        if (succeeded == false) {
            return false;
        }
        for (Question question : questions) {
            succeeded = QuestionDBModel.saveQuestion(question, name);
            if (succeeded == false) {
                return false;
            }
        }
        return true;
    }

    public static boolean isUnlocked(String gameName, String studentName) {
        return GameDBModel.isUnlocked(studentName, gameName);
    }

    public static Vector<String> getCategories() {
        return GameDBModel.fetchCategories();
    }
}
