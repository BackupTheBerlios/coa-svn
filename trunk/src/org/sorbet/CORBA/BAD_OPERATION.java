package org.sorbet.CORBA; 
 
/** This exception is thrown when asked operation is non defined 
 *@author ORB team
 *@version 2.5
 */ 
public class BAD_OPERATION extends SystemException 
{ 
    /** Constructs an Exception without a message. */ 
    public BAD_OPERATION(int id,int aaa) 
    { 
        super("operation non deinie"); 
    } 
 
    /** 
     * Constructs an Exception with a detailed message. 
     * @param Message The message associated with the exception. 
     */ 
    public BAD_OPERATION(String message) 
    { 
        super(message); 
         
    } 
}
