/* 
 * File    : COA.java
 * Created : 14 févr. 2005
 * 
 * =======================================
 * COA PROJECT ("http://coa.berlios.de")
 * =======================================
 *
 */

package fr.umlv.coa.corba;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.Servant;

import fr.umlv.coa.javacard.CardApplet;
import fr.umlv.coa.terminal.CardTerminalInterface;
import fr.umlv.coa.terminal.CardTerminalListener;


/**
 * @author Ludo
 *
 */
public final class CardManager
{
	/** The ORB */
	private final ORB   orb;
	/** The POA */
	private final POA   poa;
	
	/** The naming context */
	private final NamingContextExt naming;
	
	/** Map associating for a slot ID <-> a list of applets */
	private final Map slots = new HashMap ();
	
	/** The card listener */
	private final CardTerminalListener cardListener = new CardListener ();
	/** The card terminal */
	private final CardTerminalInterface terminal;	
	
	/**
	 * Constructor
	 * 
	 * @param orb 	 the initial orb 
	 * @param poa 	 the poa
	 * @param naming the naming context
	 * @param terminal the card terminal
	 */
	public CardManager (ORB orb, POA poa, NamingContextExt naming, CardTerminalInterface terminal)
	{
		this.orb = orb;
		this.poa = poa;
		
		this.naming = naming;
		this.terminal = terminal;
		
		System.out.println ("[Card Manager Started]");
		
		// Register to the card listener and initialize it (by the singleton)
		terminal.addCardTerminalListener (cardListener);
	}

	//----------------------------------------------------------//
	//------------------ PRIVATE METHODS -----------------------//
	//----------------------------------------------------------//

	/**
	 * To export the IOR
	 * 
	 * @param szName 	  the applet name
	 * @param iCardNumber the card number 
	 * @param object 	  the object to export
	 * 
	 * @throws Exception 
	 */
	private void exportObject (String szName, int iCardNumber, Object object) throws Exception
	{
		exportObjectToFile (szName, iCardNumber, object);
		
		if (naming != null)
			exportObjectToNamingService (szName, iCardNumber, object);
	}
	
	/**
	 * To export the IOR
	 * 
	 * @param szName 	  the applet name
	 * @param iCardNumber the card number 
	 * @param object 	  the object to export
	 * 
	 * @throws Exception 
	 */
	private void exportObjectToFile (String szName, int iCardNumber, Object object) throws Exception
	{
		String szFileName = szName + iCardNumber + CardServantParameters.IOR_FILE_SUFFIX;
		BufferedWriter bw = null;
		
		try
		{
			bw = new BufferedWriter (new FileWriter (szFileName));
			bw.write (orb.object_to_string (object));
		}
		finally
		{
			if (bw != null)
				bw.close ();
		}
	}
	
	
	/**
	 * To export the IOR
	 * 
	 * @param szName 	  the applet name
	 * @param iCardNumber the card number 
	 * @param object 	  the object to export
	 * 
	 * @throws Exception 
	 */
	private void exportObjectToNamingService (String szName, int iCardNumber, Object object) throws Exception
	{
		String szRegistrationName = szName + iCardNumber;
		
		NameComponent [] name = naming.to_name (szRegistrationName);
		naming.rebind (name, object);
	}	
	
	/**
	 * To unexport a registered objet
	 * 
	 * @param szName 	  the applet name
	 * @param iCardNumber the card number 
	 * 
	 * @throws Exception 
	 */
	private void unexportObject (String szName, int iCardNumber) throws Exception
	{
		unexportOnFile (szName, iCardNumber);
		
		if (naming != null)
			unexportOnNamingContext (szName, iCardNumber);
	}
	
	
	/**
	 * To unexport an IOR reference
	 * 
	 * @param szName 	  the applet name
	 * @param iCardNumber the card number 
	 * 
	 * @throws IOException 
	 */
	private void unexportOnFile (String szName, int iCardNumber) throws IOException
	{
		String szFileName = szName + iCardNumber + CardServantParameters.IOR_FILE_SUFFIX;
		
		File f = new File (szFileName);
		f.delete ();
	}	
	
