package fr.umlv.coa.javacard;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import opencard.core.event.CTListener;
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


class COACardInterface
{

	private CardServiceOPCore		serv;
	private GemXpressoService		libService;

	private CardTerminal			terminal			= null;
	private SmartCard				card				= null;
	private int						slotId				= 0;

	private TreeMap					appletMap			= new TreeMap ();

	private String					homeDir				= "c:" + File.separator + "gemplus" + File.separator + "gemxpresso.rad3";

	private String					keyFileDir;

	private String					profileDir;

	private String					cardTarget			= "gempc410_com1";

	private String					cardKeyFile			= "GXP211_PK.properties";

	private boolean					loadPackage			= true;

	private String					frameworkType		= "GXP211_PK";

	private byte []					securityDomainAID	= null;
	private String					keyFile;
	private int						keySetVersion		= 13;
	private int						keyIndex			= 0;
	private boolean					isEnciphered		= false;
	private boolean					isMacing			= false;

	private String					appKeyFile;
	private int						appKeySetVersion	= 13;
	private int						appKeyIndex			= 0;

	private final List				listListener		= new ArrayList ();

	private static COACardInterface	INSTANCE			= null;

	private final static byte		CARD_GET_NAME		= (byte) 0xFF;
	private final static byte		CARD_GET_INS		= (byte) 0xFE;
	private final static byte		CARD_GET_INS_NAME	= (byte) 0xFD;

	public COACardInterface ()
	{
	}

	public static COACardInterface getInstance ()
	{
		if (INSTANCE == null)
			INSTANCE = new COACardInterface ();

		return INSTANCE;
	}

	public boolean addErrorListener (CTListener l)
	{
		return listListener.add (l);
	}

	public boolean removeErrorListener (CTListener l)
	{
		return listListener.remove (l);
	}

	public void initOCF ()
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

	private void authentication ()
	{
		System.out.println ("Authentication ...");

		keyFile = appKeyFile = keyFileDir + File.separator + cardKeyFile;

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

		authenticationInput.setKeyIndex (keyIndex);
		authenticationInput.setEnciphered (isEnciphered);
		authenticationInput.setMacing (isMacing);
		authenticationInput.setKeyfile (keyFile);
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

	public void stopOCF ()
	{
		try
		{
			SmartCard.shutdown ();
		}
		catch (Exception ex)
		{
			System.out.println ("Exception caught in stopOCF : " + ex.getMessage ());
			java.lang.System.exit (-1);
		}
	}

	public void initListApplet ()
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
						makeAppletINSMap (ap);
						appletMap.put (cOS.getAID ().toString () , ap);
					}
				}
			}
		else
			System.out.println ("No Applet on card");
	}

	private void makeAppletINSMap (AppletCOA applet)
	{
		byte [] desc = getAppletDescription (applet.getAid ());
		int i = 2;

		if ((desc.length < 2) || (desc [0] != 0x12) || (desc [1] != 0x03))
			return;

		while (desc [i] != (byte) 0x90 && desc [i + 1] != (byte) 0x0)
		{
			byte instructionByte = desc[i];
			
			String instructionName = getInstructionName(applet.getAid(), instructionByte);
			
			System.out.println ("\t Found "+instructionName + " - "+instructionByte);

			applet.addINS (instructionByte , instructionName);
			
			i++;
		}
	}

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
	
	public void cardInserted (SmartCard card, CardTerminal terminal, int slotId)
	{
		this.card = card;
		this.terminal = terminal;
		this.slotId = slotId;

		System.out.println ("Card Inserted !");

		initListApplet ();

		fireCardInserted ();
	}

	public void cardRemoved ()
	{
		System.out.println ("Card Removed !");

		fireCardRemoved ();
	}

	private void fireCardInserted ()
	{
		try
		{
			for (Iterator i = listListener.iterator (); i.hasNext ();)
				((CTListener) i.next ()).cardInserted (null);
		}
		catch (CardTerminalException e)
		{
			e.printStackTrace ();
		}
	}

	private void fireCardRemoved ()
	{
		try
		{
			for (Iterator i = listListener.iterator (); i.hasNext ();)
				((CTListener) i.next ()).cardRemoved (null);
		}
		catch (CardTerminalException e)
		{
			e.printStackTrace ();
		}
	}

	public void init ()
	{
		GxpSystem.getInstance ().setRadHome (homeDir);

		keyFileDir = System.getProperty ("gemplus.gemxpresso.rad.home.resources.targets");
		profileDir = System.getProperty ("gemplus.gemxpresso.rad.home.resources.cardprofile");
		System.setProperty ("user.dir" , System.getProperty ("gemplus.gemxpresso.rad.home.conf"));

		System.out.println ("COACardInterface started");

		initOCF ();

		EventGenerator.getGenerator ().addCTListener (new COAJavaCardListener ());

		boolean stop = true;

		while (stop)
		{

		}

		stopOCF ();

		System.out.println ("COACardInterface stoped");
	}

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
}