/* 
 * File    : COAPOA.java
 * Created : 14 févr. 2005
 * 
 * =======================================
 * COA PROJECT ("http://coa.berlios.de")
 * =======================================
 *
 */

package fr.umlv.coa;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.sorbet.PortableServer.POAImpl;
import org.sorbet.PortableServer.Servant;
import org.sorbet.PortableServer.ServantAlreadyActive;
import org.sorbet.PortableServer.WrongPolicy;

/**
 * @author Ludo
 *
 */
public final class COA extends POAImpl 
{
	
	public byte [] activate_object (Servant p_servant) throws ServantAlreadyActive, WrongPolicy
	{
		if (p_servant == null)
			return null;
		
		Servant tmp = p_servant;
		
		if (p_servant instanceof CardApplet && ((CardApplet) p_servant).isOnCard ())
		{
			System.out.println ("CARD APPLET");
			
			InvocationHandler handler = new CardProxyHandler (); 
			
			// Create Remote Proxy
			tmp = (Servant) Proxy.newProxyInstance (p_servant.getClass ().getClassLoader (), new Class [] {p_servant.getClass ()}, handler);
		}
		
		return super.activate_object (tmp);
	}
}
