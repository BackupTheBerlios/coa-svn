package fr.umlv.coa.javacard;

import java.util.HashMap;
import java.util.Map;

import opencard.opt.applet.AppletID;


/**
 */
public final class AppletCOA
{
	/** The applet ID */
	private AppletID	aid;
	private Map			mapINS	= new HashMap ();
	private String		name;

	/**
	 * @param aid
	 */
	public AppletCOA (AppletID aid, String name)
	{
		super ();
		this.aid = aid;
		this.name = name;
	}

	/**
	 * @return Returns the aid.
	 */
	public AppletID getAid ()
	{
		return aid;
	}

	/**
	 * @return Returns the listINS.
	 */
	public Map getMapINS ()
	{
		return mapINS;
	}

	/**
	 * @param name
	 * @return
	 */
	public byte getINS (String name)
	{
		return ((Byte) mapINS.get (name)).byteValue ();
	}

	/**
	 * @param number
	 * @param name
	 */
	public void addINS (byte number, String name)
	{
		mapINS.put (name , new Byte (number));
	}
		
	public String getName ()
	{
		return name;
	}
}