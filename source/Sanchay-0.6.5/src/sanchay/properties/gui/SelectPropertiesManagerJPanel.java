/*
 * SelectPropertiesManagerJPanel.java
 *
 * Created on October 16, 2005, 3:32 PM
 */

package sanchay.properties.gui;

import java.io.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import sanchay.GlobalProperties;
import sanchay.properties.PropertiesTable;

/**
 *
 * @author  anil
 */
public class SelectPropertiesManagerJPanel extends javax.swing.JPanel {

    private JFrame owner;
    private JPanel editPMJPanel;

    private String pmfile;
    private PropertiesTable sanchayComponents;
    
    /** Creates new form SelectPropertiesManagerJPanel */
    public SelectPropertiesManagerJPanel() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        selectFromJPanel = new javax.swing.JPanel();
        selectFromJLabel = new javax.swing.JLabel();
        selectFromJComboBox = new javax.swing.JComboBox();
        selectPMJPanel = new javax.swing.JPanel();
        forSanchayComponentJPanel = new javax.swing.JPanel();
        sanchayComponentLabel = new javax.swing.JLabel();
        sanchayComponentJComboBox = new javax.swing.JComboBox();
        fromFileJPanel = new javax.swing.JPanel();
        fromFileJLabel = new javax.swing.JLabel();
        fromFileJTextField = new javax.swing.JTextField();
        fromFileJButton = new javax.swing.JButton();
        commandsJPanel = new javax.swing.JPanel();
        editJButton = new javax.swing.JButton();

        setLayout(new java.awt.GridLayout(3, 0, 0, 8));

        selectFromJPanel.setLayout(new javax.swing.BoxLayout(selectFromJPanel, javax.swing.BoxLayout.LINE_AXIS));

        selectFromJLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        selectFromJLabel.setLabelFor(selectFromJComboBox);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("sanchay/intl/sanchay"); // NOI18N
        selectFromJLabel.setText(bundle.getString("Select_from:_")); // NOI18N
        selectFromJLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        selectFromJLabel.setMinimumSize(new java.awt.Dimension(77, 19));
        selectFromJPanel.add(selectFromJLabel);

        selectFromJComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sanchay Component", "File" }));
        selectFromJComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectFromJComboBoxActionPerformed(evt);
            }
        });
        selectFromJPanel.add(selectFromJComboBox);

        add(selectFromJPanel);

        selectPMJPanel.setLayout(new java.awt.CardLayout());

        forSanchayComponentJPanel.setLayout(new java.awt.BorderLayout());

        sanchayComponentLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sanchayComponentLabel.setLabelFor(sanchayComponentJComboBox);
        sanchayComponentLabel.setText("Sanchay component: ");
        sanchayComponentLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        forSanchayComponentJPanel.add(sanchayComponentLabel, java.awt.BorderLayout.WEST);
        forSanchayComponentJPanel.add(sanchayComponentJComboBox, java.awt.BorderLayout.CENTER);

        selectPMJPanel.add(forSanchayComponentJPanel, "forSanchay");

        fromFileJPanel.setLayout(new java.awt.BorderLayout());

        fromFileJLabel.setLabelFor(fromFileJTextField);
        fromFileJLabel.setText("File: ");
        fromFileJPanel.add(fromFileJLabel, java.awt.BorderLayout.WEST);

        fromFileJTextField.setText(bundle.getString("File")); // NOI18N
        fromFileJPanel.add(fromFileJTextField, java.awt.BorderLayout.CENTER);

        fromFileJButton.setText(bundle.getString("Browse")); // NOI18N
        fromFileJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fromFileJButtonActionPerformed(evt);
            }
        });
        fromFileJPanel.add(fromFileJButton, java.awt.BorderLayout.EAST);

        selectPMJPanel.add(fromFileJPanel, "fromFile");

        add(selectPMJPanel);

        editJButton.setText(bundle.getString("Edit")); // NOI18N
        editJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editJButtonActionPerformed(evt);
            }
        });
        commandsJPanel.add(editJButton);

        add(commandsJPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void editJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editJButtonActionPerformed
// TODO add your handling code here:
        String selItem = (String) selectFromJComboBox.getSelectedItem();
        
        if(selItem.equals(GlobalProperties.getIntlString("Sanchay_Component")))
        {
            Vector v = sanchayComponents.getValues("SanchayComponent", sanchayComponentJComboBox.getSelectedItem(), new String[] {"PMPath"});
            
            if(v.size() != 1 && ((Vector) v.get(0)).size() != 1)
            {
                JOptionPane.showMessageDialog(this, GlobalProperties.getIntlString("Error_in_Sanchay_component_list."), GlobalProperties.getIntlString("Error"), JOptionPane.ERROR_MESSAGE);
                return;
            }

            pmfile = (String) ((Vector) v.get(0)).get(0);
        }
        else if(selItem.equals(GlobalProperties.getIntlString("File")))
        {
            pmfile = fromFileJTextField.getText();
        }
        
        if(validateFilePath(pmfile).equals("") == false)
        {
            JDialog editPMDialog = new JDialog(this.getOwner(), GlobalProperties.getIntlString("Properties_Editor"), true);
            
            editPMJPanel = new PropertiesManagerJPanel(pmfile, GlobalProperties.getIntlString("UTF-8"));
                    
            ((PropertiesManagerJPanel) editPMJPanel).setOwner(this.getOwner());
            ((PropertiesManagerJPanel) editPMJPanel).setDialog(editPMDialog);
                    
            editPMDialog.add(editPMJPanel);
            editPMDialog.setBounds(280, 180, 500, 400);

            editPMDialog.setVisible(true);
        }
    }//GEN-LAST:event_editJButtonActionPerformed

    private void fromFileJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fromFileJButtonActionPerformed
