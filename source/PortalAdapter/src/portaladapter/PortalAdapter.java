/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portaladapter;

import java.util.ArrayList;
import org.python.util.PythonInterpreter;
import org.python.core.*;
/**
 *
 * @author Akshat Tandon
 */
public class PortalAdapter {
    private PythonInterpreter pi;
    boolean auth_token;
    
    PortalAdapter(){
        pi = new PythonInterpreter(null, new PySystemState());
        auth_token = false;
        pi.exec("import django");
        pi.exec("import doj");
        pi.exec("import os");
        pi.exec("import sys"); 
        pi.exec("sys.path.append('/home/droftware/SSAD_2015_Team15/source/sanchay_web')");
        pi.exec("sys.path.append('/home/droftware/SSAD_2015_Team15/source/sanchay_web/sanchay_web')");
        pi.exec("sys.path.append('/home/droftware/SSAD_2015_Team15/source/sanchay_web/project_management')");
        pi.exec("os.environ['DJANGO_SETTINGS_MODULE']='sanchay_web.settings'");  
        pi.exec("django.setup()");
        pi.exec("from project_management.models import Annotator, Batch, Document, Message, Deadline");
        pi.exec("temp = str(Batch.objects.all()[0])");
       PyString str = (PyString)pi.get("temp");
       System.out.println("Magic -- " + str.asString());
    }
    
    boolean authenticate(String username, String password){
        PyInteger flag;
        pi.exec("from django.contrib.auth import authenticate");
        pi.exec("pflag = 0");
        pi.set("pyusr", new PyString(username));
        pi.set("pwd", new PyString(password));
        pi.exec("pauth_user = authenticate(username=pyusr, password=pwd)");
        pi.exec("if pauth_user is not None:\n\t pflag = 1");
        flag = (PyInteger)pi.get("pflag");
  
        if(flag.asInt() == 1){
            auth_token = true;
            return true;
        }
        else return false;
    }
    
    boolean checkAuthentication(){
        if(this.auth_token == true)return true;
        else return false;
    }
    
    ArrayList<String> getAllottedBatchesName(){
        //pbatches = pauth_user.annotator.batches.all()
        //lst = [str(obj) for obj in user.annotator.batches.all()]
        //String[] tokens = "I,am ,Legend, , oh ,you ?".split(",");
        String str_batch="";
        String substr = "";
        int len;
        ArrayList<String> arr;
        arr = new ArrayList<String>();
        pi.exec("pbatches = [str(obj) for obj in pauth_user.annotator.batches.all()]");
        
        PyList batches = (PyList)pi.get("pbatches");
        str_batch = batches.toString();
        len = str_batch.length();
        if(len > 2){
            substr = str_batch.substring(1, len - 1);
            //System.out.println("String is -- " + substr);
            //str_batch = batches.toString().substring(1,str_batch.length()-1);
            String[] tokens = substr.split(",");
            // Object[] arr = batches.toArray();
            for (String st : tokens) {
                st = st.trim();
                //System.out.println("Yo =" + st);
                arr.add(st);
            }
            
        } 
       return arr;
    }
    
    ArrayList<Integer> getAllottedBatchesId(){
        //pbatches = pauth_user.annotator.batches.all()
        //lst = [str(obj) for obj in user.annotator.batches.all()]
        //String[] tokens = "I,am ,Legend, , oh ,you ?".split(",");
        String str_batch="";
        String substr = "";
        int len;
        ArrayList<Integer> arr;
        arr = new ArrayList<Integer>();
        pi.exec("pbatches = [obj.id for obj in pauth_user.annotator.batches.all()]");
        
        PyList batches = (PyList)pi.get("pbatches");
        str_batch = batches.toString();
        len = str_batch.length();
        if(len > 2){
            substr = str_batch.substring(1, len - 1);
            //System.out.println("String is -- " + substr);
            //str_batch = batches.toString().substring(1,str_batch.length()-1);
            String[] tokens = substr.split(",");
            // Object[] arr = batches.toArray();
            for (String st : tokens) {
                st = st.trim();
                System.out.println("Yo =" + st);
                arr.add(Integer.parseInt(st));
            }
            
        } 
       return arr;
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        PortalAdapter obj = new PortalAdapter();
        ArrayList<String> arr;
        ArrayList<Integer> brr;
        if(obj.authenticate("akshat","asd"))
            {
              System.out.println("User authenticated");
              arr = obj.getAllottedBatchesName();  
              for(String sr: arr){
                  System.out.println("Name -- " + sr);
              }
              brr = obj.getAllottedBatchesId();
              for(Integer i: brr){
                  System.out.println("ID -- " + Integer.toString(i));
              }
            }
        else System.out.println("Authentication Failed");
        
    }
    
}
