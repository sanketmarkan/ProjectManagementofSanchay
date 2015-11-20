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
    String username;
    ArrayList<String> batch_names;
    ArrayList<Integer> batch_ids;
    
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
        username = new String();
        //PyString str = (PyString)pi.get("temp");
        //System.out.println("Magic -- " + str.asString());
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
            this.username = username;
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
       this.batch_names = arr;
       return arr;
    }
    
    ArrayList<String> getBatchFilesName(int batchid){
        //	batch = get_object_or_404(Batch, pk=batch_id)
	//documents = batch.document_set.all()
        int temp;
        String str_batch="";
        String substr = "";
        int len;
        ArrayList<String> arr;
        arr = new ArrayList<String>();
        pi.exec("from django.shortcuts import render, get_object_or_404, redirect");
        pi.set("pbatchid", new PyInteger(batchid));
        pi.exec("pbatch = get_object_or_404(Batch, pk=pbatchid)");
        pi.exec("pdocuments = pbatch.document_set.all()");
        pi.exec("pdocuments = [str(obj) for obj in pdocuments]");
        PyList batches = (PyList)pi.get("pdocuments");
        str_batch = batches.toString();
        System.out.println("p- " + str_batch);
        len = str_batch.length();
        if(len > 2){
            substr = str_batch.substring(1, len - 1);
            //System.out.println("String is -- " + substr);
            //str_batch = batches.toString().substring(1,str_batch.length()-1);
            String[] tokens = substr.split(",");
            // Object[] arr = batches.toArray();
            for (String st : tokens) {
                st = st.trim();
                System.out.println("b- " + st);
                temp = st.indexOf("/");
                len = st.length();
                st = st.substring(temp+1,len-1);
                System.out.print("q- " + st );
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
       this.batch_ids = arr;
       return arr;
    }
    
    int getBatchId(String batchname){
        //cheese_blog = Blog.objects.get(name="Cheddar Talk")
        //PyList batches = (PyList)pi.get("pbatches");
        pi.set("pbname", new PyString(batchname));
        pi.exec("pid = Batch.objects.get(name=pbname)");
        pi.exec("ptemp = pid.id");
        PyInteger id = (PyInteger)pi.get("ptemp");
        int temp = id.asInt();
        return temp;
    }
    
    String getBatchDeadline(String batchname){
        String temp = new String();
        pi.exec("from django.contrib.auth.models import User, Group, Permission");
        pi.set("pbatch_name", new PyString(batchname));
        pi.set("pusername", new PyString(this.username));
        pi.exec("pu = User.objects.get(username=pusername)");
        pi.exec("pa = Annotator.objects.get(user = pu)");
        pi.exec("pb = Batch.objects.get(name = pbatch_name)");
        pi.exec("pd = Deadline.objects.get(annotator = pa,batch = pb)");
        pi.exec("ptemp = str(pd.final_date)");
        //PyList batches = (PyList)pi.get("pbatches");
        PyString dd = (PyString)pi.get("ptemp");
        temp = dd.asString();
        return temp;
    }
    
    boolean getBatchStatus(String batchname){
        pi.set("pbatch_name", new PyString(batchname));
        pi.exec("pb = Batch.objects.get(name = pbatch_name)");
        pi.exec("ptemp = pb.status");
        PyString sd = (PyString)pi.get("ptemp");
        System.out.println("QWE -- " + sd.asString());
        return sd.asString().equalsIgnoreCase("DONE");
    }
    
    void setBatchStatus(String batchname, boolean status){
        pi.set("pbatch_name", new PyString(batchname));
        pi.exec("pb = Batch.objects.get(name = pbatch_name)");
        if(status == true)pi.exec("pb.status = u'DONE'");
        else pi.exec("pb.status = u'NOT_DONE'");
       
        pi.exec("pb.save()");
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
              int i = obj.getBatchId("English Batch");
              System.out.println("English Batch id is -- " + i);
              ArrayList<String> tpq = new ArrayList<String>();
              tpq = obj.getBatchFilesName(i);
              for(String st : tpq)
                  System.out.println("File -- "+st);
              System.out.println("Its deadline is - " + obj.getBatchDeadline("English Batch"));
              System.out.println("Status of English Batch " + obj.getBatchStatus("English Batch"));
              System.out.println("Setting status to true");
              obj.setBatchStatus("English Batch",true);
              System.out.println("Status of English Batch " + obj.getBatchStatus("English Batch"));
              
            }
        else System.out.println("Authentication Failed");
        
    }
    
}
