/* 
 * File    : COACardInterface.java
 * Created : 24 févr. 2005
 * 
 * =======================================
 * COA PROJECT ("http://coa.berlios.de")
 * =======================================
 *
 */

package fr.umlv.coa.javacard;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import opencard.core.event.CTListener;
import opencard.core.event.CardTerminalEvent;
import opencard.core.event.EventGenerator;
import opencard.core.service.SmartCard;
import opencard.core.terminal.CardTerminal;
import opencard.core.terminal.CardTerminalException;
import opencard.core.terminal.CardTerminalRegistry;
import opencard.core.terminal.ResponseAPDU;
import opencard.opt.applet.AppletID;

import com.gemplus.opencard.service.op.CardObjectStatus;
import com.gemplus.opencard.service.op.CardServiceOPCore;
import com.gemplus.opencard.service.op.Result;
import com.gemplus.opencard.service.op.vop.VOPAuthenticationInput;
import com.gemplus.opencard.service.op.vop.vop211.CardServiceVOP211;
import com.gemplus.tools.gemxpresso.GemXpressoException;
import com.gemplus.tools.gemxpresso.GemXpressoService;
import com.gemplus.tools.gemxpresso.util.GxpSystem;


/**
 * Interface between the COA and the card
 * 
 * @author Ludo
 *
 */
public final class COACardInterface
{
	/** The opencard home directory */ 
	private static final String	HOME_DIR 	= "c:\\gemplus\\gemxpresso.rad3";
	/** The card reader */
	private static final String	CARD_TARGET	= "gempc410_com1";
	/** The card profile name */
	private static final String	CARD_KEY_FILE = "GXP211_PK.properties";
	/** The framework type */
	private static final String	FRAMEWORK_TYPE = "GXP211_PK";

	/** The card get name byte value */
	private final static byte		CARD_GET_NAME		= (byte) 0xFF;
	/** The card get instruction */
	private final static byte		CARD_GET_INS		= (byte) 0xFE;
	/** The card get instruction name */
	private final static byte		CARD_GET_INS_NAME	= (byte) 0xFD;
	/** The card get instruction return type */
	private final static byte		CARD_GET_INS_TYPE	= (byte) 0xFC;
	
	
	/** The Singleton instance */
	private static COACardInterface	INSTANCE = null;

	/** The applet map */
	private final Map appletMap = new TreeMap ();
	/** The listeners list */
	private final List listListener	= new ArrayList ();

	/** The card service core */
	private CardServiceOPCore		serv;
	/** The gemxpresso service */
	private GemXpressoService		libService;

	/** The card terminal */
	private CardTerminal			terminal			= null;
	/** The smartcard */
	private SmartCard				card				= null;
	/** The slot id */
	private int						slotId				= 0;

	/** The key file directory */
	private String					keyFileDir;
	/** The profile directory */
	private String					profileDir;
	/** The key file */
	private String					keyFile;

	/** The security domain AID */
	private byte []					securityDomainAID	= null;
	/** The key set version */
	private int						keySetVersion		= 13;
	/** The key index */
	private int						keyIndex			= 0;
	/** To know if there is some enciphering */
	private boolean					isEnciphered		= false;
	/** To know if there is macing */
	private boolean					isMacing			= false;

	/** The application key file */
	private String					appKeyFile;
	/** The application key set version */
	private int						appKeySetVersion	= 13;
	/** The application key index */
	private int						appKeyIndex			= 0;

	/** Applet return Values */
	private static final byte		BYTE_APPLET			= 0;
	private static final byte		SHORT_APPLET		= 1;
	private static final byte		INT_APPLET			= 2;
	private static final byte		LONG_APPLET			= 3;
	private static final byte		STRING_APPLET		= 4;
	private static final byte		ARRAY_APPLET		= 5;
	private static final byte		VOID_APPLET			= 6;
	
	
	//----------------------------------------------------------//
	//--------------------- CONSTRUCTORS -----------------------//
	//----------------------------------------------------------//

