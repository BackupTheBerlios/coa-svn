package org.sorbet.PortableServer; 
 
import org.sorbet.CORBA.SystemException; 
 
/** This exception is thrown when AdapterAlreadyExist occurred 
 * @author PortableServerTeam 
 * @version 2.5
 */ 
public class AdapterAlreadyExists extends SystemException 
{ 
    /** Constructs an Exception without a message. */ 
    public AdapterAlreadyExists() 
    { 
        super(); 
    } 
 
    /** 
     * Constructs an Exception with a detailed message. 
     * @param Message The message associated with the exception. 
     */ 
    public AdapterAlreadyExists(String message) 
    { 
        super(message); 
    } 
}
