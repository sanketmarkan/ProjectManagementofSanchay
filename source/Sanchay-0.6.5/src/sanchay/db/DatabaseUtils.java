/*
 * DatabaseUtils.java
 *
 * Created on July 25, 2008, 11:27 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package sanchay.db;

import javax.swing.JDialog;
import javax.swing.JFrame;
import sanchay.GlobalProperties;
import sanchay.db.gui.ConnectToDBJPanel;
import sanchay.gui.common.DialogFactory;
import sanchay.gui.common.SanchayJDialog;

/**
 *
 * @author Anil Kumar Singh
 */
public class DatabaseUtils {
    
    public static String DERBY_DRIVER_NAME = "org.apache.derby.jdbc.EmbeddedDriver";
    public static String DERBY_PROTCOL = "jdbc:derby:";
    public static String DERBY_DB_NAME = "derbyDB";
    public static String DERBY_URL = "jdbc:derby:derbyDB";
    
    /** Creates a new instance of DatabaseUtils */
    public DatabaseUtils()
    {
    }
    
    public static ConnectToDBJPanel connectToDatabase(JFrame owner)
    {
        SanchayJDialog dbConnDialog = (SanchayJDialog) DialogFactory.showDialog(ConnectToDBJPanel.class, owner, GlobalProperties.getIntlString("Connect_to_Database"), true);
        
        return (ConnectToDBJPanel) dbConnDialog.getJPanel();        
    }

    public static ConnectToDBJPanel connectToDatabase(JDialog owner)
    {
        SanchayJDialog dbConnDialog = (SanchayJDialog) DialogFactory.showDialog(ConnectToDBJPanel.class, owner, GlobalProperties.getIntlString("Connect_to_Database"), true);
        
        return (ConnectToDBJPanel) dbConnDialog.getJPanel();        
    }
    
}
