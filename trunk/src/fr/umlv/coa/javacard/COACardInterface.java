package fr.umlv.coa.javacard;

import java.io.File;
import java.util.Enumeration;
import java.util.TreeMap;

import opencard.core.service.CardRequest;
import opencard.core.service.SmartCard;
import opencard.core.terminal.CardTerminal;
import opencard.core.terminal.CardTerminalException;
import opencard.core.terminal.CardTerminalRegistry;
import opencard.core.terminal.CommandAPDU;
import opencard.core.terminal.ResponseAPDU;
import opencard.opt.applet.AppletID;

import com.gemplus.opencard.service.op.CardObjectStatus;
import com.gemplus.opencard.service.op.CardServiceOPCore;
import com.gemplus.opencard.service.op.CardServiceOPException;
import com.gemplus.opencard.service.op.Result;
import com.gemplus.opencard.service.op.vop.VOPAuthenticationInput;
import com.gemplus.opencard.service.op.vop.vop200.CardServiceVOP200;
import com.gemplus.opencard.service.op.vop.vop211.CardServiceVOP211;
import com.gemplus.tools.gemxpresso.GemXpressoService;
import com.gemplus.tools.gemxpresso.util.GxpSystem;


class COACardInterface
{
	/**
	 * global declarations
	 */
	// OP/VOP OCF CardService and its high level API library
	private CardServiceOPCore	serv;
	private boolean				isVOP211Compliant	= true;
	private GemXpressoService	libService;
	// OCF CardTerminal for Card or GSE communication
	private CardTerminal		terminal;
	// OCF objects for APDU transport
	private CommandAPDU			cmd;
	private ResponseAPDU		resp;
	
	private TreeMap				appletMap			= new TreeMap ();
	
	// Windows platform (c:\gemplus\gemxpresso.rad3)
	private String				homeDir				= "c:" + File.separator + "gemplus" + File.separator + "gemxpresso.rad3";
	
	//targets path (<gemxpressorad>\resources\targets)
	private String				keyFileDir;
	
	//card profile dir (<gemxpressorad>\resouces\cardprofile)
	private String				profileDir;
	
	/**
	 *  Card target names
	 */
	// the name defined in the CardTerminal section of the "opencard.properties" file
	private String				cardTarget			= "gempc410_com1";
	
	/**
	 *  Key file
	 */
	// the key file (GXP211_PK is default)
	private String				cardKeyFile			= "GXP211_PK.properties";
	
	// the target flag, if true the GSE target is used, card otherwise
	private static boolean		simulation			= true;
	
	// the load flag, if true package is loaded in card or GSE
	private boolean				loadPackage			= true;
	
	// the type of GSE or Card (GXP211_PK is default)
	private String				FrameworkType		= "GXP211_PK";
	
	/**
	 * System authentication informations
	 */
	// the Security Domain AID used for security
	private byte []				securityDomainAID	= null;
	// the system key File for management
	private String				keyFile;
	// the system key set version
	private int					keySetVersion		= 13;
	// the system key set index (0 is default)
	private int					keyIndex			= 0;
	// the security level definition flags
	private boolean				isEnciphered		= false;
	private boolean				isMacing			= false;
	
	/**
	 * Application authentication informations
	 */
	// the application key Files
	private String				appKeyFile;
	// the application key set version
	private int					appKeySetVersion	= 13;
	// the application key set index
	private int					appKeyIndex			= 0;
	
	public COACardInterface ()
	{
	}
	
