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

public class Game {

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * @param numOfQuestions the numOfQuestions to set
     */
    public void setNumOfQuestions(int numOfQuestions) {
        this.numOfQuestions = numOfQuestions;
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return the teacherName
     */
    public String getTeacherName() {
        return teacherName;
    }

    /**
     * @param teacherName the teacherName to set
     */
    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    /**
     * @return the confirmed
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    /**
     * @param confirmed the confirmed to set
     */
    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    /**
     * @param nextQuestion the nextQuestion to set
     */
    public void setNextQuestion(int nextQuestion) {
        this.nextQuestion = nextQuestion;
    }

    /**
     * @return the questions
     */
    public Vector<Question> getQuestions() {
        return questions;
    }

    /**
     * @param questions the questions to set
     */
    public void setQuestions(Vector<Question> questions) {
        this.questions = questions;
    }

    private String name;
    private int level;
    private int numOfQuestions;
    private String category;
    private String teacherName;
    private boolean confirmed;
    private int nextQuestion = 0;
    private Vector<Question> questions = new Vector<Question>();

    public int getNumOfQuestions() {
        return numOfQuestions;
    }

    public void setInfo(String name, int level, String category, String teacherName, boolean confirmed, Vector<Question> questions) throws Exception {
        if (questions == null) {
            throw new Exception("Questions Cannot be Null !");
        }
        this.getQuestions().clear();//we do not guarantee that the vector parameter and 
        //the vector attribute are of the same size
        this.setName(name);
        this.setLevel(level);
        this.setNumOfQuestions(questions.size());
        this.setCategory(category);
        this.setTeacherName(teacherName);
        this.setConfirmed(confirmed);
        for (int i = 0; i < questions.size(); i++) {
            this.getQuestions().addElement(questions.get(i));
        }
    }

    public Question getNextQuestion() throws Exception {
        if (getQuestions().size() == 0) {
            throw new Exception("The game has no questions !");
        }
        Question question = getQuestions().get(nextQuestion);
        nextQuestion = (nextQuestion + 1) % numOfQuestions; //cannot exceed numOfQuestions
        return question;
    }

    public int getAnswer() throws Exception {
        if (getQuestions().size() == 0) {
            throw new Exception("The game has no questions !");
        }
        int currentQuestion = this.nextQuestion - 1;
        if (currentQuestion < 0) {
            currentQuestion = numOfQuestions - 1;
        }
        return getQuestions().get(currentQuestion).getCorrectAnswer();
    }

    public void pushQuestion(Question question) {
        getQuestions().addElement(question);
        numOfQuestions++;
    }
}
