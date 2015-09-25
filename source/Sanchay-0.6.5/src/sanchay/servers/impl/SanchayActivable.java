/*
 * SanchayActivable.java
 *
 * Created on November 3, 2005, 10:19 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package sanchay.servers.impl;

import java.io.*;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.activation.*;

import sanchay.GlobalProperties;
import sanchay.servers.ResourceManagerServerRI;
import sanchay.servers.SanchayRI;
import sanchay.servers.UserManagerServerRI;

/**
 *
 *  @author Anil Kumar Singh
 */
public abstract class SanchayActivable extends Activatable
    implements SanchayRI, Serializable {
    
    protected UserManagerServerRI userManagerServerRI;
    protected ResourceManagerServerRI taskManagerServerRI;
    
    /** Creates a new instance of SanchayActivable */
    protected SanchayActivable(ActivationID id, int port)
        throws RemoteException {
        // Register the object with the activation system
        // then export it on an anonymous port

        super(id, 0);
    }    

    protected SanchayActivable(ActivationID id, int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf)
        throws RemoteException {
        // Register the object with the activation system
        // then export it on an anonymous port

        super(id, port, csf, ssf);
    }    

    protected SanchayActivable(String location, MarshalledObject data, boolean restart, int port)
        throws RemoteException, ActivationException {
        // Register the object with the activation system
        // then export it on an anonymous port

        super(location, data, restart, port);
    }    

    protected SanchayActivable(String location, MarshalledObject data, boolean restart,
	    int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf)
        throws RemoteException, ActivationException {
        // Register the object with the activation system
        // then export it on an anonymous port

        super(location, data, restart, port, csf, ssf);
    }    
    
    public String checkConnection()
    {
        return GlobalProperties.getIntlString("Connected_successfully");
    }

    public void setUserManager(UserManagerServerRI umri) throws RemoteException
    {
	userManagerServerRI = umri;
    }
    
    public void setTaskManager(ResourceManagerServerRI tmri) throws RemoteException
    {
	taskManagerServerRI = tmri;
    }
}
