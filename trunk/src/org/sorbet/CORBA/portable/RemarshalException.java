package org.sorbet.CORBA.portable; 
 
/** This exception is thrown when asked operation is non defined 
 *@author Millo Jean-Vivien
 *@version 2.5 
 */ 
public class RemarshalException extends org.sorbet.CORBA.SystemException 
{ 
    /** Constructs an Exception without a message. */ 
    public RemarshalException() 
    { 
        super("MARSHAL ERROR"); 
                 
    } 
 
    /** 
     * Constructs an Exception with a detailed message. 
     * @param Message The message associated with the exception. 
     */ 
    public RemarshalException(String message) 
    { 
        super(message); 
    } 
} 
