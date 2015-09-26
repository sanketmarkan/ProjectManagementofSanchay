/*
 * TamMarkerJFrame.java
 *
 * Created on September 26, 2005, 10:14 PM
 */

package sanchay.corpus.parallel.gui;

import java.awt.Component;
import java.awt.Frame;
import java.io.*;
import javax.swing.*;

import sanchay.GlobalProperties;
import sanchay.gui.clients.AnnotationClient;
import sanchay.gui.SelectTaskJPanel;

import sanchay.properties.PropertiesManager;
import sanchay.properties.PropertiesTable;
import sanchay.util.UtilityFunctions;
import sanchay.common.types.ClientType;

/**
 *
 * @author  anil
 */
public class ParallelMarkupMain extends javax.swing.JFrame implements AnnotationClient {

    protected ClientType clientType = ClientType.SENTENCE_ALIGNMENT_INTERFACE;


    private JPanel tamMarkerJPanel;
    
    private String workspace;
    private PropertiesManager propman;
    private PropertiesTable taskList;
    
    protected String langEnc;
    private int currentPosition;
    
    /** Creates new form TamMarkerJFrame */
    public ParallelMarkupMain() {
        setTitle(GlobalProperties.getIntlString("Parallel_Corpus_Annotation"));
        initComponents();

        tamMarkerJPanel = new SelectTaskJPanel(SelectTaskJPanel.PARALLEL_MARKUP_TASK);
        tamMarkerJPanel.setBounds(0, 0, 550, 150);
        desktopPane.add(tamMarkerJPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        setBounds(250, 300, 558, 195);
//        currentJPanel.setVisible(true);
	UtilityFunctions.centre(this);

        configure("workspace/tam-marker/server-props.txt", GlobalProperties.getIntlString("UTF-8"));
        ((SelectTaskJPanel) tamMarkerJPanel).setOwner(this);
        ((SelectTaskJPanel) tamMarkerJPanel).setAnnotationClient(this);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        desktopPane = new javax.swing.JDesktopPane();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        cutMenuItem = new javax.swing.JMenuItem();
        copyMenuItem = new javax.swing.JMenuItem();
        pasteMenuItem = new javax.swing.JMenuItem();
        deleteMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        contentMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().add(desktopPane, java.awt.BorderLayout.CENTER);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("sanchay/intl/sanchay"); // NOI18N
        fileMenu.setText(bundle.getString("File")); // NOI18N

        openMenuItem.setText(bundle.getString("Open")); // NOI18N
        fileMenu.add(openMenuItem);

        saveMenuItem.setText(bundle.getString("Save")); // NOI18N
        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setText(bundle.getString("Save_As_...")); // NOI18N
        fileMenu.add(saveAsMenuItem);

        exitMenuItem.setText(bundle.getString("Exit")); // NOI18N
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        editMenu.setText(bundle.getString("Edit")); // NOI18N

        cutMenuItem.setText(bundle.getString("Cut")); // NOI18N
        editMenu.add(cutMenuItem);

        copyMenuItem.setText(bundle.getString("Copy")); // NOI18N
        editMenu.add(copyMenuItem);

        pasteMenuItem.setText(bundle.getString("Paste")); // NOI18N
        editMenu.add(pasteMenuItem);

        deleteMenuItem.setText(bundle.getString("Delete")); // NOI18N
        editMenu.add(deleteMenuItem);

        menuBar.add(editMenu);

        helpMenu.setText(bundle.getString("Help")); // NOI18N

        contentMenuItem.setText(bundle.getString("Contents")); // NOI18N
        helpMenu.add(contentMenuItem);

        aboutMenuItem.setText(bundle.getString("About")); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    public ClientType getClientType()
    {
        return clientType;
    }
    
    public String getWorkspace()
    {
        return workspace;
    }
    
    public void setWorkspace(String p) throws Exception
    {
        workspace = p;
        File wsdir = new File(p);

        if(wsdir.exists() == false)
        {
            if(wsdir.mkdir() == false)
                throw new Exception();
        }
        else if(wsdir.isDirectory() == false)
        throw new Exception();
    }
	
    public PropertiesManager getPropertiesManager()
    {
        return propman;
    }
    
    public void configure(String pmPath, String charSet)
    {
        clear();
        
        try {
            propman = new PropertiesManager(pmPath, charSet);
            propman.print(System.out);
            
            
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void setCurrentPosition(int cp)
    {
        currentPosition = cp;
    }
    
    public void clear()
    {
        workspace = "/home/anil/myproj/sanchay/eclipse/Sanchay/";
    }

    public void setWorkJPanel(JPanel wjp)
    {
        
    }
  
    public PropertiesTable getTaskList()
    {
        return taskList;
    }
    
    public void setTaskList(PropertiesTable tl)
    {
        taskList = tl;
    }

    public String getLangEnc()
    {
        return langEnc;
    }
    
    public Frame getOwner() {
        return null;
    }
    
    public void setOwner(Frame f) {
    }

    public void setParentComponent(Component parentComponent)
    {
    }

    public JMenuBar getJMenuBar() {
        throw new UnsupportedOperationException(GlobalProperties.getIntlString("Not_supported_yet."));
    }

    public JToolBar getJToolBar() {
        throw new UnsupportedOperationException(GlobalProperties.getIntlString("Not_supported_yet."));
    }

    public JPopupMenu getJPopupMenu()
    {
        throw new UnsupportedOperationException(GlobalProperties.getIntlString("Not_supported_yet."));
    }    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ParallelMarkupMain().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JMenuItem aboutMenuItem;
    public javax.swing.JMenuItem contentMenuItem;
    public javax.swing.JMenuItem copyMenuItem;
    public javax.swing.JMenuItem cutMenuItem;
    public javax.swing.JMenuItem deleteMenuItem;
    public javax.swing.JDesktopPane desktopPane;
    public javax.swing.JMenu editMenu;
    public javax.swing.JMenuItem exitMenuItem;
    public javax.swing.JMenu fileMenu;
    public javax.swing.JMenu helpMenu;
    public javax.swing.JMenuBar menuBar;
    public javax.swing.JMenuItem openMenuItem;
    public javax.swing.JMenuItem pasteMenuItem;
    public javax.swing.JMenuItem saveAsMenuItem;
    public javax.swing.JMenuItem saveMenuItem;
    // End of variables declaration//GEN-END:variables
}