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
 * 
 * @author Leny
 *
 */
public final class AppletInstruction
{
	/** Applet byte return type */
	public static final byte		BYTE_RETURN			= 0;
	/** Applet short return type */
	public static final byte		SHORT_RETURN		= 1;
	/** Applet integer return type */
	public static final byte		INT_RETURN			= 2;
	/** Applet long return type */
	public static final byte		LONG_RETURN			= 3;
	/** Applet string return type */
	public static final byte		STRING_RETURN		= 4;
	/** Applet array return type */
	public static final byte		ARRAY_RETURN		= 5;
	/** Applet void return type */
	public static final byte		VOID_RETURN			= 6;
	

	/** The instruction name */
	private final String name;
	/** The return type */
	private final byte	 returnType;
	
		
	/**
	 * Constructor
	 * 
	 * @param name		 the instruction name
	 * @param returnType the instruction return type
	 */
	public AppletInstruction (String name, byte returnType)
	{
		this.name 		= name;
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
