/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sanchayclient;

import access.AccessInterface;
import com.healthmarketscience.rmiio.RemoteInputStreamServer;
import com.healthmarketscience.rmiio.SimpleRemoteInputStream;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.text.Document;

/**
 *
 * @author droftware
 */
public class SanchayClient {

    public static void main(String[] argv) {

        JFrame window = new JFrame();
        LoginForm form = new LoginForm();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(form);
        window.setTitle("Login");
        window.setSize(300, 150);
        window.setVisible(true);

    }
}


