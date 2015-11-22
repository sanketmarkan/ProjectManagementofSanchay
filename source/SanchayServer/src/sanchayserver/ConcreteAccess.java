/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sanchayserver;

import access.AccessInterface;
import adapter.AdapterInterface;
import com.healthmarketscience.rmiio.GZIPRemoteInputStream;
import com.healthmarketscience.rmiio.RemoteInputStream;
import com.healthmarketscience.rmiio.RemoteInputStreamClient;
import com.healthmarketscience.rmiio.RemoteInputStreamServer;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Akshat Tandon
 */
public class ConcreteAccess extends UnicastRemoteObject implements AccessInterface {

    AdapterInterface adptr;

    public ConcreteAccess() throws RemoteException {
        adptr = new PortalAdapter();
    }

    public boolean authenticate(String username, String password) {
        return adptr.authenticate(username, password);
    }

    public ArrayList<String> getAllottedBatchesName() {
        return adptr.getAllottedBatchesName();
    }

    public ArrayList<String> getBatchFilesName(int batchid) {
        return adptr.getBatchFilesName(batchid);
    }

    public ArrayList<Integer> getAllottedBatchesId() {
        return adptr.getAllottedBatchesId();
    }

    public int getBatchId(String batchname) {
        return adptr.getBatchId(batchname);
    }

    public String getBatchDeadline(String batchname) {
        return adptr.getBatchDeadline(batchname);
    }

    public boolean getBatchStatus(String batchname) {
        return adptr.getBatchStatus(batchname);
    }

    public void setBatchStatus(String batchname, boolean status) {
        adptr.setBatchStatus(batchname, status);
    }

    public void publish(RemoteInputStream data, String fileName, int batchId) {
        InputStream stream = null;
        try {
            stream = RemoteInputStreamClient.wrap(data);
            writeToFile(stream, fileName, batchId);
        } catch (IOException ex) {
            Logger.getLogger(ConcreteAccess.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stream.close();
            } catch (IOException ex) {
                Logger.getLogger(ConcreteAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void writeToFile(InputStream stream, String fileName, int batchId) throws FileNotFoundException, IOException {
        String batchFolder = "Batch_" + Integer.toString(batchId) + "/";
        String pathName = "/home/droftware/SSAD_2015_Team15/source/sanchay_web/media/" + batchFolder + fileName;
        File tf = new File(pathName);
        FileOutputStream output = new FileOutputStream(tf);
        int chunk = 4096;
        byte[] result = new byte[chunk];
        int readBytes = 0;
        do {
            readBytes = stream.read(result);
            if (readBytes != -1) {
                output.write(result, 0, readBytes);
            }
        } while (readBytes != -1);
        System.out.println("File transferred");
    }

    public RemoteInputStream getFile(String fileName, int batchId) throws FileNotFoundException, IOException{
       
        //istream = new GZIPRemoteInputStream(new BufferedInputStream(new FileInputStream(fileName)));
        String pathName = "/home/droftware/SSAD_2015_Team15/source/sanchay_web/media/";
        pathName = pathName + "Batch_" + Integer.toString(batchId);
        pathName = pathName + "/" + fileName;
        File f = new File(pathName);
        if (f.exists()) {
            System.out.println("File exists");
        } else {
            System.out.println("File does not exist");
        }
        ByteArrayInputStream stream;
        stream = new ByteArrayInputStream(Files.readAllBytes(f.toPath()));
        RemoteInputStreamServer istream = new GZIPRemoteInputStream(stream);
        RemoteInputStream result = istream.export();
        istream = null;
        return result;
    }

}
