/* 
 * File    : Main.java
 * Created : 21 f�vr. 2005
 * 
 * =======================================
 * COA PROJECT ("http://coa.berlios.de")
 * =======================================
 *
 */

package fr.umlv.coa.javacard;

/**
 * @author Administrateur
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
