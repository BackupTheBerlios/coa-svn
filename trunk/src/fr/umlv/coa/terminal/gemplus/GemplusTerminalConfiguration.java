/* 
 * File    : GemplusTerminalConfiguration.java
 * Created : 24 févr. 2005
 * 
 * =======================================
 * COA PROJECT ("http://coa.berlios.de")
 * =======================================
 *
 */
package fr.umlv.coa.terminal.gemplus;

import java.io.File;

/**
 * Contains the configuration for the gemplus terminal
 * 
 * @author Ludo
 *
 */
public final class GemplusTerminalConfiguration
{
	/** The opencard home directory */ 
	public static final String	HOME_DIR 	   = "d:" + File.separator + "gemplus" + File.separator + "gemxpresso.rad3";
	/** The card reader */
	public static final String	CARD_TARGET	   = "gempc410_com1";
	/** The card profile name */
	public static final String	CARD_KEY_FILE  = "GXP211_PK.properties";
	/** The framework type */
	public static final String	FRAMEWORK_TYPE = "GXP211_PK";
	
	
	/**
	 * Private constructor to avoir any instantiation
	 */
	private GemplusTerminalConfiguration ()
	{
	}

}
