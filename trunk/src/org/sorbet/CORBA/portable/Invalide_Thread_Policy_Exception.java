package org.sorbet.CORBA.portable; 
 
 
 
/** 
 *Cette exception se lance quand la politique de Thread est invalide
 *@author Millo Jean-Vivien
 *@version 2.5  
 **/ 
public class Invalide_Thread_Policy_Exception extends org.sorbet.CORBA.SystemException 
{ 
    /** 
     *Constructeur par defaut 
     **/ 
    public Invalide_Thread_Policy_Exception() 
    { 
        super(); 
                 
    } 
    /** 
     * Constructeur 
     * @param Message Le message lancé avec l'exception. 
     */ 
    public Invalide_Thread_Policy_Exception(String message) 
    { 
        super(message); 
         
    } 
}
