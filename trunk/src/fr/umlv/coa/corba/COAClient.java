/* 
 * File    : COAClient.java
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

import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.Request;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;

/**
 * @author Ludo
 *
 */
public final class COAClient
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
		
		BufferedReader reader = new BufferedReader (new FileReader ("Test00Ref"));
		szRef = reader.readLine ();
		reader.close ();
		
		Object  object = orb.string_to_object (szRef);
		Request req	   = object._request ("hello");		
		
		TypeCode retObject = orb.get_primitive_tc (TCKind.tk_string);
		req.set_return_type (retObject);
		//req.set_return_type (orb.create_interface_tc ("IDL:COA/Test0-0:1.0", "Test"));
		req.invoke ();
		
		Any result   = req.return_value ();
		String szRes = result.extract_string (); 
		
		System.out.println ("RESULTAT : " + szRes);
		
		//System.out.println (result.extract_octet ());
		
		/*while (true)
		{
			System.out.println ("OCTET : " + (char) result.extract_octet ());
		}*/
		
	}
}
