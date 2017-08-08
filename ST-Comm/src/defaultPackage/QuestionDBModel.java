package defaultPackage;



import java.sql.*;
import java.util.Vector;

public class QuestionDBModel {

    public static Vector<Question> fetchQuestions(String gameName) {
        CallableStatement cstmt = null;

        Vector<Question> questions = new Vector<Question>();
        ResultSet rs = null;
        try {
            cstmt = SQLConnection.getConnection().prepareCall("{call fetchQuestion(?)}");
            cstmt.setString(1, gameName);
            cstmt.execute();
            rs = cstmt.getResultSet();
            while (rs.next()) {
                Vector<String> Choices = new Vector<String>();

                String choice = "";
                String allChoices = rs.getString("Choices");
                for (int i = 0; i < allChoices.length(); i++) {
                    if (allChoices.charAt(i) != ';') {
                        choice += (allChoices.charAt(i) + "");
                    } else {
                        Choices.add(choice);
                        choice = "";
                    }

                }
                Choices.add(choice);

                questions.add(new Question());
                questions.get(questions.size() - 1).setQuestionStatement(rs.getString("QuestionStatement"));
                questions.get(questions.size() - 1).setChoices(Choices);
                questions.get(questions.size() - 1).setCorrectAnswer(rs.getInt("CorrectAnswer"));
                questions.get(questions.size() - 1).setType(QuestionType.valueOf(rs.getString("Type")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    public static boolean saveQuestion(Question question, String gameName) {//check
        CallableStatement cstmt = null;
        String allChoices = "";
        Vector<String> Choices = new Vector<String>(1);
        Choices = question.getChoices();
        for (int i = 0; i < Choices.size(); i++) {
            allChoices += Choices.get(i);
            allChoices += ";";
        }
        boolean saved = false;
        try {
            cstmt = SQLConnection.getConnection().prepareCall("{call saveQuestion(?,?,?,?,?,?)}");
            cstmt.setString(1, question.getQuestionStatement());
            cstmt.setString(2, question.getType().name());
            cstmt.setString(3, allChoices);
            cstmt.setString(4, gameName);
            cstmt.setInt(5, question.getCorrectAnswer());
            cstmt.registerOutParameter(6, Types.BIT);
            cstmt.execute();
            saved = cstmt.getBoolean(6);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return saved;
    }
}
