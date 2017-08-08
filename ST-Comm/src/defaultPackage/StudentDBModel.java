package defaultPackage;



import java.sql.*;
import java.text.SimpleDateFormat;

public class StudentDBModel extends RegistrantDBModel {// error

    public static boolean authenticate(String name, String password) {
        CallableStatement cstmt = null;
        boolean found = false;
        try {
            cstmt = SQLConnection.getConnection().prepareCall("{call AuthenticateStudent(?,?,?)}");
            cstmt.setString(1, name);
            cstmt.setString(2, password);
            cstmt.registerOutParameter(3, Types.BIT);
            cstmt.execute();
            found = cstmt.getBoolean(3);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return found;
    }

    public static boolean saveAccount(Student obj) {
        CallableStatement cstmt = null;
        boolean saved = false;
        try {
            cstmt = SQLConnection.getConnection().prepareCall("{call saveStudent(?,?,?,?,?,?,?)}");
            cstmt.setString(1, obj.getName());
            cstmt.setString(2, obj.getCountry());
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
            cstmt.setDate(3, java.sql.Date.valueOf(s.format(obj.getBirthdate())));
            cstmt.setString(4, obj.getGender());
            cstmt.setString(5, obj.getMail());
            cstmt.setString(6, obj.getPassword());
            cstmt.registerOutParameter(7, Types.BIT);
            cstmt.execute();
            saved = cstmt.getBoolean(7);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return saved;
    }

    public static boolean saveScore(String name, int score, String gameName) {
        CallableStatement cstmt = null;
        boolean saved = false;
        try {
            cstmt = SQLConnection.getConnection().prepareCall("{call saveScore(?,?,?,?)}");
            cstmt.setString(1, name);
            cstmt.setInt(2, score);
            cstmt.setString(3, gameName);
            cstmt.registerOutParameter(4, Types.BIT);
            cstmt.execute();
            saved = cstmt.getBoolean(4);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return saved;
    }
}
