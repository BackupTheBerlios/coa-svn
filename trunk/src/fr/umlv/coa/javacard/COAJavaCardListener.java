/*
 * File : COAJavaCardListener.java Created : 20 févr. 2005
 * ======================================= BOSS PROJECT
 * ("http://boss.berlios.de") =======================================
 */

package fr.umlv.coa.javacard;

import opencard.core.event.CTListener;
import opencard.core.event.CardTerminalEvent;
import opencard.core.service.SmartCard;
import opencard.core.terminal.CardTerminal;
import opencard.core.terminal.CardTerminalException;


/**
 */
public class COAJavaCardListener implements CTListener
{
	private SmartCard		smartcard	= null;
	private CardTerminal	terminal	= null;
	private int				slotID		= 0;

	/**
	 * @see opencard.core.event.CTListener#cardInserted(opencard.core.event.CardTerminalEvent)
	 */
	public void cardInserted (CardTerminalEvent event) throws CardTerminalException
	{
		if (smartcard == null)
		{
			try
			{
				smartcard = SmartCard.getSmartCard (event);
				terminal = event.getCardTerminal ();
				slotID = event.getSlotID ();

				COACardInterface.getInstance ().cardInserted (smartcard , terminal , slotID);

			}
			catch (Exception e)
			{
				e.printStackTrace ();
			}
		}
	}

	/**
	 * @see opencard.core.event.CTListener#cardRemoved(opencard.core.event.CardTerminalEvent)
	 */
	public void cardRemoved (CardTerminalEvent event) throws CardTerminalException
	{
		if ((event.getSlotID () == slotID) && (event.getCardTerminal () == terminal))
		{
			smartcard = null;
			terminal = null;
			slotID = 0;

			COACardInterface.getInstance ().cardRemoved ();
		}
	}

}