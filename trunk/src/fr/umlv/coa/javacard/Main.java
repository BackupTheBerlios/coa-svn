/* 
 * File    : Main.java
 * Created : 21 févr. 2005
 * 
 * =======================================
 * COA PROJECT ("http://coa.berlios.de")
 * =======================================
 *
 */

package fr.umlv.coa.javacard;

/**
 *
 */
public class Main
{

	public static void main (String [] args)
	{
		COACardInterface i = COACardInterface.getInstance();
		
		i.init();		
	}
}