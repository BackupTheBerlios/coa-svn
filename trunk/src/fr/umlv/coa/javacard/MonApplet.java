/* 
 * File    : MonApplet.java
 * Created : 24 févr. 2005
 * 
 * =======================================
 * COA PROJECT ("http://coa.berlios.de")
 * =======================================
 *
 */

package fr.umlv.coa.javacard;

import javacard.framework.APDU;
import javacard.framework.Applet;
import javacard.framework.ISO7816;
import javacard.framework.ISOException;
import javacard.framework.Util;

/**
 * @author Ludo
 *
 */
public class MonApplet extends Applet
{

	private final static byte CLA_MON_APPLET = (byte)0x91;
	private final static byte GET_NAME = (byte) 0xFF;
	private final static byte INS_GET_NAME = (byte) 0x13;
	private final static byte INS_GET_NUMBER = (byte) 0x14;
	private final static byte INS_GET_ARG = (byte) 0x15;
	private final static byte INS_SET_ARG = (byte) 0x16;
	private final static byte INS_DESC = (byte) 0xFD;
	private final static byte INS_LIST_INS = (byte) 0xFE;
	private final static byte [] DESC_OK = {(byte)0x12, (byte)0x03};
	private final static byte [] DESC_NOK = {(byte)0x12, (byte)0x04};
	private final static byte [] LIST_INS = {(byte)INS_GET_NAME, (byte)INS_GET_NUMBER, (byte)INS_GET_ARG, (byte)INS_SET_ARG};	
	private final static byte [] INS_GET_NAME_DESC = {(byte)'g', (byte)'e',(byte)'t',(byte)'N',(byte)'a',(byte)'m',(byte)'e'};
	private final static byte [] INS_GET_ARG_DESC = {(byte)'g', (byte)'e',(byte)'t',(byte)'A',(byte)'r',(byte)'g'};
	private final static byte [] INS_SET_ARG_DESC = {(byte)'s', (byte)'e',(byte)'t',(byte)'A',(byte)'r',(byte)'g'};
	private final static byte [] INS_GET_NUMBER_DESC = {(byte)'g', (byte)'e',(byte)'t',(byte)'N',(byte)'u',(byte)'m',(byte)'b',(byte)'e',(byte)'r'};
	private final static byte [] NAME = {(byte)'M', (byte)'o', (byte)'n', (byte)'A', (byte)'p', (byte)'p', (byte)'l', (byte)'e', (byte)'t' };
	private byte arg = (byte)0;
	
	//----------------------------------------------------------//
	//------------------- CONSTRUCTORS -------------------------//
	//----------------------------------------------------------//

	/**
	 * @param buffer 
	 * @param offset 
	 * @param length 
	 * 
	 */
	public MonApplet (byte [] buffer, short offset, byte length)
	{
		register ();
	}

	//----------------------------------------------------------//
	//------------------- PUBLIC METHODS -----------------------//
	//----------------------------------------------------------//

	
	/**
	 * @param buffer
	 * @param offset
	 * @param length
	 */
	public static void install (byte [] buffer, short offset, byte length)
	{
		new MonApplet (buffer, offset, length);
		
	}
	
	/**
	 * @see javacard.framework.Applet#process(javacard.framework.APDU)
	 */
	public void process (APDU arg0) throws ISOException
	{
		
		byte [] apduBuffer = arg0.getBuffer ();
		
		if (apduBuffer [ISO7816.OFFSET_CLA] == CLA_MON_APPLET)
		{
			switch (apduBuffer [ISO7816.OFFSET_INS])
			{
				case GET_NAME :
					sendAppletName (arg0);
					break;
				case INS_GET_NAME :
					getName (arg0);
					break;
				case INS_DESC :
					sendDesc (arg0);
					break;
				case INS_LIST_INS :
					sendListIns (arg0);
					break;
				case INS_GET_NUMBER :
					getNumber (arg0);
					break;
				case INS_GET_ARG :
					getArg (arg0);
					break;
				case INS_SET_ARG :
					setArg (arg0);
					break;
					
				default :
					ISOException.throwIt (ISO7816.SW_INS_NOT_SUPPORTED);
			}
		}
	}
	
	/**
	 * @see javacard.framework.Applet#deselect()
	 */
	public void deselect ()
	{
		super.deselect ();
	}
	
	/**
	 * @see javacard.framework.Applet#select()
	 */
	public boolean select ()
	{
		return super.select ();
	}
	
