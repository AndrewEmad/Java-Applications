package defaultPackage;



import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class TeacherDBModel extends RegistrantDBModel {

    public static boolean authenticate(String name, String password) {
        CallableStatement cstmt = null;
        boolean found = false;
        try {
            cstmt = SQLConnection.getConnection().prepareCall("{call AuthenticateTeacher(?,?,?)}");
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

    public static boolean saveAccount(Teacher obj) {
        CallableStatement cstmt = null;
        boolean saved = false;
        try {
            cstmt = SQLConnection.getConnection().prepareCall("{call saveTeacher(?,?,?,?,?,?,?)}");
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

    public static Vector<String> fetchRequests() {//check
        CallableStatement cstmt = null;
        Vector<String> requests = new Vector<String>(1);
        ResultSet rs = null;
        try {
            cstmt = SQLConnection.getConnection().prepareCall("{call fetchTeacherRequests}");
            cstmt.execute();
            rs = cstmt.getResultSet();
            while (rs.next()) {
                requests.add(rs.getString("TeacherName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public static boolean setVerified(String E_mail) {
        CallableStatement cstmt = null;
        boolean saved = false;
        try {
            cstmt = SQLConnection.getConnection().prepareCall("{call setVerified(?,?)}");
            cstmt.setString(1, E_mail);
            cstmt.registerOutParameter(2, Types.BIT);
            cstmt.execute();
            saved = cstmt.getBoolean(2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return saved;
    }
}
