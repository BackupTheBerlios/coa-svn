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

import fr.umlv.coa.javacard.CardApplet;
import fr.umlv.coa.terminal.CardTerminalInterface;
import fr.umlv.coa.terminal.gemplus.GemplusCardTerminalInterface;


/**
 * The generic card servant
 * 
 * @author Ludo
 *
 */
public final class CardServant extends Servant implements InvokeHandler
{
	/** The IDS prefix */
	private static final String IDS_PREFIX = "IDL:" + CardServantParameters.MODULE_NAME + "/";
	/** The IDS suffix */
	private static final String IDS_SUFFIX = ":1.0";
	
	/** The card applet */
	private final CardApplet applet;
	/** The card terminal interface */
	private final CardTerminalInterface terminal;
	
	//----------------------------------------------------------//
	//------------------- CONSTRUCTORS -------------------------//
	//----------------------------------------------------------//

	/**
	 * Constructor with the applet
	 * 
	 * @param applet the applet
	 * @param terminal the card terminal interface
	 */
	public CardServant (CardApplet applet, CardTerminalInterface terminal)
	{
		this.applet = applet;
		this.terminal = terminal;
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
			//FIXME Regler la recuperation des arguments
			byte [] arg = new byte [input.available ()];
			
			// Appel de la methode et Recuperation du resultat
			byte [] res = terminal.invoke (applet.getName (), method, arg);

			if (res == null)
				return out;
			
			/*System.arraycopy (res, 0, ret, 0, res.length);
			
			//byte [] 	 res = "toto".getBytes ();
			
			out.write_octet_array (ret, 0, ret.length);
			
			//for (int i = 0; i < res.length; ++i)
				//out.write_octet (res [i]);
			
			//out.write_string (new String (res));*/
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
		
		if (!szID.startsWith (CardServantParameters.MODULE_NAME + CardServantParameters.ID_DELIMITER))
			return null;
		
		int iIndex  = (CardServantParameters.MODULE_NAME + CardServantParameters.ID_DELIMITER).length ();
		
		// Recuperation du nom
		String szName = szID.substring (iIndex);
		
		System.out.println (IDS_PREFIX + szName + IDS_SUFFIX);
		
		return new String [] {IDS_PREFIX + szName + IDS_SUFFIX};
	}
}
