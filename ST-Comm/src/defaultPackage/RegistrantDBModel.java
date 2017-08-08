package defaultPackage;



import java.sql.*;

public class RegistrantDBModel {

    public static boolean isBlackListed(String E_mail) {
        CallableStatement cstmt = null;
        boolean blackListed = false;
        try {
            cstmt = SQLConnection.getConnection().prepareCall("{call isBlacklisted(?,?)}");
            cstmt.setString(1, E_mail);
            cstmt.registerOutParameter(2, Types.BIT);
            cstmt.execute();
            blackListed = cstmt.getBoolean(2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return blackListed;
    }

    public static boolean exists(String name, String E_mail) {
        CallableStatement cstmt = null;
        boolean found = false;
        try {
            // search in student table
            cstmt = SQLConnection.getConnection().prepareCall("{call studentExists(?,?,?)}");
            cstmt.setString(1, name);
            cstmt.setString(2, E_mail);
            cstmt.registerOutParameter(3, Types.BIT);
            cstmt.execute();
            found = cstmt.getBoolean(3);
            if (found) {
                return true;
            }
            // search in teacher table
            cstmt = SQLConnection.getConnection().prepareCall("{call teacherExists(?,?,?)}");
            cstmt.setString(1, name);
            cstmt.setString(2, E_mail);
            cstmt.registerOutParameter(3, Types.BIT);
            cstmt.execute();
            found = cstmt.getBoolean(3);
            if (found) {
                return true;
            }
            //search in admin table
            cstmt = SQLConnection.getConnection().prepareCall("{call adminExists(?,?,?)}");
            cstmt.setString(1, name);
            cstmt.setString(2, E_mail);
            cstmt.registerOutParameter(3, Types.BIT);
            cstmt.execute();
            found = cstmt.getBoolean(3);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return found;
    }

    public static boolean authenticate(String name, String password) {
        CallableStatement cstmt = null;
        boolean found = false;
        try {
            cstmt = SQLConnection.getConnection().prepareCall("{call AuthenticateAdmin(?,?,?)}");
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

}
