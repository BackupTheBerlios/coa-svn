package org.sorbet.PortableServer; 
 
import org.sorbet.CORBA.Object; 
/**
 *@author PortableServerTeam
 *@version 2.5
 */
public  class POAHelper 
{ 
    static POA lePoa = null ; 
 
	public static void setPOA(POA value) 
    { 
		lePoa = value ; 
    } 
 
    public static POA narrow() 
    { 
        return lePoa ; 
    } 
 
    public static POA narrow( org.sorbet.CORBA.Object obj ) 
    { 
        return (POA)obj ; 
    } 
 
}