	/**
	 * OCF layer initialisation
	 */
	public void initOCF ()
	{
		// start of initialisation
		try
		{
			// start the OCF layer
			SmartCard.start ();
			
			// fixe the target
			String target = cardTarget;
			
			// print found terminals
			printAvailableTerminals ();
			
			// select the target specific CardTerminal
			terminal = CardTerminalRegistry.getRegistry ().cardTerminalForName (target);
			
			// check if required terminal name was found
			if (terminal == null)
			{
				throw new Exception ("terminal not found : " + target);
			}
			
			// retreive the CardTerminal type
			String type = terminal.getType ();
			if ((type.compareTo ("SOCKETJC21SIMULATOR") == 0) && !simulation)
			{
				throw new Exception ("terminal " + target + " is a simulator instance");
			}
			
			if (!(type.compareTo ("SOCKETJC21SIMULATOR") == 0) && simulation)
			{
				throw new Exception ("terminal " + target + " is not a simulator instance");
			}
			
			// create a new card request object
			CardRequest cr = new CardRequest (CardRequest.ANYCARD, terminal, null);
			
			// build the complete key file
			keyFile = appKeyFile = keyFileDir + File.separator + cardKeyFile;
			
			// wait for card insertion
			SmartCard sc = SmartCard.waitForCard (cr);
			// get the OP/VOP specific CardService
			if (isVOP211Compliant)
				serv = (CardServiceOPCore) sc.getCardService (CardServiceVOP211.class , true);
			else
				serv = (CardServiceOPCore) sc.getCardService (CardServiceVOP200.class , true);
			
			//sets FullCrypto if FullCrypto card or Limited Crypto if IS card
			serv.setFullCrypto (FrameworkType.indexOf ("IS") == -1);
			
			// create the  high level API library object
			libService = new GemXpressoService ();
			// set the OP/VOP CardService to the service library for communication
			libService.setCardService (serv);
			
			// reset the Card or GSE to look for the default ATR
			serv.warmReset ();
			
			// the service authentication object use for authentication configuration
			VOPAuthenticationInput authenticationInput;
			// service authentication object creation
			authenticationInput = new VOPAuthenticationInput ();
			// look if key set version is defined
			if (keySetVersion > 0)
			{
				// key set version configuration
				authenticationInput.setKeySetVersion (keySetVersion);
				// do not use the key set version defined in the target file
				authenticationInput.setDefaultKeySetVersion (false);
			}
			else
			{
				// no key set version defined
				// use the key set version defined in the target file
				authenticationInput.setDefaultKeySetVersion (true);
			}
			// key set version index configuration
			authenticationInput.setKeyIndex (keyIndex);
			// security configuration
			authenticationInput.setEnciphered (isEnciphered);
			authenticationInput.setMacing (isMacing);
			// define the target specific key file to use
			authenticationInput.setKeyfile (keyFile);
			// define if the security domain has to be select (yes)
			authenticationInput.setSecurityDomainSelection (true);
			// define the Security Domain AID
			// null force the use of the AID present in the target key file
			authenticationInput.setSecurityDomainAID (null);
			
			try
			{
				// process mutual authentication
				// initialize/update and external/authenticate are done
				Result result = serv.openSecureChanel (authenticationInput);
				// check the result object for authentication status
				if ((result != null) && !result.isOK ())
				{
					throw new Exception ("authentication error : " + result.getResultMessage ());
				}
			}
			catch (Exception ex)
			{
				// authentication fails
				throw new Exception ("authentication error : " + ex.getMessage ());
			}
			// authentication succeed
			System.out.println ("Authentication OK");
			System.out.println ("");
			
		}
		catch (Exception ex)
		{
			// exception generated during OCF initialization
			System.out.println ("Exception caught in initOCF : " + ex.getMessage ());
			// terminate client application
			System.exit (-1);
		}
	}
	
	/**
	 * PurseClient default constructor
	 */
	public void setHomeDir (String radHome)
	{
		homeDir = radHome;
	}
	
	/**
	 * Utility that print the available OCF CardTerminal for the client
	 */
	private void printAvailableTerminals ()
	{
		// get an enumeration from the registry
		java.util.Enumeration terminals = CardTerminalRegistry.getRegistry ().getCardTerminals ();
		// the CardTerminal we are retreiving information
		CardTerminal inFocusTerminal;
		
		// analyse loop
		while (terminals.hasMoreElements ())
		{
			inFocusTerminal = (CardTerminal) terminals.nextElement ();
			System.out.println ("Found OCF Card Terminal:");
			// print the user defined name
			System.out.println ("\t- Name = " + inFocusTerminal.getName ());
			// print the legal type
			System.out.println ("\t- Type = " + inFocusTerminal.getType ());
			// print the corresponding adress
			System.out.println ("\t- Adress = " + inFocusTerminal.getAddress () + "\n");
		}
	}
	
	/**
	 * Free OCF layer used by the client
	 */
	public void stopOCF ()
	{
		try
		{
			// free the OCF layer before terminating
			SmartCard.shutdown ();
		}
		catch (Exception ex)
		{
			System.out.println ("Exception caught in stopOCF : " + ex.getMessage ());
			// terminate client application with an exception
			java.lang.System.exit (-1);
		}
	}
	
