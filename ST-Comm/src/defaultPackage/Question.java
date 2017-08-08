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

public class Question {

    private Vector<String> choices;
    private QuestionType type;
    private int correctAnswer;
    private String questionStatement;

    public Question() {
        choices = new Vector<String>();
        type = QuestionType.MCQ;
        correctAnswer = 0;
        questionStatement = "";
    }

    public void setChoices(Vector<String> choices) {
        this.choices = choices;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setQuestionStatement(String questionStatement) {
        this.questionStatement = questionStatement;
    }

    public Vector<String> getChoices() {
        return choices;
    }

    public QuestionType getType() {
        return type;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public String getQuestionStatement() {
        return questionStatement;
    }

    public void setInfo(Vector<String> choices, QuestionType type, int correctAnswer, String questionStatement) throws Exception {
        if (choices == null) {
            throw new Exception("Question: Null exception!!");
        }
        if (correctAnswer >= choices.size()) {
            throw new Exception("Question: Bad index of correct answer!!");
        }
        this.choices.clear(); //we do not guarantee that the vector parameter and 
        //the vector attribute are of the same size
        for (int i = 0; i < choices.size(); i++) {
            this.choices.addElement(choices.get(i));
        }
        this.setType(type);
        this.setCorrectAnswer(correctAnswer);
        this.setQuestionStatement(questionStatement);
    }
}
