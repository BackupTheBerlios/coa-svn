/* 
 * File    : CardTerminalListener.java
 * Created : 24 févr. 2005
 * 
 * =======================================
 * COA PROJECT ("http://coa.berlios.de")
 * =======================================
 *
 */
package fr.umlv.coa.terminal;


/**
 * Card terminal listener used to get events when a card is inserted or removed
 * 
 * @author Ludo
 *
 */
public interface CardTerminalListener
{
	/**
	 * When a card is inserted
	 * 
	 * @param szTerminalName the terminal name
	 * @param iSlot			 the terminal slot into which the card was inserted
	 */
	public void cardInserted (String szTerminalName, int iSlot);

	
	/**
	 * When a card is removed
	 * 
	 * @param szTerminalName the terminal name
	 * @param iSlot			 the terminal slot into which the card was removed
	 */
	public void cardRemoved (String szTerminalName, int iSlot);
}