	public void initListApplet ()
	{
		Enumeration en = null;
		
		try
		{
			en = serv.scanCard ();
		}
		catch (CardServiceOPException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace ();
		}
		catch (CardTerminalException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace ();
		}
		
		while (en.hasMoreElements ())
		{
			CardObjectStatus cOS = (CardObjectStatus) en.nextElement ();
			if (cOS.getCardObjectType () == CardObjectStatus.OBJECT_STATUS_ON_APPLICATION)
			{
				appletMap.put (cOS.getAIDAsString () , new AppletCOA (cOS.getAID ()));
				
				AppletCOA ap = new AppletCOA (cOS.getAID ());
				makeAppletINSMap (ap);
				appletMap.put (cOS.getAID ().toString () , ap);
			}
		}
	}
	
	private void makeAppletINSMap (AppletCOA applet)
	{
		byte [] desc = getAppletDescription (applet.getAid ());
		int i = 2;
		
		if ((desc.length < 2) || (desc [0] != 0x12) || (desc [1] != 0x03))
			return;
		
		while (desc [i] != (byte) 0x90 && desc [i + 1] != (byte) 0x0)
		{
			byte [] tmpBuff = new byte [256];
			String insName = null;
			byte ins = 0;
			int j = 0;
			
			for (j = 0; desc [i] != 00; i++, j++)
				tmpBuff [j] = desc [i];
			
			ins = desc [++i];
			
			i++;
			
			insName = bytesToString (tmpBuff , (int) j);
			
			applet.addINS (ins , insName);
		}
	}
	
	private byte [] getAppletDescription (AppletID aid)
	{
		try
		{
			byte [] aidBuffer = new byte [5];
			
			serv.select (aid);
			
			aidBuffer [0] = aid.getBytes () [0];
			aidBuffer [1] = (byte) 0xFF;
			aidBuffer [2] = (byte) 0x00;
			aidBuffer [3] = (byte) 0x00;
			aidBuffer [4] = (byte) 0x00;
			
			ResponseAPDU response = serv.sendAPDU (aidBuffer);
			
			return response.getBuffer ();
			
		}
		catch (CardServiceOPException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace ();
		}
		catch (CardTerminalException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace ();
		}
		
		return null;
	}
	
	/**
	 * Application main entry point.
	 * @param argv not used by default application.
	 */
	public static void main (String [] argv)
	{
		String radHome = null;
		String cardT = null;
		
		// parse arguments
		for (int i = 0; i < argv.length; i++)
		{
			// card request
			if (argv [i].toLowerCase ().equals ("-card"))
				simulation = false;
			// GSE request
			if (argv [i].toLowerCase ().equals ("-gse"))
				simulation = true;
			// rad home directory
			if (argv [i].toLowerCase ().equals ("-radhome"))
				radHome = argv [++i];
			if (argv [i].toLowerCase ().equals ("-cardtype"))
			{
				cardT = argv [++i];
			}
		}
		
		if (cardT == null)
		{
			System.exit (-1);
		}
		// create client instance
		COACardInterface client = new COACardInterface ();
		client.cardKeyFile = cardT + ".properties";
		client.FrameworkType = cardT;
		if (cardT.equals ("GXP211v1") || cardT.equals ("GXP211v1_IS"))
			client.isVOP211Compliant = false;
		else
			client.isVOP211Compliant = true;
		
		// change rad home directory used for properties files.
		if (radHome != null)
			client.setHomeDir (radHome);
		
		//allows to get all path properties for GemXpressoRad
		GxpSystem.getInstance ().setRadHome (client.homeDir);
		
		//sets the targets dir
		client.keyFileDir = System.getProperty ("gemplus.gemxpresso.rad.home.resources.targets");
		//sets the card profile dir
		client.profileDir = System.getProperty ("gemplus.gemxpresso.rad.home.resources.cardprofile");
		//!!!!REQUIRED !!!!
		//sets user.dir to <gemxpressorad>\conf directory for OCF initialization (initOCF)
		//OCF automatically search the opencard.properties file in the user.dir (current) directory.
		System.setProperty ("user.dir" , System.getProperty ("gemplus.gemxpresso.rad.home.conf"));
		
		System.out.println ("----- start of COACardInterface client application -----");
		
		// initialise OCF layer
		client.initOCF ();
		
		client.initListApplet ();
		
		// free OCF layer
		client.stopOCF ();
		
		System.out.println ("----- end of COACardInterface client application -----");
		// terminate client application normally
		java.lang.System.exit (0);
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