	//----------------------------------------------------------//
	//------------------- PRIVATE METHODS ----------------------//
	//----------------------------------------------------------//

	
	/**
	 * @param apdu
	 */
	private void getNumber (APDU apdu)
	{
		byte [] apduBuffer = apdu.getBuffer ();
		
		apduBuffer [5] = (byte) 0x12;
		apduBuffer [6] = (byte) 0x03;
		
		apdu.setOutgoing ();
		
		apdu.setOutgoingLength ((short) 2);
		
		apdu.sendBytes ((short) 5 , (short) 2);
	}
	
	/**
	 * @param apdu
	 */
	private void getName (APDU apdu)
	{
		byte [] apduBuffer = apdu.getBuffer ();
		
		apduBuffer [5] = (byte) 'A';
		apduBuffer [6] = (byte) 'i';
		
		apdu.setOutgoing ();
		
		apdu.setOutgoingLength ((short) 2);
		
		apdu.sendBytes ((short) 5 , (short) 2);
	}

	/**
	 * @param apdu
	 */
	private void getArg (APDU apdu)
	{
		byte [] apduBuffer = apdu.getBuffer ();
		
		apduBuffer [5] = arg;
		
		apdu.setOutgoing ();
		
		apdu.setOutgoingLength ((short) 1);
		
		apdu.sendBytes ((short) 5 , (short) 1);
	}
	
	/**
	 * @param apdu
	 */
	private void setArg (APDU apdu)
	{
		byte [] apduBuffer = apdu.getBuffer ();
		
		arg = apduBuffer[5];
		
		apdu.setOutgoing ();
		
		apdu.setOutgoingLength ((short) 0);
		
		apdu.sendBytes ((short) 5 , (short) 0);
	}
	
	/**
	 * @param apdu
	 */
	private void sendAppletName (APDU apdu)
	{
		byte [] buffer = apdu.getBuffer ();
		
		Util.arrayCopy (DESC_OK , (short) 0 , buffer , (short) 0 , (short) 2);
		Util.arrayCopy (NAME , (short) 0 , buffer , (short) 2 ,
				(short) NAME.length);
		
		apdu.setOutgoing ();
		apdu.setOutgoingLength ((short) (2 + NAME.length));
		
		apdu.sendBytes ((short) 0 , (short) (2 + NAME.length));
	}
	
	/**
	 * @param apdu
	 */
	private void sendDesc (APDU apdu)
	{
		byte [] buffer = apdu.getBuffer ();
		
		switch (buffer [ISO7816.OFFSET_P1])
		{
			case INS_GET_NAME :
				sendInsDesc (apdu , INS_GET_NAME_DESC , true);
				break;
			case INS_GET_NUMBER :
				sendInsDesc (apdu , INS_GET_NUMBER_DESC , true);
				break;
			case INS_GET_ARG :
				sendInsDesc (apdu , INS_GET_ARG_DESC, true);
				break;
			case INS_SET_ARG :
				sendInsDesc (apdu , INS_SET_ARG_DESC, true);
				break;
			default :
				sendInsDesc (apdu , null , false);
		}
	}
	
	/**
	 * @param apdu
	 */
	private void sendListIns (APDU apdu)
	{
		byte [] buffer = apdu.getBuffer ();
		
		Util.arrayCopy (DESC_OK , (short) 0 , buffer , (short) 0 , (short) 2);
		Util.arrayCopy (LIST_INS , (short) 0 , buffer , (short) 2 ,
				(short) LIST_INS.length);
		
		apdu.setOutgoing ();
		apdu.setOutgoingLength ((short) (2 + LIST_INS.length));
		
		apdu.sendBytes ((short) 0 , (short) (2 + LIST_INS.length));
	}
	
	/**
	 * @param apdu
	 * @param desc
	 * @param ok
	 */
	private void sendInsDesc (APDU apdu, byte [] desc, boolean ok)
	{
		byte [] buffer = apdu.getBuffer ();
		short length = 0;
		
		if (ok)
		{
			Util.arrayCopy (DESC_OK , (short) 0 , buffer , (short) 0 ,
					(short) 2);
			Util.arrayCopy (desc , (short) 0 , buffer , (short) 2 ,
					(short) desc.length);
			length = (short) (2 + desc.length);
		}
		else
		{
			Util.arrayCopy (DESC_NOK , (short) 0 , buffer , (short) 0 ,
					(short) 2);
			length = (short) (2);
		}
		
		apdu.setOutgoing ();
		apdu.setOutgoingLength (length);
		
		apdu.sendBytes ((short) 0 , length);
	}
}