	/**
	 * Constructor
	 * 
	 */
	public COACardInterface ()
	{
		init ();
	}
	
	
	//----------------------------------------------------------//
	//------------------- PUBLIC METHODS -----------------------//
	//----------------------------------------------------------//

	/**
	 * To get the singleton instance
	 *  
	 * @return the singleton instance
	 */
	public static COACardInterface getInstance ()
	{
		if (INSTANCE == null)
			INSTANCE = new COACardInterface ();

		return INSTANCE;
	}

	
	/**
	 * To initialize the card
	 * 
	 */
	public void init ()
	{
		GxpSystem.getInstance ().setRadHome (HOME_DIR);

		keyFileDir = System.getProperty ("gemplus.gemxpresso.rad.home.resources.targets");
		profileDir = System.getProperty ("gemplus.gemxpresso.rad.home.resources.cardprofile");
		System.setProperty ("user.dir" , System.getProperty ("gemplus.gemxpresso.rad.home.conf"));

		System.out.println ("COACardInterface started");

		initOCF ();

		EventGenerator.getGenerator ().addCTListener (new CardListener ());
	}
	
	/**
	 * To stop the card reading
	 * 
	 */
	public void stop()
	{
		stopOCF ();

		System.out.println ("COACardInterface stoped");
	}

	
	/**
	 * To invoke a method on the card
	 * 
	 * @param appletName	  the applet name
	 * @param instructionName the instruction name
	 * @param arg			  the argument
	 * 
	 * @return the byte array
	 */
	public byte[] invoke (String appletName, String instructionName, byte [] arg)
	{
		if ((appletMap == null) || (serv == null))
			return null;
		
		AppletCOA applet = (AppletCOA) appletMap.get (appletName);
		
		if (applet == null)
			return null;
		
		byte instructionByte = applet.getINS(instructionName);
		
		if (instructionByte == (byte) 0xFF)
			return null;
		
		byte [] aidBuffer;
		byte lengthArg = 0;
		
		if (arg != null)
		{
			lengthArg = (byte)(arg.length+1);
			aidBuffer = new byte [5+lengthArg];
			System.arraycopy(arg, 0, aidBuffer, 5, lengthArg-1);
			aidBuffer[5+lengthArg-1] = 0;
		}
		else
		{
			aidBuffer = new byte [5];
		}

		try
		{
			System.out.println ("Selecting "+applet.getName());
			serv.select(applet.getAid());
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		
		aidBuffer [0] = applet.getAid().getBytes () [0];
		aidBuffer [1] = instructionByte;
		aidBuffer [2] = (byte) 0x00;
		aidBuffer [3] = (byte) 0x00;
		aidBuffer [4] = (byte) lengthArg;

		ResponseAPDU response = null;
		
		try
		{
			System.out.println ("Executing "+instructionName);
			response = serv.sendAPDU (aidBuffer);
		}
		catch (CardTerminalException e)
		{
			e.printStackTrace();
		}

		if (response != null)
		{
			byte [] buffer = response.getBuffer();
			
			if(buffer.length>=2)
			{
				if((buffer[buffer.length-1]!=(byte)0x00)||(buffer[buffer.length-2]!=(byte)0x90))
				{
					return null;
				}
				else
				{ 	
					byte [] tmpBuff = new byte[buffer.length-2];
						
					System.arraycopy(buffer, 0, tmpBuff, 0, buffer.length-2);
					
					return tmpBuff;
				}
			}
		}
		return null;
	}
		
	
	
	/**
	 * To add a card insertion listener
	 * 
	 * @param l the card insertion listener
	 */
	public void addCardInsertionListener (CTListener l)
	{
		if (l != null)
			listListener.add (l);
	}

	/**
	 * To remove a card insertion listener
	 * 
	 * @param l the card insertion listener
	 */
	public void removeCardInsertionListener (CTListener l)
	{
		if (l != null)
			listListener.remove (l);
	}

	/**
	 * To get the applet map
	 * 
	 * @return the applet map
	 */
	public Map getAppletMap ()
	{
		return appletMap;
	}
	
	//----------------------------------------------------------//
	//------------------ PRIVATE METHODS -----------------------//
	//----------------------------------------------------------//
	
	/**
	 * To initialize the Open Card Framework
	 */
	private void initOCF ()
	{
		try
		{
			SmartCard.start ();

			printAvailableTerminals ();
		}
		catch (Exception ex)
		{
			System.out.println ("Exception caught in initOCF : " + ex.getMessage ());
			System.exit (-1);
		}
	}


	/**
	 * To do the authentification
	 * 
	 */
	private void authentication ()
	{
		System.out.println ("Authentication ...");

		keyFile = appKeyFile = keyFileDir + File.separator + CARD_KEY_FILE;

		try
		{
			serv = (CardServiceOPCore) card.getCardService (CardServiceVOP211.class , true);
		}
		catch (Exception e2)
		{
			e2.printStackTrace ();
		}

		serv.setFullCrypto (true);

		try
		{
			libService = new GemXpressoService ();
			libService.setCardService (serv);
		}
		catch (GemXpressoException e1)
		{
			e1.printStackTrace ();
		}

		try
		{
			serv.warmReset ();
		}
		catch (Exception e)
		{
			e.printStackTrace ();
		}

		VOPAuthenticationInput authenticationInput;
		authenticationInput = new VOPAuthenticationInput ();

		authenticationInput.setKeySetVersion (keySetVersion);
		authenticationInput.setDefaultKeySetVersion (false);

		authenticationInput.setKeyIndex 	(keyIndex);
		authenticationInput.setEnciphered 	(isEnciphered);
		authenticationInput.setMacing 		(isMacing);
		authenticationInput.setKeyfile 		(keyFile);
		authenticationInput.setSecurityDomainSelection (true);
		authenticationInput.setSecurityDomainAID (null);

		try
		{
			Result result = serv.openSecureChanel (authenticationInput);
			if ((result != null) && !result.isOK ())
			{
				throw new Exception ("authentication error : " + result.getResultMessage ());
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace ();
		}

		System.out.println ("Succeed.");
	}

	/**
	 * To print the avalable terminals
	 * 
	 */
	private void printAvailableTerminals ()
	{
		Enumeration terminals = CardTerminalRegistry.getRegistry ().getCardTerminals ();
		CardTerminal inFocusTerminal;

		while (terminals.hasMoreElements ())
		{
			inFocusTerminal = (CardTerminal) terminals.nextElement ();
			System.out.println ("Found OCF Card Terminal:");
			System.out.println ("\t- Name = " + inFocusTerminal.getName ());
			System.out.println ("\t- Type = " + inFocusTerminal.getType ());
			System.out.println ("\t- Adress = " + inFocusTerminal.getAddress () + "\n");
		}
	}

	/**
	 * To stop the Open Card Framework
	 */
	private void stopOCF ()
	{
		try
		{
			SmartCard.shutdown ();
		}
		catch (Exception ex)
		{
			System.out.println ("Exception caught in stopOCF : " + ex.getMessage ());
			System.exit (-1);
		}
	}

	/**
	 * To initialize the applet list
	 * 
	 */
	private void initListApplet ()
	{
		authentication ();

		Enumeration en = null;
		if (serv == null)
		{
			System.out.println ("Error service unavailable !!!§!§!§");
			return;
		}

		try
		{
			System.out.println ("Listing Applets ...");
			en = serv.scanCard ();
		}
		catch (Exception e)
		{
			e.printStackTrace ();
		}

		if (en != null)
			while (en.hasMoreElements ())
			{
				CardObjectStatus cOS = (CardObjectStatus) en.nextElement ();
				if (cOS.getCardObjectType () == CardObjectStatus.OBJECT_STATUS_ON_APPLICATION)
				{
					String name = getAppletName (cOS.getAID ());
					if (name != null)
					{
						System.out.println ("\"Introspection\" on " + name);
						AppletCOA ap = new AppletCOA (cOS.getAID (), name);
						buildAppletINSMap (ap);
						appletMap.put (name , ap);
					}
				}
			}
		else
			System.out.println ("No Applet on card");
	}

	/**
	 * To build the INS applet map 
	 * 
	 * @param applet the applet COA
	 */
	private void buildAppletINSMap (AppletCOA applet)
	{
		byte [] desc = getAppletDescription (applet.getAid ());
		byte [] type = getAppletReturnType(applet.getAid());
		int i = 2;

		if ((desc.length < 2) || (desc [0] != 0x12) || (desc [1] != 0x03))
			return;

		while (desc [i] != (byte) 0x90 && desc [i + 1] != (byte) 0x0)
		{
			byte instructionByte = desc[i];
			
			String instructionName = getInstructionName(applet.getAid(), instructionByte);
			
			String tmpType = "";
			
			switch(type[i])
			{
				case BYTE_APPLET:
					tmpType = "byte";
					break;
				case SHORT_APPLET:
					tmpType = "short";
					break;
				case INT_APPLET:
					tmpType = "int";
					break;
				case LONG_APPLET:
					tmpType = "long";
					break;
				case STRING_APPLET:
					tmpType = "String";
					break;
				case ARRAY_APPLET:
					tmpType = "byte []";
					break;
				case VOID_APPLET:
					tmpType = "void";
					break;
			}
			
			System.out.println ("\t Found : "+tmpType+" "+instructionName + " - "+instructionByte);

			applet.addINS (instructionByte , new AppletInstruction(instructionName, type[i]));
			
			i++;
		}
	}

	/**
	 * To get an applet description
	 * 
	 * @param aid the applet ID
	 * @return the applet description byte array
	 */
	private byte [] getAppletDescription (AppletID aid)
	{
		try
		{
			byte [] aidBuffer = new byte [5];

			serv.select (aid);

			aidBuffer [0] = aid.getBytes () [0];
			aidBuffer [1] = CARD_GET_INS;
			aidBuffer [2] = (byte) 0x00;
			aidBuffer [3] = (byte) 0x00;
			aidBuffer [4] = (byte) 0x00;

			ResponseAPDU response = serv.sendAPDU (aidBuffer);

			return response.getBuffer ();

		}
		catch (Exception e)
		{
			e.printStackTrace ();
		}

		return null;
	}

	/**
	 * To get an applet instructions return type
	 * 
	 * @param aid the applet ID
	 * @return the applet description byte array
	 */
	private byte [] getAppletReturnType (AppletID aid)
	{
		try
		{
			byte [] aidBuffer = new byte [5];

			serv.select (aid);

			aidBuffer [0] = aid.getBytes () [0];
			aidBuffer [1] = CARD_GET_INS_TYPE;
			aidBuffer [2] = (byte) 0x00;
			aidBuffer [3] = (byte) 0x00;
			aidBuffer [4] = (byte) 0x00;

			ResponseAPDU response = serv.sendAPDU (aidBuffer);

			return response.getBuffer ();

		}
		catch (Exception e)
		{
			e.printStackTrace ();
		}

		return null;
	}
	
	/**
	 * To get the applet name
	 * 
	 * @param aid the applet ID
	 * @return the applet name
	 */
	private String getAppletName (AppletID aid)
	{
		try
		{
			byte [] aidBuffer = new byte [5];
			byte [] tmpBuff = new byte [256];
			int i = 2, j = 0;
			String appletName = null;

			serv.select (aid);

			aidBuffer [0] = aid.getBytes () [0];
			aidBuffer [1] = CARD_GET_NAME;
			aidBuffer [2] = (byte) 0x00;
			aidBuffer [3] = (byte) 0x00;
			aidBuffer [4] = (byte) 0x00;

			ResponseAPDU response = serv.sendAPDU (aidBuffer);

			byte [] desc = response.getBuffer ();

			if ((desc.length < 2) || (desc [0] != 0x12) || (desc [1] != 0x03))
				return null;

			for (; desc [i] != (byte) 0x90 && desc [i + 1] != (byte) 0x00; i++, j++)
				tmpBuff [j] = desc [i];

			return bytesToString (tmpBuff , (int) j);
		}
		catch (Exception e)
		{
			e.printStackTrace ();
		}

		return null;
	}

	/**
	 * To get an instruction name
	 * 
	 * @param aid the applet ID
	 * @param instructionByte the instruction number
	 * @return the instruction name
	 */
	private String getInstructionName (AppletID aid, byte instructionByte)
	{
		try
		{
			byte [] aidBuffer = new byte [5];
			byte [] tmpBuff = new byte [256];
			int i = 2, j = 0;
			String appletName = null;

			aidBuffer [0] = aid.getBytes () [0];
			aidBuffer [1] = CARD_GET_INS_NAME;
			aidBuffer [2] = instructionByte;
			aidBuffer [3] = (byte) 0x00;
			aidBuffer [4] = (byte) 0x00;

			ResponseAPDU response = serv.sendAPDU (aidBuffer);

			byte [] desc = response.getBuffer ();

			if ((desc.length < 2) || (desc [0] != 0x12) || (desc [1] != 0x03))
				return null;

			for (; desc [i] != (byte) 0x90 && desc [i + 1] != (byte) 0x00; i++, j++)
				tmpBuff [j] = desc [i];

			return bytesToString (tmpBuff , (int) j);
		}
		catch (Exception e)
		{
			e.printStackTrace ();
		}

		return null;
	}
	

	/**
	 * When an insertion card event occured
	 * 
	 * @param event the card terminal event
	 */
	private void fireCardInserted (CardTerminalEvent event)
	{
		try
		{
			for (Iterator i = listListener.iterator (); i.hasNext ();)
				((CTListener) i.next ()).cardInserted (event);
		}
		catch (CardTerminalException e)
		{
			e.printStackTrace ();
		}
	}

	/**
	 * When a remove card event occured
	 * 
	 * @param event the card terminal event
	 */
	private void fireCardRemoved (CardTerminalEvent event)
	{
		try
		{
			for (Iterator i = listListener.iterator (); i.hasNext ();)
				((CTListener) i.next ()).cardRemoved (event);
		}
		catch (CardTerminalException e)
		{
			e.printStackTrace ();
		}
	}


	/**
	 * To transform the bytes into a string
	 * 
	 * @param b	  the byte array to transform
	 * @param len the length
	 * @return the generated string
	 */
	private String bytesToString (byte [] b, int len)
	{
		byte [] c = new byte [len];
		for (int i = 0; i < len; i++)
		{
			byte n = b [i];
			if ((n < 32) || (n > 128))
				n = 65;
			c [i] = n;
		}
		
		return new String (c);
	}
	
	
	/**
	 * Internal class representating the card listener
	 * 
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
			if (card == null)
			{
				card 	 = SmartCard.getSmartCard (event);
				terminal = event.getCardTerminal ();
				slotId   = event.getSlotID ();

				System.out.println ("Card Inserted !");

				initListApplet ();

				fireCardInserted (event);
			}
		}

		/**
		 * @see opencard.core.event.CTListener#cardRemoved(opencard.core.event.CardTerminalEvent)
		 */
		public void cardRemoved (CardTerminalEvent event) throws CardTerminalException
		{
			if ((event.getSlotID () == slotId) && (event.getCardTerminal () == terminal))
			{
				System.out.println ("Card Removed !");

				card 	 = null;
				terminal = null;
				slotId 	 = 0;
				
				appletMap.clear();
				
				fireCardRemoved (event);				
			}
		}		
	}

}