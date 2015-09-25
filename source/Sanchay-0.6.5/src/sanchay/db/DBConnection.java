/*
 * DBConnection.java
 *
 * Created on July 23, 2008, 10:08 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package sanchay.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import sanchay.GlobalProperties;

/**
 *
 * @author Anil Kumar Singh
 */
public class DBConnection {
    static String url = DatabaseUtils.DERBY_URL;
    String username;
    String password;
    static String driver = DatabaseUtils.DERBY_DRIVER_NAME;
    Connection connection;
    String status = GlobalProperties.getIntlString("Not_Connected");
    
    public DBConnection() {
    }
    
    public void connect()
    {
        try {
            Class.forName(driver);
            status = GlobalProperties.getIntlString("Found_driver,_starting_to_connect...");
        } catch (java.lang.ClassNotFoundException e) {
            status = GlobalProperties.getIntlString("Driver_not_found");
            return;
        }
        try {
            connection = DriverManager.getConnection(url, username, password);
            status = GlobalProperties.getIntlString("Connected_to_the_database");
        } catch(java.sql.SQLException e) {
            status = GlobalProperties.getIntlString("Could_not_connect");
        }        
    }
    
    public static String getURL()
    {
        return url;
    }
    
    public static void setURL(String u)
    {
        url = u;
    }
    
    public void setUser(String user)
    {
        this.username = user;
    }
    
    public void setPassword(String pwd)
    {
        this.password = pwd;
    }
    
    public String getStatus() {
        return status;
    }
    
    public String getPass() {
        return password;
    }
    
    public String getUser() {
        return username;
    }
    
    public ResultSet executeQuery(String s) throws java.sql.SQLException {
        Statement stmt = connection.createStatement();
        System.out.println(s);
        return stmt.executeQuery(s);
    }
    
    public int executeUpdate(String s) throws java.sql.SQLException {
        Statement stmt = connection.createStatement();
        System.out.println(s);
        return stmt.executeUpdate(s);
    }
    
    public Connection getConnection()
    {
        return connection;
    }
}
