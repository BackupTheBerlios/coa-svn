package fr.umlv.coa.javacard;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import opencard.opt.applet.AppletID;


/**
 * The card Applet
 * 
 */
public final class CardApplet
{
	/** The instructions map */
	private final Map mapINS = new HashMap ();

	/** The applet name */
	private final String	name;
	/** The applet ID */
	private final AppletID	aid;

	/**
	 * Constructor
	 * 
	 * @param aid	the applet id
	 * @param name 	the applet name
	 */
	public CardApplet (AppletID aid, String name)
	{
		this.aid = aid;
		this.name = name;
	}

	/**
	 * Returns the applet ID
	 * 
	 * @return Returns the aid.
	 */
	public AppletID getAid ()
	{
		return aid;
	}

	/**
	 * Return the INS map
	 * 
	 * @return Returns the listINS.
	 */
	public Map getMapINS ()
	{
		return mapINS;
	}

	
	/**
	 * @param name
	 * @return the instruction value
	 */
	public byte getINS (String name)
	{
		byte insByte = -1;
		
		for(Iterator i = mapINS.keySet().iterator(); i.hasNext();)
		{
			AppletInstruction ins = (AppletInstruction)i.next();
			if(ins.getName().equals(name))
			{
				insByte = ins.getReturnType();
			}
		}
		
		if(insByte==-1)
			return (byte)0xFF;
		
		return insByte;
	}

	/**
	 * @param number
	 * @param ins
	 */
	public void addINS (byte number, AppletInstruction ins)
	{
		mapINS.put (ins , new Byte (number));
	}
		
	/**
	 * @return the applet name
	 */
	public String getName ()
	{
		return name;
	}
}