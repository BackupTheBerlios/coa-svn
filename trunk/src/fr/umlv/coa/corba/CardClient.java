/* 
 * File    : CardClient.java
 * Created : 24 févr. 2005
 * 
 * =======================================
 * COA PROJECT ("http://coa.berlios.de")
 * =======================================
 *
 */

package fr.umlv.coa.corba;

import java.io.BufferedReader;
import java.io.FileReader;

import org.omg.CORBA.ORB;

import fr.umlv.coa.test.test.MonApplet;
import fr.umlv.coa.test.test.MonAppletHelper;

/**
 * @author Ludo
 *
 */
public final class CardClient
{

	/**
	 * The main entry
	 * 
	 * @param args the program arguments
	 * @throws Exception
	 */
	public static void main (String [] args) throws Exception
	{
		ORB orb = ORB.init (args, null);
		
		String szRef = null;
		
		BufferedReader reader = new BufferedReader (new FileReader ("MonApplet0Ref"));
		szRef = reader.readLine ();
		reader.close ();
		
		MonApplet applet = MonAppletHelper.unchecked_narrow (orb.string_to_object (szRef));
		
		System.out.println ("Affichage du nom : " + applet.getName ());
		System.out.println ("--------------------------------------");
		System.out.println ("Affichage du nombre : " + applet.getNumber ());
		System.out.println ("--------------------------------------");
	}
}
