package org.sorbet.CORBA; 
 
/** This exception is thrown when System occurred 
 * @author  ORBteam & Millo Jean-Vivien
 * @version	2.5
*/ 
public class SystemException extends RuntimeException  
{ 
    /** Constructs an Exception without a message. */ 
    public SystemException() 
    { 
        super(); 
                 
    } 
 
    /** 
     * Constructs an Exception with a detailed message. 
     * @param Message The message associated with the exception. 
     */ 
    public SystemException(String message) 
    { 
        super(message); 
         
    } 
} 
