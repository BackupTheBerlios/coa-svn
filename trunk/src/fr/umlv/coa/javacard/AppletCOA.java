package fr.umlv.coa.javacard;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import opencard.opt.applet.AppletID;


/**
 * Applet COA
 * 
 */
public final class AppletCOA
{
	/** The applet ID */
	private AppletID	aid;
	private Map			mapINS	= new HashMap ();
	private String		name;

	/**
	 * @param aid
	 * @param name 
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
	 * @param name
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