	/**
	 * To unexport an IOR reference
	 * 
	 * @param szName 	  the applet name
	 * @param iCardNumber the card number 
	 * 
	 * @throws Exception 
	 */
	private void unexportOnNamingContext (String szName, int iCardNumber) throws Exception
	{
		String szRegistrationName = szName + iCardNumber;
		
		naming.unbind (naming.to_name (szRegistrationName));
	}	
	
	/**
	 * To generate the servant ID
	 * 
	 * @param szName 		the applet name
	 * @param iCardNumber	the card number
	 * @return the generated id
	 */
	private byte [] generate_card_id (String szName, int iCardNumber)
	{
		StringBuilder szID = new StringBuilder (CardServantParameters.MODULE_NAME);
		szID.append (CardServantParameters.ID_DELIMITER);
		szID.append (szName);
		szID.append (CardServantParameters.ID_DELIMITER);
		szID.append (iCardNumber);
		
		return szID.toString ().getBytes ();
	}
	
	
	/**
	 * The card listener
	 * 
	 * @author Ludo
	 *
	 */
	private final class CardListener implements CardTerminalListener
	{
		/**
		 * @see CardTerminalListener#cardInserted(String, int)
		 */
		public void cardInserted (String szTerminalName, int iSlot)
		{
			System.out.println ("CARD INSERTION -> SERVANT ACTIVATION");
			
			// The slot ID used for the slots map
			Integer slotID = new Integer (iSlot);
			
			// Get the applets list for this slot
			List list = (List) slots.get (slotID);
			
			if (list == null)
				list = new ArrayList ();
			
			Map applets = terminal.getAppletsMap ();
			
			for (Iterator it = applets.keySet ().iterator (); it.hasNext ();)
			{
				String  	szName  = (String) it.next ();
				CardApplet	applet  = (CardApplet) applets.get (szName);
				
				Servant servant = new CardServant (applet, terminal);
				
				try
				{
					byte [] oid	= generate_card_id (szName, slotID.intValue ());
					
					System.out.println ("OID DU SERVANT INSERE : " + new String (oid));
					
					poa.activate_object_with_id (oid, servant);
					
					exportObject (szName, slotID.intValue (), poa.id_to_reference (oid));
					
					list.add (new ServantNameOID (oid, szName));
				}
				catch (Exception e)
				{
					e.printStackTrace ();
				}
			}
			
			slots.put (slotID, list);			
		}

		/**
		 * @see CardTerminalListener#cardRemoved(String, int)
		 */
		public void cardRemoved (String szTerminalName, int iSlot)
		{
			System.out.println ("CARD INSERTION -> SERVANT DESACTIVATION");
			
			// The slot ID used for the slots map
			Integer slotID = new Integer (iSlot);
			
			// Get the applets list for this slot
			List list   = (List) slots.get (slotID);
			
			if (list == null)
				return;
			
			for (Iterator it = list.iterator (); it.hasNext ();)
			{
				try
				{
					ServantNameOID s = (ServantNameOID) it.next (); 
					
					poa.deactivate_object (s.oid);
					
					unexportObject (s.szName, slotID.intValue ());
				}
				catch (Exception e)
				{
					e.printStackTrace ();
				}
			}
			
			slots.remove (slotID);
		}
	}
	
	
	
	/**
	 * Class containing the oid and the name of a servant 
	 * 
	 * @author Ludo
	 *
	 */
	private static final class ServantNameOID
	{
		/** The servant id */
		final byte [] oid;
		/** The servant name */
		final String  szName;
		
		/**
		 * Constructor
		 * 
		 * @param oid 
		 * @param szName 
		 */
		public ServantNameOID (byte [] oid, String szName)
		{
			this.oid 	= oid;
			this.szName	= szName;
		}
	}
	
}
