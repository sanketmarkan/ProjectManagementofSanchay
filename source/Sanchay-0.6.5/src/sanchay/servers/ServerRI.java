/*
 * ServerRI.java
 *
 * Created on November 3, 2005, 9:47 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package sanchay.servers;

import java.rmi.*;

import sanchay.common.types.*;
import sanchay.properties.*;


/**
 *
 *  @author Anil Kumar Singh
 */
public interface ServerRI extends SanchayRI {
    public PropertiesManager getPropertiesManager() throws RemoteException;
    public void setPropertiesManager(PropertiesManager pm) throws RemoteException;
}
