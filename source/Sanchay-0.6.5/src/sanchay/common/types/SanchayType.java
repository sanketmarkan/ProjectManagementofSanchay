/*
 * SanchayType.java
 *
 * Created on October 21, 2005, 1:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package sanchay.common.types;

import java.io.*;
import java.util.*;
import sanchay.corpus.ssf.tree.*;

/**
 *
 *  @author Anil Kumar Singh Kumar Singh
 */
public class SanchayType implements Serializable {

    protected String id;
    protected SanchayType prev;
    protected SanchayType next;
    protected String java_package;

    protected SanchayType(String id, String pk) {

        this.id = id;
        this.java_package = pk;
    }

    public static int size()
    {
        return -1;
    }
    
    public static SanchayType first()
    {
        return null;
    }
    
    public static SanchayType last()
    {
        return null;
    }

    public static SanchayType getType(int i)
    {
        return null;
    }

    public static Enumeration elements()
    {
        return null;
    }

    public static SanchayType findFromClassName(String className)
    {
        return null;
    }

    public static SanchayType findFromId(String i)
    {
        return null;
    }

    public static SanchayType findFromClassName(Enumeration enm, String className)
    {
        SanchayType dt = null;

        while(enm.hasMoreElements())
        {
            dt = (SanchayType) enm.nextElement();

            if(className.equals(dt.getClassName()))
                return dt;
        }

        return null;
    }

    public static SanchayType findFromId(Enumeration enm, String i)
    {
        SanchayType dt = null;

        while(enm.hasMoreElements())
        {
            dt = (SanchayType) enm.nextElement();

            if(i.equals(dt.toString()))
                return dt;
        }

        return null;
    }

    public static void init()
    {
    }

    public String getId() { return this.id; }

    public String toString() { return this.id; }
    
    public boolean equals(Object obj)
    {
	if(obj == null)
	    return false;

	return getClassName().equalsIgnoreCase( ((SanchayType) obj).getClassName() );
    }
    
    public String getClassName() { return (java_package + "." + this.id); }
    
    public SanchayType prev()  { return this.prev; }
    public SanchayType next()  { return this.next; }
}
    
//class TypeEnumerator implements Enumeration
//{
//    protected SanchayType curr;
//
//    protected TypeEnumerator(SanchayType f)
//    {
//        curr = f;
//    }
//
//    public boolean hasMoreElements() {
//        boolean ret = false;
//
//        if(curr != null)
//            ret = true;
//        else
//        {
//            ret = false;
//            curr = null;
//        }
//
//        return ret;
//    }
//
//    public Object nextElement() {
//        SanchayType c = curr;
//        curr = curr.next();
//        return c;
//    }
//}
