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

import opencard.core.event.CTListener;
import opencard.core.event.CardTerminalEvent;
import opencard.core.terminal.CardTerminalException;

import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.Servant;

import fr.umlv.coa.javacard.AppletCOA;
import fr.umlv.coa.javacard.COACardInterface;


/**
 * @author Ludo
 *
 */
public final class CardCheckThread
{
	private static int COMPTEUR = 0;
	
	/** The ORB */
	private final ORB   orb;
	/** The POA */
	private final POA   poa;
	
	/** Map associating for a slot ID <-> a list of applets */
	private final Map		 slots		  = new HashMap ();
	
	/** The card listener */
	private final CTListener cardListener = new CardListener ();
	
	/**
	 * Constructor
	 * 
	 * @param orb the initial orb 
	 * @param poa the poa
	 */
	public CardCheckThread (ORB orb, POA poa)
	{
		this.orb = orb;
		this.poa = poa;
		
		System.out.println ("[COA Started]");
		
		// Register to the card listener and initialize it (by the singleton)
		COACardInterface.getInstance ().addCardInsertionListener (cardListener);
	}

	//----------------------------------------------------------//
	//------------------ PRIVATE METHODS -----------------------//
	//----------------------------------------------------------//

	/**
	 * To export the IOR
	 * 
	 * @param szName 	  the applet name
	 * @param iCardNumber the card number 
	 * @param szIOR 	  the IOR to export
	 * 
	 * @throws IOException 
	 */
	private void exportIOR (String szName, int iCardNumber, String szIOR) throws IOException
	{
		String szFileNale = szName + iCardNumber + COAParameters.COA_IOR_FILE_SUFFIX;
		BufferedWriter bw = null;
		
		try
		{
			bw = new BufferedWriter (new FileWriter (szFileNale));
			bw.write (szIOR);
		}
		finally
		{
			if (bw != null)
				bw.close ();
		}
	}
	
	
	/**
	 * To unexport an IOR reference
	 * 
	 * @param szName 	  the applet name
	 * @param iCardNumber the card number 
	 * 
	 * @throws IOException 
	 */
	private void unexportIOR (String szName, int iCardNumber) throws IOException
	{
		String szFileName = szName + iCardNumber + COAParameters.COA_IOR_FILE_SUFFIX;
		
		File f = new File (szFileName);
		f.delete ();
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
		StringBuilder szID = new StringBuilder (COAParameters.COA_MODULE_NAME);
		szID.append (COAParameters.COA_ID_DELIMITER);
		szID.append (szName);
		szID.append (COAParameters.COA_ID_DELIMITER);
		szID.append (iCardNumber);
		
		return szID.toString ().getBytes ();
	}
	
	
	/**
	 * The card listener
	 * 
	 * @author Ludo
	 *
	 */
	private final class CardListener implements CTListener
	{
		/**
		 * @see opencard.core.event.CTListener#cardInserted(opencard.core.event.CardTerminalEvent)
		 */
		public void cardInserted (CardTerminalEvent event) throws CardTerminalException
		{
			System.out.println ("CARD INSERTION -> SERVANT ACTIVATION");
			
			// The slot ID used for the slots map
			Integer slotID = new Integer (event.getSlotID ());
			
			// Get the applets list for this slot
			List list = (List) slots.get (slotID);
			
			if (list == null)
				list = new ArrayList ();
			
			Map applets = COACardInterface.getInstance ().getAppletMap ();
			
			for (Iterator it = applets.keySet ().iterator (); it.hasNext ();)
			{
				String  	szName  = (String) it.next ();
				AppletCOA	applet  = (AppletCOA) applets.get (szName);
				
				Servant servant = new CardServant (applet);
				
				try
				{
					byte [] oid	= generate_card_id (szName, slotID.intValue ());
					
					System.out.println ("OID DU SERVANT INSERE : " + new String (oid));
					
					poa.activate_object_with_id (oid, servant);
					
					exportIOR (szName, slotID.intValue (), orb.object_to_string (poa.id_to_reference (oid)));
					
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
		 * @see opencard.core.event.CTListener#cardRemoved(opencard.core.event.CardTerminalEvent)
		 */
		public void cardRemoved (CardTerminalEvent event) throws CardTerminalException
		{
			System.out.println ("CARD INSERTION -> SERVANT DESACTIVATION");
			
			// The slot ID used for the slots map
			Integer slotID = new Integer (event.getSlotID ());
			
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
					
					unexportIOR (s.szName, slotID.intValue ());
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
