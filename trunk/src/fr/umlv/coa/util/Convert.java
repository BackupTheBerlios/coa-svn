/* 
 * File    : Convert.java
 * Created : 24 févr. 2005
 * 
 * =======================================
 * COA PROJECT ("http://coa.berlios.de")
 * =======================================
 *
 */

package fr.umlv.coa.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Class containing convert routines such as :
 * <ul>
 *   <li>Convert signed type to unsigned type
 * </ul>
 */
public final class Convert
{
	//---------------------------------------------------------------------//
	//-------------------------- CONSTRUCTORS -----------------------------//
	//---------------------------------------------------------------------//
 
    /** Private constructor to avoid instantiation
     *
     */
    private Convert ()
    {
    }

	//---------------------------------------------------------------------//
	//-------------------------- PUBLIC METHODS ---------------------------//
	//---------------------------------------------------------------------//
    
    /**
     * To convert a byte array to a string
     * 
     * @param x 	  the byte array
     * @param iOffset the offset
     * 
     * @return the string converted
     */
    public static String bytesToString (byte [] x, int iOffset)
    {
    	int iIndex = iOffset;
    	
    	for (; iIndex < x.length && x [iIndex] != 0; iIndex ++);
		
    	return new String (x, iOffset, iIndex);
    }
    
    
    /** To convert a byte array to a short
     * 
     * @param x 		byte array to convert
     * @param iOffset	offset of the array to read
     * 
     * @return the short value read
     */
    public static short bytesToShort (byte [] x, int iOffset)
    {
 		return (short)( (x [iOffset] <<  8) | (x [iOffset + 1] & 0xFF));
    }


    /** To convert a stream to a short
     * 
     * @param is 		stream to convert
     * 
     * @return the short value read 
     * @throws IOException if the stream was unreadable
     */
    public static short streamToShort (InputStream is) throws IOException
    {
		return (short) ((is.read() << 8) | (is.read() & 0xFF));	
    }


	/** To convert a byte array to an integer
	 * 
	 * @param x 		byte array to convert
     * @param iOffset	offset of the array to read
	 * 
	 * @return the integer value read
	 */
    public static int bytesToInt (byte [] x , int iOffset)
    {
		if (x == null || x.length == 0)
			throw new IllegalArgumentException ("Bad byte array");
		
		switch (x.length)
		{
			case 1:
				return  (int) x [iOffset] & 0xFF;
				
			case 2:
				return  (((int) x [iOffset    ] & 0xFF) <<  8) |
					    ( (int) x [iOffset + 1] & 0xFF       );
					   
			case 3:
				return  (((int) x [iOffset    ] & 0xFF) << 16) |
					    (((int) x [iOffset + 1] & 0xFF) <<  8) |
					    ( (int) x [iOffset + 2] & 0xFF       );

			default :
				return  (  (int) x [iOffset    ]         << 24) |
				   		( ((int) x [iOffset + 1] & 0xFF) << 16) |
				   		( ((int) x [iOffset + 2] & 0xFF) <<  8) |
				   		(  (int) x [iOffset + 3] & 0xFF       );
		}
    }


	/** To convert a stream to an integer
	 * 
	 * @param is 	   stream to convert
	 * 
	 * @return the integer value read
 	 * @throws IOException if the stream was unreadable
	 */
	public static int streamToInt (InputStream is) throws IOException
    {
		return  (  (int)is.read()         << 24) |
		   		( ((int)is.read() & 0xFF) << 16) |
		   		( ((int)is.read() & 0xFF) <<  8) |
		   		(  (int)is.read() & 0xFF       );
    }


    /** To write a short into a byte array
     * 
	 * @param x 		short value to write
	 * @param buf		array into which the value is written
     * @param iOffset	offset of the array to read
     */
    public static void shortToBytes (short x, byte [] buf, int iOffset)
    {
		buf [iOffset    ] = (byte)(x >> 8);
		buf [iOffset + 1] = (byte) x      ;
    }


	/** To write a short into a stream
	 * 
	 * @param x 		short value to write
	 * @param os		stream into which the value is written
	 * 
	 * @throws IOException if the short was unwriteable into the stream
	 */
    public static void shortToStream (short x, OutputStream os) throws IOException
    {
		os.write((byte)(x >> 8));
		os.write((byte) x      );
    }


	/** To write an integer into a byte array
	 * 
	 * @param x 		integer value to write
	 * @param buf		array into which the value is written
	 * @param iOffset	offset of the array to read
	 */
	public static void intToBytes (int x, byte [] buf, int iOffset)
	{
		buf [iOffset    ] = (byte)(x >> 24);
		buf [iOffset + 1] = (byte)(x >> 16);
		buf [iOffset + 2] = (byte)(x >>  8);
		buf [iOffset + 3] = (byte) x       ;
	}


	/** To write an integer into a stream
	 * 
	 * @param x 	   integer value to write
	 * @param os	   stream into which the value is written
	 * 
	 * @throws IOException if the integer was unwriteable into the stream
	 */
	public static void intToStream (int x, OutputStream os) throws IOException
    {
		os.write((byte)(x >> 24));
		os.write((byte)(x >> 16));
		os.write((byte)(x >>  8));
		os.write((byte) x       );
    }

    /**
     * Interprets the value of x as an unsigned byte, and returns
     * it as integer.  For example, ubyte2int(0xFF) == 255, not -1.
     * 
     * @param x byte to convert
     * @return unsigned value of the byte given in param
     */
    public static int toUnsignedByte (byte x)
    {
        return (int) x & 0xFF;
    }

    /**
     * Interprets the value of x as an unsigned two-byte number.
     * 
     * @param x short to convert
     * @return unsigned value of the short given in param
     **/
    public static int toUnsignedShort (short x)
    {
        return (int) x & 0xFFFF;
    }

    /**
     * Interprets the value of x as an unsigned four-byte number.
     *
     * @param x int to convert
     * @return unsigned value of the int given in param
     **/
    public static long toUnsignedInt (int x)
    {
        return (long) x & 0xFFFFFFFFL;
    }

    /**
     * Returns the closest int value of a long given in param
     * 
     * @param l long to convert into integer
     * @return int value of the long given in param
     */
    public static int longToInt (long l)
    {
        int m;
        
        if (l < (m = Integer.MAX_VALUE) && l > (m = Integer.MIN_VALUE))
            return (int) l;
            
        return m;
    }
}
