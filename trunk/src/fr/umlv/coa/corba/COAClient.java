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

import opencard.core.util.HexString;

import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.Request;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;

import fr.umlv.coa.javacard.MonApplet;
import fr.umlv.coa.monapplet.MonAppletHelper;

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
		
		BufferedReader reader = new BufferedReader (new FileReader ("MonApplet0Ref"));
		szRef = reader.readLine ();
		reader.close ();
		
		Object  object = orb.string_to_object (szRef);
		
		fr.umlv.coa.monapplet.MonApplet applet = MonAppletHelper.unchecked_narrow (object);
		
		byte [] res = applet.getName ();
		
		int iIndex = 0;
		
		for (; res [iIndex] != 0; iIndex ++);
		
		System.out.println ("NAME " + new String (res, 0, iIndex));
		
		//System.out.println (HexString.hexify(res));

		System.out.println ("--------------------------------------");
		
		res = applet.getNumber ();
		
		int iRes = res [0] << 8 | res [1];
		
		System.out.println ("RESULTAT : " + iRes);
		
		
		//System.out.println (HexString.hexify(res));

		
		//System.out.println (new String (applet.getName ()));
		
		
		//System.out.println (HexString.hexify(applet.getArg()));
		
		//applet.setArg((byte)0x28);
		
		//System.out.println (HexString.hexify(applet.getArg()));
		
//		
//		Request req	   = object._request ("getName");		
//		
//		TypeCode retObject = orb.get_primitive_tc (TCKind.tk_string);
//		req.set_return_type (retObject);
//		//req.set_return_type (orb.create_interface_tc ("IDL:COA/Test0-0:1.0", "Test"));
//		req.invoke();
//		
//		Any result   = req.return_value ();
//		String Res = result.extract_string(); 
//		
//		System.out.println ("RESULTAT : " + Res);
//		
//		//System.out.println (result.extract_octet ());
		
		/*while (true)
		{
			System.out.println ("OCTET : " + (char) result.extract_octet ());
		}*/
		
	}
}
