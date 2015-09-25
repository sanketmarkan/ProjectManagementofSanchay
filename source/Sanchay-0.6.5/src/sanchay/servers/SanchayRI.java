/*
 * SanchayRI.java
 *
 * Created on November 2, 2005, 7:10 PM
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
public interface SanchayRI extends Remote {

    public String checkConnection() throws RemoteException;

    public void setUserManager(UserManagerServerRI umri) throws RemoteException;
    public void setTaskManager(ResourceManagerServerRI tmri) throws RemoteException;
}
