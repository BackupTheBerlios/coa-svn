package org.sorbet.PortableServer; 
 
import org.sorbet.CORBA.SystemException; 
 
/** This exception is thrown when AdapterAlreadyExist occurred
 * @author PortableServerTeam 
 * @version 2.5
 */ 
public class BAD_INV_ORDER extends SystemException 
{ 
    /** Constructs an Exception without a message. */ 
    public BAD_INV_ORDER() 
    { 
        super(); 
    } 
 
    /** 
     * Constructs an Exception with a detailed message. 
     * @param Message The message associated with the exception. 
     */ 
    public BAD_INV_ORDER(String message) 
    { 
        super(message); 
    } 
}
