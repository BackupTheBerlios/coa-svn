package org.sorbet.PortableServer; 
 
import java.util.HashMap; 
import org.sorbet.CORBA.Object; 
import org.sorbet.CORBA.portable.ObjectImpl; 
import org.sorbet.CORBA.SystemException; 
 
//fin de la classe POAImpl 
     
 
/** This exception is thrown when InvalidPolicy occurred 
 * @author PortableServerTeam 
 * @version 2.5
*/ 
public class InvalidPolicy extends SystemException 
{ 
    /** Constructs an Exception without a message. */ 
    public InvalidPolicy() 
    { 
        super(); 
    } 
 
    /** 
     * Constructs an Exception with a detailed message. 
     * @param Message The message associated with the exception. 
     */ 
    public InvalidPolicy(String message) 
    { 
        super(message); 
    } 
}
