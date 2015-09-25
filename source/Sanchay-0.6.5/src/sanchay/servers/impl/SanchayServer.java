/*
 * SanchayActivable.java
 *
 * Created on November 2, 2005, 7:12 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package sanchay.servers.impl;

import java.io.*;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.activation.*;
import java.util.*;

import sanchay.common.types.*;
import sanchay.gui.clients.*;
import sanchay.properties.*;
import sanchay.servers.ServerRI;

/**
 *
 *  @author Anil Kumar Singh
 */
public abstract class SanchayServer extends SanchayActivable
    implements ServerRI, Serializable {

    protected PropertiesManager propman;

    /**
     * 
     */
    protected SanchayServer(PropertiesManager pm) throws RemoteException 
    {
        super(null, 0);
        
        propman = pm;
    }    

    protected SanchayServer(ActivationID id, int port)
        throws RemoteException {
        // Register the object with the activation system
        // then export it on an anonymous port

        super(id, 0);
    }    

    protected SanchayServer(ActivationID id, int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf)
        throws RemoteException {
        // Register the object with the activation system
        // then export it on an anonymous port

        super(id, port, csf, ssf);
    }    

    protected SanchayServer(String location, MarshalledObject data, boolean restart, int port)
        throws RemoteException, ActivationException {
        // Register the object with the activation system
        // then export it on an anonymous port

        super(location, data, restart, port);
    }    

    protected SanchayServer(String location, MarshalledObject data, boolean restart,
	    int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf)
        throws RemoteException, ActivationException {
        // Register the object with the activation system
        // then export it on an anonymous port

        super(location, data, restart, port, csf, ssf);
    }    
	
    public PropertiesManager getPropertiesManager() throws RemoteException
    {
        return propman;
    }
	
    public void setPropertiesManager(PropertiesManager pm) throws RemoteException
    {
        propman = pm;
    }
}
