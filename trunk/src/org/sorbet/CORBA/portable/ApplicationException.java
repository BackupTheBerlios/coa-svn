package org.sorbet.CORBA.portable; 
 
/** This exception is thrown when asked operation is non defined
*@author Portable team
*@version 2.5 
 */ 
public class ApplicationException extends org.sorbet.CORBA.SystemException 
{ 
    /** Constructs an Exception without a message. */ 
    public ApplicationException() 
    { 
        super("l'execution de la requ�te s'est mal pass�"); 
                 
    } 
 
    /** 
     * Constructs an Exception with a detailed message. 
     * @param Message The message associated with the exception. 
     */ 
    public ApplicationException(String message) 
    { 
        super(message); 
         
    } 
     
    public org.sorbet.CORBA.portable.InputStream getInputStream () 
    {return null;} 
    public String getId () 
	{return null;} 
} 
