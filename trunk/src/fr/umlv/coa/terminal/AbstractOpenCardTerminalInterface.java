/* 
 * File    : OpenCardTerminalInterface.java
 * Created : 24 févr. 2005
 * 
 * =======================================
 * COA PROJECT ("http://coa.berlios.de")
 * =======================================
 *
 */

package fr.umlv.coa.terminal;

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
import com.gemplus.tools.gemxpresso.GemXpressoService;

import fr.umlv.coa.javacard.AppletInstruction;
import fr.umlv.coa.javacard.CardApplet;


/**
 * OpenCard Interface between the terminal and the card
 * 
 * @author Ludo
 *
 */
public abstract class AbstractOpenCardTerminalInterface implements CardTerminalInterface
{
	/** The card get name byte value */
	private final static byte CARD_GET_NAME		= (byte) 0xFF;
	/** The card get instruction */
	private final static byte CARD_GET_INS		= (byte) 0xFE;
	/** The card get instruction name */
	private final static byte CARD_GET_INS_NAME	= (byte) 0xFD;
	/** The card get instruction return type */
	private final static byte CARD_GET_INS_TYPE	= (byte) 0xFC;

	/** The applet map */
	private final Map appletMap = new TreeMap ();
	/** The listeners list */
	private final List terminalListeners = new ArrayList ();

	/** The card service core */
	protected CardServiceOPCore		serv;
	/** The gemxpresso service */
	protected GemXpressoService		libService;

	/** The card terminal */
	protected CardTerminal			terminal = null;
	/** The smartcard */
	protected SmartCard				card	 = null;
	/** The slot id */
	protected int					slotId	 = 0;

	//----------------------------------------------------------//
	//--------------------- CONSTRUCTORS -----------------------//
	//----------------------------------------------------------//

	/**
	 * Constructor
	 * 
	 */
	protected AbstractOpenCardTerminalInterface ()
	{
		initialize ();
	}
	
	
	//----------------------------------------------------------//
	//------------------- PUBLIC METHODS -----------------------//
	//----------------------------------------------------------//

	/**
	 * @see fr.umlv.coa.terminal.CardTerminalInterface#initialize ()
	 */
	public void initialize ()
	{
		initializeProperties ();
		
		initOCF ();
		EventGenerator.getGenerator ().addCTListener (new CardListener ());
	}

	
	/**
	 * @see fr.umlv.coa.terminal.CardTerminalInterface#stop ()
	 */
	public void stop()
	{
		stopOCF ();
	}

	
	/**
	 * @see fr.umlv.coa.terminal.CardTerminalInterface#invoke(String, String, byte[])
	 */
	public byte[] invoke (String appletName, String instructionName, byte [] arg)
	{
		if ((appletMap == null) || (serv == null))
			return null;
		
		CardApplet applet = (CardApplet) appletMap.get (appletName);
		
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
	 * @see fr.umlv.coa.terminal.CardTerminalInterface#addCardTerminalListener(CardTerminalListener)
	 */
	public void addCardTerminalListener (CardTerminalListener listener)
	{
		if (listener != null)
			terminalListeners.add (listener);
	}

	/**
	 * @see fr.umlv.coa.terminal.CardTerminalInterface#removeCardTerminalListener(CardTerminalListener)
	 */
	public void removeCardTerminalListener (CardTerminalListener listener)
	{
		if (listener != null)
			terminalListeners.remove (listener);
	}


	/**
	 * @see fr.umlv.coa.terminal.CardTerminalInterface#getAppletsMap()
	 */
	public Map getAppletsMap ()
	{
		return appletMap;
	}
	
	//----------------------------------------------------------//
	//---------------- PROTECTED METHODS -----------------------//
	//----------------------------------------------------------//
	
	/**
	 * To initialize the open card properties for the vendor
	 * 
	 */
	protected abstract void initializeProperties ();
	
	/**
	 * To authenticate on the vendor terminal 
	 */
	protected abstract void authentication ();


	/**
	 * To print the avalable terminals
	 * 
	 */
	protected void printAvailableTerminals ()
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
						CardApplet ap = new CardApplet (cOS.getAID (), name);
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
	private void buildAppletINSMap (CardApplet applet)
	{
		byte [] desc = getAppletDescription (applet.getAid ());
		byte [] type = getAppletReturnType (applet.getAid());
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
				case AppletInstruction.BYTE_RETURN:
					tmpType = "byte";
					break;
				case AppletInstruction.SHORT_RETURN:
					tmpType = "short";
					break;
				case AppletInstruction.INT_RETURN:
					tmpType = "int";
					break;
				case AppletInstruction.LONG_RETURN:
					tmpType = "long";
					break;
				case AppletInstruction.STRING_RETURN:
					tmpType = "String";
					break;
				case AppletInstruction.ARRAY_RETURN:
					tmpType = "byte []";
					break;
				case AppletInstruction.VOID_RETURN:
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
		for (Iterator i = terminalListeners.iterator (); i.hasNext ();)
			((CardTerminalListener) i.next ()).cardInserted (event.getCardTerminal ().getName (), event.getSlotID ());
	}

	/**
	 * When a remove card event occured
	 * 
	 * @param event the card terminal event
	 */
	private void fireCardRemoved (CardTerminalEvent event)
	{
		for (Iterator i = terminalListeners.iterator (); i.hasNext ();)
			((CardTerminalListener) i.next ()).cardRemoved (event.getCardTerminal ().getName (), event.getSlotID ());
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

				System.out.println ("---- Card Inserted ! ----");

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
				System.out.println ("---- Card Removed ! ----");

				card 	 = null;
				terminal = null;
				slotId 	 = 0;
				
				appletMap.clear();
				
				fireCardRemoved (event);				
			}
		}		
	}

}