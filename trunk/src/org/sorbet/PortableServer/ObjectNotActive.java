package org.sorbet.PortableServer;
 
import java.util.HashMap; 
import org.sorbet.CORBA.Object; 
import org.sorbet.CORBA.portable.ObjectImpl; 
import org.sorbet.CORBA.SystemException; 
 
/**
 * @author PortableServerTeam 
 * @version 2.5
 */
public class ObjectNotActive extends SystemException 
    { 
        /** Constructs an Exception without a message. */ 
    public ObjectNotActive() 
    { 
        super(); 
    } 
 
    /** 
     * Constructs an Exception with a detailed message. 
     * @param Message The message associated with the exception. 
     */ 
    public ObjectNotActive(String message) 
	{ 
        super(message); 
    } 
}
