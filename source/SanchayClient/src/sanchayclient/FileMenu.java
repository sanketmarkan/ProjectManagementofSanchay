/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sanchayclient;

import access.AccessInterface;
import com.healthmarketscience.rmiio.GZIPRemoteInputStream;
import com.healthmarketscience.rmiio.RemoteInputStream;
import com.healthmarketscience.rmiio.RemoteInputStreamClient;
import com.healthmarketscience.rmiio.RemoteInputStreamServer;
import com.healthmarketscience.rmiio.SimpleRemoteInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.ListSelectionModel;

/**
 *
 * @author Akshat Tandon
 */
public class FileMenu extends javax.swing.JFrame {

    /**
     * Creates new form FileMenu
     */
    public FileMenu() {
        try {
            obj = (AccessInterface) Naming.lookup("//localhost/Access");
            batchNames = obj.getAllottedBatchesName();
            batchIds = obj.getAllottedBatchesId();
            numBatches = batchNames.size();
            System.out.println("Initializing");
            System.out.println("Batch names" + batchNames);
            System.out.println("Batch Ids" + batchIds);
            //System.out.println("Batch deadline" + obj.getBatchDeadline());

        } catch (NotBoundException ex) {
            Logger.getLogger(FileMenu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(FileMenu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(FileMenu.class.getName()).log(Level.SEVERE, null, ex);
        }

        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {
        //Vehicle[] car = new Vehicle[N];
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel[numBatches];
        batchNameLabel = new javax.swing.JLabel[numBatches];
        deadlineLabel = new javax.swing.JLabel[numBatches];
        statusLabel = new javax.swing.JLabel[numBatches];
        statusComboBox = new javax.swing.JComboBox[numBatches];
        jSeparator1 = new javax.swing.JSeparator[numBatches];
        filesLabel = new javax.swing.JLabel[numBatches];
        filesScrollPane = new javax.swing.JScrollPane[numBatches];
        filesList = new javax.swing.JList[numBatches];

        fileNameLabel = new javax.swing.JLabel[numBatches];
        downloadButton = new javax.swing.JButton[numBatches];
        uploadButton = new javax.swing.JButton[numBatches];
        //jPanel2 = new javax.swing.JPanel[numBatches];
        jPanel1Layout = new javax.swing.GroupLayout[numBatches];
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Remote File Manager");

        for (int i = 0; i < numBatches; i++) {
            batchNameLabel[i] = new javax.swing.JLabel();
            jPanel1[i] = new javax.swing.JPanel();
            deadlineLabel[i] = new javax.swing.JLabel();
            statusLabel[i] = new javax.swing.JLabel();
            statusComboBox[i] = new javax.swing.JComboBox();
            jSeparator1[i] = new javax.swing.JSeparator();
            filesLabel[i] = new javax.swing.JLabel();
            filesScrollPane[i] = new javax.swing.JScrollPane();
            filesList[i] = new javax.swing.JList();
            fileNameLabel[i] = new javax.swing.JLabel();
            downloadButton[i] = new javax.swing.JButton();
            uploadButton[i] = new javax.swing.JButton();
            //jPanel2[i] = new javax.swing.JPanel();
        }

        for (int i = 0; i < numBatches; i++) {
            try {
                System.out.println("Batch name is " + batchNames.get(i));

                batchNameLabel[i].setText("Batch Name: " + batchNames.get(i));

                String temp2 = batchNames.get(i);
                int len2 = temp2.length();
                String temp3 = temp2.substring(1, len2 - 1);
                deadlineLabel[i].setText("Deadline: " + obj.getBatchDeadline(temp3));

                String temp4 = new String();
                if (obj.getBatchStatus(temp3)) {
                    temp4 = "Done\t";
                } else {
                    temp4 = "Not Done\t";
                }
                statusLabel[i].setText("Status: " + temp4 + "\t");

                statusComboBox[i].setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Done", "Not Done"}));

                //javax.swing.JComboBox statusComboBoxTemp = statusComboBox[i];
                javax.swing.JLabel statusLabelTemp = statusLabel[i];
                statusComboBox[i].addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        javax.swing.JComboBox cb = (javax.swing.JComboBox) evt.getSource();
                        String stName = (String) cb.getSelectedItem();
                        boolean fg;
                        if (stName.equalsIgnoreCase("Done")) {
                            fg = true;
                        } else {
                            fg = false;
                        }
                        try {
                            obj.setBatchStatus(temp3, fg);
                            statusLabelTemp.setText("Status: " + stName + "\t");
                        } catch (RemoteException ex) {
                            Logger.getLogger(FileMenu.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                filesLabel[i].setText("Files");
                ArrayList<String> temp;
                temp = obj.getBatchFilesName(batchIds.get(i));
                filesList[i].setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                filesList[i].setModel(new javax.swing.AbstractListModel() {
                    //String[] arr = list.toArray(new String[list.size()]);
                    String[] strings = temp.toArray(new String[temp.size()]);

                    public int getSize() {
                        return strings.length;
                    }

                    public Object getElementAt(int i) {
                        return strings[i];
                    }
                });
                javax.swing.JLabel fileNameLabelTemp = fileNameLabel[i];
                javax.swing.JList filesListTemp = filesList[i];
                filesList[i].addListSelectionListener(new javax.swing.event.ListSelectionListener() {
                    public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                        if (!evt.getValueIsAdjusting()) {
                            String selection = (String) filesListTemp.getSelectedValue();
                            //System.out.println("Object -- * "+selection);
                            fileNameLabelTemp.setText("Name:" + selection);
                        }
                    }
                });
                filesScrollPane[i].setViewportView(filesList[i]);

                fileNameLabel[i].setText("Name:");
                int iTemp = batchIds.get(i);
                downloadButton[i].setText("Download");
                downloadButton[i].addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        try {
                            InputStream stream = null;
                            String downloadedFileName = (String) filesListTemp.getSelectedValue();
                            int downloadedBatchId = iTemp;
                            RemoteInputStream data = obj.getFile(downloadedFileName, downloadedBatchId);
                            stream = RemoteInputStreamClient.wrap(data);
                            String pathName = "/home/droftware/Desktop/SanchayDownloads/" + downloadedFileName;
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
                            System.out.println("File downloaded - "+ downloadedFileName);
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(FileMenu.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(FileMenu.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                });

                uploadButton[i].setText("Upload");
                
                uploadButton[i].addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        JFileChooser fc = new JFileChooser();
                        int returnVal = fc.showOpenDialog(null);
                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            try {
                                File f = fc.getSelectedFile();
                                //File f = new File("/home/droftware/SSAD_2015_Team15/source/sanchay_web/README.md");
                                if (f.exists()) {
                                    System.out.println("File exists");
                                } else {
                                    System.out.println("File does not exist");
                                }
                                String uploadedFileName = (String) filesListTemp.getSelectedValue();
                                int uploadedBatchId = iTemp;
                                ByteArrayInputStream stream;
                                stream = new ByteArrayInputStream(Files.readAllBytes(f.toPath()));
                                RemoteInputStreamServer data = new GZIPRemoteInputStream(stream);
                                obj.publish(data.export(), uploadedFileName, uploadedBatchId);
                                System.out.println("File transferred File Name -" + uploadedFileName + "Batch id " + uploadedBatchId);
                            } catch (IOException ex) {
                                Logger.getLogger(FileMenu.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                });

                //
                jPanel1Layout[i] = new javax.swing.GroupLayout(jPanel1[i]);
                jPanel1[i].setLayout(jPanel1Layout[i]);
                jPanel1Layout[i].setHorizontalGroup(
                        jPanel1Layout[i].createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSeparator1[i])
                        .addGroup(jPanel1Layout[i].createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout[i].createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(batchNameLabel[i])
                                        .addComponent(deadlineLabel[i])
                                        .addGroup(jPanel1Layout[i].createSequentialGroup()
                                                .addComponent(statusLabel[i])
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(statusComboBox[i], javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(filesLabel[i])
                                        .addGroup(jPanel1Layout[i].createSequentialGroup()
                                                .addComponent(filesScrollPane[i], javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel1Layout[i].createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(fileNameLabel[i])
                                                        .addComponent(downloadButton[i])
                                                        .addComponent(uploadButton[i]))))
                                .addContainerGap(151, Short.MAX_VALUE))
                );
                jPanel1Layout[i].setVerticalGroup(
                        jPanel1Layout[i].createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout[i].createSequentialGroup()
                                .addContainerGap()
                                .addComponent(batchNameLabel[i])
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(deadlineLabel[i])
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout[i].createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(statusLabel[i])
                                        .addComponent(statusComboBox[i], javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator1[i], javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(filesLabel[i])
                                .addGroup(jPanel1Layout[i].createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout[i].createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(filesScrollPane[i], javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout[i].createSequentialGroup()
                                                .addGap(27, 27, 27)
                                                .addComponent(fileNameLabel[i])
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(downloadButton[i])
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(uploadButton[i])))
                                .addContainerGap(24, Short.MAX_VALUE))
                );
                Integer temp1 = (i + 1);
                jTabbedPane1.addTab("Batch " + Integer.toString(temp1), jPanel1[i]);
            } catch (RemoteException ex) {
                Logger.getLogger(FileMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>                        

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */

    }

    // Variables declaration - do not modify                     
    private javax.swing.JLabel[] batchNameLabel;
    private javax.swing.JLabel[] deadlineLabel;
    private javax.swing.JButton[] downloadButton;
    private javax.swing.JLabel[] fileNameLabel;
    private javax.swing.JLabel[] filesLabel;
    private javax.swing.JList[] filesList;
    private javax.swing.JScrollPane[] filesScrollPane;
    private javax.swing.JPanel[] jPanel1;
    private javax.swing.JPanel[] jPanel2;
    private javax.swing.JSeparator[] jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JComboBox[] statusComboBox;
    private javax.swing.JLabel[] statusLabel;
    private javax.swing.JButton[] uploadButton;
    javax.swing.GroupLayout jPanel1Layout[];
    AccessInterface obj;
    ArrayList<String> batchNames;
    ArrayList<Integer> batchIds;

    int numBatches;
    // End of variables declaration                   
}
