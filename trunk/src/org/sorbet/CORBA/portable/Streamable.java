package org.sorbet.CORBA.portable;

/**
 * Cette interface definie les methodes standards que doivent
 * implanter les diverses classes pour signaler qu'elles supportent
 * l'encodage et le decodage de leurs donnees.
 *
 * @author	Equipe Stub&Skeleton
 * @version	2.5
 */

public interface Streamable
{
	/**
	 * Fonction de lecture depuis un flux de donnees
	 *
	 * @param	in	le flux de donnees
	 */

	public void _read(org.sorbet.CORBA.portable.InputStream in) ;
	
	/**
	 * Fonction d'ecriture dans un flux de donnees
	 *
	 *  @param	out	le flux de donnes
	 */

	public void _write(org.sorbet.CORBA.portable.OutputStream out) ;
	
}
