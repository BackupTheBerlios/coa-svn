package org.sorbet.PortableServer; 

import java.util.HashMap; 
import org.sorbet.CORBA.Object; 
import org.sorbet.CORBA.portable.ObjectImpl; 
import org.sorbet.CORBA.SystemException; 
 
/** This exception is thrown when WrongAdapter occurred 
 *@author PortableServerTeam
 *@version 2.5
*/ 
public class WrongAdapter extends SystemException 
{ 
    /** Constructs an Exception without a message. */ 
    public WrongAdapter() 
    { 
        super(); 
    } 
 
    /** 
     * Constructs an Exception with a detailed message. 
     * @param Message The message associated with the exception. 
     */ 
    public WrongAdapter(String message) 
    { 
        super(message); 
    } 
}
