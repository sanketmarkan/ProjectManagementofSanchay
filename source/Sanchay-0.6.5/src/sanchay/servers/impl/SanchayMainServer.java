/*
 * Created on Sep 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package sanchay.servers.impl;

import java.io.*;
import java.rmi.*;
import java.rmi.activation.*;
import java.util.*;

import sanchay.common.types.*;
import sanchay.gui.clients.*;
import sanchay.properties.*;
import sanchay.servers.SanchayServerRI;

/**
 *  @author Anil Kumar Singh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SanchayMainServer extends SanchayServer
    implements SanchayServerRI, Serializable {

    /**
     * 
     */

    public SanchayMainServer(PropertiesManager pm) throws RemoteException 
    {
        super(pm);
    }    
 
    public SanchayMainServer(ActivationID id, MarshalledObject data)
        throws RemoteException {
        // Register the object with the activation system
        // then export it on an anonymous port

        super(id, 0);
//	setPropertiesManager(SanchayMainOld.getSanchayComponentPM(ServerType.USER_MANAGER.toString()));
    }    
}
