/* 
 * File    : CardTerminalInterface.java
 * Created : 24 févr. 2005
 * 
 * =======================================
 * COA PROJECT ("http://coa.berlios.de")
 * =======================================
 *
 */
package fr.umlv.coa.terminal;

import java.util.Map;

/**
 * @author Ludo
 *
 */
public interface CardTerminalInterface
{
	/**
	 * To initialize the card terminal
	 * 
	 */
	public void initialize ();
	
	
	/**
	 * To stop the card terminal
	 * 
	 */
	public void stop ();

	
	/**
	 * To invoke a method on the card inserted into the card terminal
	 * 
	 * @param appletName	  the applet name into the card
	 * @param instructionName the instruction name of the applet
	 * @param arg			  the argument
	 * 
	 * @return the byte array
	 */
	public byte[] invoke (String appletName, String instructionName, byte [] arg);

	
	/**
	 * To get the applets map
	 * 
	 * @return the applets map
	 */
	public Map getAppletsMap ();
	
	
	/**
	 * To add a card terminal listener
	 * 
	 * @param listener the card terminal listener to add
	 */
	public void addCardTerminalListener (CardTerminalListener listener);
	
	
	/**
	 * To remove a card terminal listener
	 * 
	 * @param listener the card terminal listener to remove
	 */
	public void removeCardTerminalListener (CardTerminalListener listener);
	
}
