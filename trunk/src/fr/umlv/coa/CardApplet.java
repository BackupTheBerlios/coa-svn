/* 
 * File    : CardApplet.java
 * Created : 16 févr. 2005
 * 
 * =======================================
 * COA PROJECT ("http://coa.berlios.de")
 * =======================================
 *
 */

package fr.umlv.coa;

/**
 * Interface for a class that could be installed on a Card
 * 
 * @author Ludo
 *
 */
public interface CardApplet
{
	/**
	 * To know if if it an applet that must be on a card
	 * 
	 * @return true if it an applet that must be on a card, false otherwise
	 */
	public boolean isOnCard ();
}
