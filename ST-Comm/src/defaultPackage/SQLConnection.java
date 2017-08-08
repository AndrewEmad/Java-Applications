package defaultPackage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.sql.*;

/**
 *
 * @author Andrew
 */
public class SQLConnection {

    public static Connection getConnection() throws SQLException {
        DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
        String dbURL = "jdbc:sqlserver://ANDREW;databaseName=ST-Comm;integratedSecurity=true;";
        Connection conn = DriverManager.getConnection(dbURL);
        return conn;
    }
}
