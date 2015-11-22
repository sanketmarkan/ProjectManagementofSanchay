/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package access;

import com.healthmarketscience.rmiio.RemoteInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author Akshat Tandon
 */
public interface AccessInterface extends Remote{
    
    public boolean authenticate(String username, String password)throws RemoteException;
    
    public ArrayList<String> getAllottedBatchesName()throws RemoteException;
    
    public ArrayList<String> getBatchFilesName(int batchid)throws RemoteException;
    
    public ArrayList<Integer> getAllottedBatchesId()throws RemoteException;
    
    public int getBatchId(String batchname)throws RemoteException;
    
    public String getBatchDeadline(String batchname)throws RemoteException;
    
    public boolean getBatchStatus(String batchname)throws RemoteException;
    
    public void setBatchStatus(String batchname, boolean status)throws RemoteException;
    
    public void publish(RemoteInputStream data, String fileName, int batchId)throws RemoteException;
    
    public void writeToFile(InputStream stream, String fileName, int batchId)throws RemoteException,FileNotFoundException, IOException ;
    
    public RemoteInputStream getFile(String fileName, int batchId)throws RemoteException, FileNotFoundException, IOException;
    
}
