/* 
 * File    : CardTerminalFactory.java
 * Created : 24 févr. 2005
 * 
 * =======================================
 * COA PROJECT ("http://coa.berlios.de")
 * =======================================
 *
 */
package fr.umlv.coa.terminal;

import fr.umlv.coa.terminal.gemplus.GemplusCardTerminalInterface;

/**
 * Card terminal factory used to create the CardTerminal depending on the 
 * different card terminal reader company
 * 
 * @author Ludo
 *
 */
public final class CardTerminalFactory
{
	
	/**
	 * Private constructor to avoid any instantiation
	 * 
	 */
	private CardTerminalFactory ()
	{
	}

	
	/**
	 * To get the gemplus terminal interface
	 * 
	 * @return the gemplus terminal interface
	 */
	public static CardTerminalInterface getGemplusTerminalInterface ()
	{
		return GemplusCardTerminalInterface.getInstance ();
	}
	
	
	/**
	 * To get the schlumberger terminal interface
	 * 
	 * @return the schlumberger terminal interface
	 */
	public static CardTerminalInterface getSchlumbergerTerminalInterface ()
	{
		//TODO Implement the Schlumberger terminal interface
		return null;
	}	
	
}
