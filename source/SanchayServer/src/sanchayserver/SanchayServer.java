/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sanchayserver;
import access.AccessInterface;
import java.rmi.Naming;
import java.rmi.registry.Registry;
/**
 *
 * @author Akshat Tandon
 */


public class SanchayServer {
        public static void main(String[] args) {
                try {
                        Registry r = java.rmi.registry.LocateRegistry.createRegistry(1099);
                        //1099 is the port number.
                        AccessInterface iobj = new ConcreteAccess();
                        r.rebind("Access", iobj);
                        System.out.println("Server is connected and ready for operation.");
                } catch (Exception e) {
                        System.out.println("Server not connected: " + e);
                }
        }
}

