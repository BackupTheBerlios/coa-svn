/* 
 * File    : CardProxyHandler.java
 * Created : 16 févr. 2005
 * 
 * =======================================
 * COA PROJECT ("http://coa.berlios.de")
 * =======================================
 *
 */

package fr.umlv.coa;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Ludo
 *
 */
public class CardProxyHandler implements InvocationHandler
{

	/**
	 * 
	 */
	public CardProxyHandler ()
	{
		super ();
	}

	/**
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	public Object invoke (final Object proxy, final Method method, final Object [] args) throws Throwable
	{
		System.out.println ("PROXY  : " + proxy);
		System.out.println ("METHOD : " + method);
		 
		return null;
	}

}