// TODO add your handling code here:
        JFileChooser chooser = new JFileChooser();
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
           pmfile = chooser.getSelectedFile().getAbsolutePath();
           
           if(validateFilePath(pmfile).equals("") == false)
           {
               fromFileJTextField.setText(pmfile);
           }
        }
    }//GEN-LAST:event_fromFileJButtonActionPerformed

    private void selectFromJComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectFromJComboBoxActionPerformed
// TODO add your handling code here:
        String selItem = (String) selectFromJComboBox.getSelectedItem();
        
        if(selItem.equals(GlobalProperties.getIntlString("Sanchay_Component")))
        {
            ((CardLayout) selectPMJPanel.getLayout()).show(selectPMJPanel, "forSanchay");
        }
        else if(selItem.equals(GlobalProperties.getIntlString("File")))
        {
            ((CardLayout) selectPMJPanel.getLayout()).show(selectPMJPanel, "fromFile");
        }
    }//GEN-LAST:event_selectFromJComboBoxActionPerformed
    
    public JFrame getOwner()
    {
        return owner;
    }
    
    public void setOwner(JFrame jframe)
    {
        owner = (JFrame) jframe;
        
        Vector vt = sanchayComponents.getColumn(GlobalProperties.getIntlString("SanchayComponent"));
        DefaultComboBoxModel cm = new DefaultComboBoxModel(vt);
        sanchayComponentJComboBox.setModel(cm);
    }

    private String validateFilePath(String fn)
    {
        PropertiesManagerMain owner = (PropertiesManagerMain) getOwner();
        String ws = owner.getWorkspace();
        
        if(fn.startsWith(File.separator) == true)
        {
            if(fn.startsWith(ws) == false)
            {
                JOptionPane.showMessageDialog(this, GlobalProperties.getIntlString("Files_should_be_in_the_workspace_directory:_") + ws, GlobalProperties.getIntlString("Error"), JOptionPane.ERROR_MESSAGE);
                return "";
            }
//            else
//            {
//                fn = taskPropFile.substring(ws.length());
//                fn.replaceFirst(ws, "");
//            }
        }
        
        return fn;
    }
    
    public void configure(String pmPath, String charSet)
    {
        try {
            sanchayComponents = new PropertiesTable(pmPath, charSet);
            sanchayComponents.print(System.out);
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
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JPanel commandsJPanel;
    public javax.swing.JButton editJButton;
    public javax.swing.JPanel forSanchayComponentJPanel;
    public javax.swing.JButton fromFileJButton;
    public javax.swing.JLabel fromFileJLabel;
    public javax.swing.JPanel fromFileJPanel;
    public javax.swing.JTextField fromFileJTextField;
    public javax.swing.JComboBox sanchayComponentJComboBox;
    public javax.swing.JLabel sanchayComponentLabel;
    public javax.swing.JComboBox selectFromJComboBox;
    public javax.swing.JLabel selectFromJLabel;
    public javax.swing.JPanel selectFromJPanel;
    public javax.swing.JPanel selectPMJPanel;
    // End of variables declaration//GEN-END:variables
    
}
