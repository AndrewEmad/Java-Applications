package defaultPackage;



import java.sql.*;
import java.util.Vector;

public class GameDBModel {

    public static Vector<String> fetchPendingGames() {
        CallableStatement cstmt = null;
        ResultSet rs = null;
        Vector<String> pendingGames = new Vector<String>(1);
        try {
            cstmt = SQLConnection.getConnection().prepareCall("{call fetchPendingGames}");
            cstmt.execute();
            rs = cstmt.getResultSet();
            while (rs.next()) {
                pendingGames.add(rs.getString("GameName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pendingGames;
    }

    public static Vector<String> fetchGames(String categoryName) {
        CallableStatement cstmt = null;
        Vector<String> games = new Vector<String>(1);
        ResultSet rs = null;
        try {
            cstmt = SQLConnection.getConnection().prepareCall("{call fetchGames(?)}");
            cstmt.setString(1, categoryName);
            cstmt.execute();
            rs = cstmt.getResultSet();
            while (rs.next()) {
                games.add(rs.getString("GameName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return games;
    }

    public static Game fetchGame(String gameName) {//check
        CallableStatement cstmt = null;
        Game game = new Game();
        ResultSet rs = null;
        try {
            cstmt = SQLConnection.getConnection().prepareCall("{call fetchGame(?)}");
            cstmt.setString(1, gameName);
            cstmt.execute();
            rs = cstmt.getResultSet();
            while (rs.next()) {
                game.setName(rs.getString("GameName"));
                game.setLevel(rs.getInt("Level"));
                game.setCategory(rs.getString("CategoryName"));
                game.setTeacherName(rs.getString("TeacherName"));
                game.setNumOfQuestions(rs.getInt("NumberOfQuestions"));
                game.setConfirmed(rs.getBoolean("Confirmed"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return game;
    }

    public static Vector<String> fetchCategories() {//check
        CallableStatement cstmt = null;
        Vector<String> categories = new Vector<String>(1);
        ResultSet rs = null;
        try {
            cstmt = SQLConnection.getConnection().prepareCall("{call getCategories}");
            cstmt.execute();
            rs = cstmt.getResultSet();
            while (rs.next()) {
                categories.add(rs.getString("CategoryName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public static boolean setConfirmed(String gameName) {
        CallableStatement cstmt = null;
        boolean saved = false;
        try {
            cstmt = SQLConnection.getConnection().prepareCall("{call setGameConfirmed(?,?)}");
            cstmt.setString(1, gameName);
            cstmt.registerOutParameter(2, Types.BIT);
            cstmt.execute();
            saved = cstmt.getBoolean(2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return saved;
    }

    public static boolean saveGame(Game game) {//check
        CallableStatement cstmt = null;
        boolean saved = false;
        try {
            cstmt = SQLConnection.getConnection().prepareCall("{call saveGame(?,?,?,?,?,?)}");
            cstmt.setString(1, game.getName());
            cstmt.setInt(2, game.getLevel());
            cstmt.setString(3, game.getCategory());
            cstmt.setString(4, game.getTeacherName());
            cstmt.setInt(5, game.getNumOfQuestions());
            cstmt.registerOutParameter(6, Types.BIT);
            cstmt.execute();
            saved = cstmt.getBoolean(6);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return saved;
    }

    public static boolean isUnlocked(String studentName, String gameName) {
        CallableStatement cstmt = null;
        boolean unlocked = false;
        try {
            cstmt = SQLConnection.getConnection().prepareCall("{call isUnlocked(?,?,?)}");
            cstmt.setString(1, studentName);
            cstmt.setString(2, gameName);
            cstmt.registerOutParameter(3, Types.BIT);
            cstmt.execute();
            unlocked = cstmt.getBoolean(3);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return unlocked;
    }
}
