
import java.util.ArrayList;
import java.util.TreeMap;

import opencard.opt.applet.AppletID;

public class AppletCOA {

	private AppletID aid;
	private TreeMap mapINS = new TreeMap();
	
	/**
	 * @param aid
	 */
	public AppletCOA(AppletID aid) {
		super();
		this.aid = aid;
	}
	
	
	/**
	 * @return Returns the aid.
	 */
	public AppletID getAid() {
		return aid;
	}
	/**
	 * @return Returns the listINS.
	 */
	public TreeMap getMapINS() {
		return mapINS;
	}
	
	public byte getINS(String name)
	{
		return ((Byte)mapINS.get(name)).byteValue();
	}
	
	public void addINS(byte number, String name)
	{
		mapINS.put(name, new Byte(number));
	}
}
