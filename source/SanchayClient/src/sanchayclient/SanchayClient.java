/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sanchayclient;

import access.AccessInterface;
import java.rmi.Naming;
import javax.swing.JFrame;

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

/*
 try {
 AccessInterface obj = (AccessInterface) Naming.lookup("//localhost/Access");
 JFrame window = new JFrame();
 LoginForm form = new LoginForm();
 window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 window.add(form);
 int height = (int) form.getBounds().getHeight();
 int width = (int) form.getBounds().getWidth();
 //System.out.println("Height " + height);
 //System.out.println("");
 window.setSize(300,400);
        
 window.setVisible(true);
                       
 /*if(obj.authenticate("akshat","asd")){
 System.out.println("User authenticated");
 }
 else System.out.println("NOT authenticated");*/
                        //System.out.println(hello.say());
/*
 } catch (Exception e) {
 System.out.println("HelloClient exception: " + e);
 }
 }
 */
