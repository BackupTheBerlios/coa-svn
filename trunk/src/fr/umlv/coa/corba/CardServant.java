/* 
 * File    : CardServant.java
 * Created : 18 févr. 2005
 * 
 * =======================================
 * COA PROJECT ("http://coa.berlios.de")
 * =======================================
 *
 */

package fr.umlv.coa.corba;

import java.io.IOException;

import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.InvokeHandler;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ResponseHandler;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.Servant;

import fr.umlv.coa.javacard.AppletCOA;
import fr.umlv.coa.javacard.COACardInterface;


/**
 * The generic card servant
 * 
 * @author Ludo
 *
 */
public final class CardServant extends Servant implements InvokeHandler
{
	/** The IDS prefix */
	private static final String IDS_PREFIX = "IDL:" + COAParameters.COA_MODULE_NAME + "/";
	/** The IDS suffix */
	private static final String IDS_SUFFIX = ":1.0";
	
	/** The card applet */
	private final AppletCOA applet;
	
	//----------------------------------------------------------//
	//------------------- CONSTRUCTORS -------------------------//
	//----------------------------------------------------------//

	/**
	 * Constructor with the applet
	 * 
	 * @param applet the applet
	 */
	public CardServant (AppletCOA applet)
	{
		this.applet = applet;
	}
	
	//----------------------------------------------------------//
	//------------------- PUBLIC METHODS -----------------------//
	//----------------------------------------------------------//

	/**
	 * @see org.omg.CORBA.portable.InvokeHandler#_invoke(java.lang.String, org.omg.CORBA.portable.InputStream, org.omg.CORBA.portable.ResponseHandler)
	 */
	public OutputStream _invoke (String method, InputStream input, ResponseHandler handler) throws SystemException
	{
		if (method == null)
			return null;

		System.out.println ("METHOD APPELLEE : " + method);
		
		OutputStream out = handler.createReply ();

		try
		{
			// Construction de l'argument
			byte [] arg = new byte [input.available ()];
			input.read (arg, 0, arg.length);
			
			byte [] res = COACardInterface.getInstance ().invoke (applet.getName (), method, arg);

			if (res == null)
				return null;
			
			//byte [] 	 res = "toto".getBytes ();
			
			/*for (int i = 0; i < res.length; ++i)
				out.write_octet (res [i]);*/
			
			out.write_string (new String (res));
		}
		catch (IOException e)
		{
			e.printStackTrace ();
		}

		return out;
	}
	

	/**
	 * @see org.omg.PortableServer.Servant#_all_interfaces(org.omg.PortableServer.POA, byte[])
	 */
	public String [] _all_interfaces (POA poa, byte [] objectId)
	{
		String szID = new String (objectId);
		
		if (!szID.startsWith (COAParameters.COA_MODULE_NAME + COAParameters.COA_ID_DELIMITER))
			return null;
		
		int iIndex  = (COAParameters.COA_MODULE_NAME + COAParameters.COA_ID_DELIMITER).length ();
		
		// Recuperation du nom
		String szName = szID.substring (iIndex);
		
		System.out.println (IDS_PREFIX + szName + IDS_SUFFIX);
		
		return new String [] {IDS_PREFIX + szName + IDS_SUFFIX};
	}
}
