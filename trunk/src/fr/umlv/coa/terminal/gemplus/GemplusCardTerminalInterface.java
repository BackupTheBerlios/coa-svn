/* 
 * File    : GemplusCardTerminalInterface.java
 * Created : 24 févr. 2005
 * 
 * =======================================
 * COA PROJECT ("http://coa.berlios.de")
 * =======================================
 *
 */

package fr.umlv.coa.terminal.gemplus;

import java.io.File;

import com.gemplus.opencard.service.op.CardServiceOPCore;
import com.gemplus.opencard.service.op.Result;
import com.gemplus.opencard.service.op.vop.VOPAuthenticationInput;
import com.gemplus.opencard.service.op.vop.vop211.CardServiceVOP211;
import com.gemplus.tools.gemxpresso.GemXpressoException;
import com.gemplus.tools.gemxpresso.GemXpressoService;
import com.gemplus.tools.gemxpresso.util.GxpSystem;

import fr.umlv.coa.terminal.AbstractOpenCardTerminalInterface;


/**
 * Interface between the COA and the card
 * 
 * @author Ludo
 *
 */
public final class GemplusCardTerminalInterface extends AbstractOpenCardTerminalInterface
{
	/** The Singleton instance */
	private static GemplusCardTerminalInterface	INSTANCE = null;

	/** The key file directory */
	private String					keyFileDir;
	/** The profile directory */
	private String					profileDir;
	/** The key file */
	private String					keyFile;

	/** The application key file */
	private String					appKeyFile;
	/** The application key set version */
	private int						appKeySetVersion	= 13;
	/** The application key index */
	private int						appKeyIndex			= 0;

	
	/** The security domain AID */
	private byte []					securityDomainAID	= null;
	/** The key set version */
	private int						keySetVersion		= 13;
	/** The key index */
	private int						keyIndex			= 0;

	
	/** To know if there is some enciphering */
	private boolean					isEnciphered = false;
	/** To know if there is macing */
	private boolean					isMacing = false;

	
	//----------------------------------------------------------//
	//------------------- PUBLIC METHODS -----------------------//
	//----------------------------------------------------------//

	/**
	 * To get the singleton instance
	 *  
	 * @return the singleton instance
	 */
	public static GemplusCardTerminalInterface getInstance ()
	{
		if (INSTANCE == null)
			INSTANCE = new GemplusCardTerminalInterface ();

		return INSTANCE;
	}

	
	protected void initializeProperties ()
	{
		GxpSystem.getInstance ().setRadHome (GemplusTerminalConfiguration.HOME_DIR);

		keyFileDir = System.getProperty ("gemplus.gemxpresso.rad.home.resources.targets");
		profileDir = System.getProperty ("gemplus.gemxpresso.rad.home.resources.cardprofile");
		System.setProperty ("user.dir" , System.getProperty ("gemplus.gemxpresso.rad.home.conf"));
	}
	

	protected void authentication ()
	{
		System.out.println ("Authentication ...");

		keyFile = appKeyFile = keyFileDir + File.separator + GemplusTerminalConfiguration.CARD_KEY_FILE;

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
}