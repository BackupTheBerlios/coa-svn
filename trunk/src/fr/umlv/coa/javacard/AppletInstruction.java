/* 
 * File    : AppletInstruction.java
 * Created : 25 févr. 2005
 * 
 * =======================================
 * COA PROJECT ("http://coa.berlios.de")
 * =======================================
 *
 */

package fr.umlv.coa.javacard;

/**
 * @author Administrateur
 *
 */
public class AppletInstruction
{
	private String	name;
	private byte	returnType;
	
	
	
	/**
	 * @param name
	 * @param returnType
	 */
	public AppletInstruction (String name, byte returnType)
	{
		this.name = name;
		this.returnType = returnType;
	}
	
	
	/**
	 * @return Returns the name.
	 */
	public String getName ()
	{
		return name;
	}
	/**
	 * @return Returns the returnType.
	 */
	public byte getReturnType ()
	{
		return returnType;
	}
}
