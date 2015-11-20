/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sanchayserver;
import access.AccessInterface;
import adapter.AdapterInterface;
import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;
/**
 *
 * @author Akshat Tandon
 */
public class ConcreteAccess extends UnicastRemoteObject implements AccessInterface{
    AdapterInterface adptr;
    
    public ConcreteAccess()throws RemoteException{
        adptr = new PortalAdapter();
    }
    
    public boolean authenticate(String username, String password){
        return adptr.authenticate(username, password);
    }
    
    public ArrayList<String> getAllottedBatchesName(){
        return adptr.getAllottedBatchesName();
    }
    
    public ArrayList<String> getBatchFilesName(int batchid){
        return adptr.getBatchFilesName(batchid);
    }
    
    public ArrayList<Integer> getAllottedBatchesId(){
        return adptr.getAllottedBatchesId();
    }
    
    public int getBatchId(String batchname){
        return adptr.getBatchId(batchname);
    }
    
    public String getBatchDeadline(String batchname){
        return adptr.getBatchDeadline(batchname);
    }
    
    public boolean getBatchStatus(String batchname){
        return adptr.getBatchStatus(batchname);
    }
    
    public void setBatchStatus(String batchname, boolean status){
        adptr.setBatchStatus(batchname, status);
    }
  
}
