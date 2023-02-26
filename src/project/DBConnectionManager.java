/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Denna klass används av DAOImplementation för att returnera en databasconnection.
 * Syftet är att 
 * kopierad från https://stackoverflow.com/questions/10915375/create-a-class-to-connect-to-any-database-using-jdbc#10916633
 */

public class DBConnectionManager {
    private static String url = "jdbc:mysql://localhost:1527/project";    
    private static String driverName = "com.mysql.jdbc.Driver";   // TODO: fixa så att det stämmer för derby
    private static String username = "admin";   
    private static String password = "admin";
    private static Connection con;
    private static String urlstring;

    public static Connection getConnection() {
        try {
            Class.forName(driverName); // ska göra samma sak som context, datasource?
            /*
            Context initContext = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource)envContext.lookup("jdbc/derby");
            */
            try {
                con = DriverManager.getConnection(urlstring, username, password);
            } catch (SQLException ex) {
                // log an exception. fro example:
                System.out.println("Failed to create the database connection."); 
            }
        } catch (ClassNotFoundException ex) {
            // log an exception. for example:
            System.out.println("Driver not found."); 
        }
        return con;
    }
}