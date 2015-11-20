/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adapter;

import java.util.ArrayList;

/**
 *
 * @author droftware
 */
public interface AdapterInterface {
    
    boolean authenticate(String username, String password);
    
    boolean checkAuthentication();
    
    ArrayList<String> getAllottedBatchesName();
    
    ArrayList<String> getBatchFilesName(int batchid);
    
    ArrayList<Integer> getAllottedBatchesId();
    
    int getBatchId(String batchname);
    
    String getBatchDeadline(String batchname);
    
    boolean getBatchStatus(String batchname);
    
    void setBatchStatus(String batchname, boolean status);
    
}
