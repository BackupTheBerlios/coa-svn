
package org.sorbet.CORBA.IIOP;

/**
 * Cette classe permet la gestion des points d'écoute IIOP.<P>
 * Classe issue de l'IDL fourni par l'sorbet.<P>
 * @author Groupe IOP,IIOP,GIOP
 * @version 2.5
 */
public class ListenPoint
{
	/**
	 * Le nom d'un hote.
	 */
	protected String	_host;

	/**
	 * Le port d'ecoute de l'hote pour le service sorbet.
	 */
	protected int	_port; // short d'apres l'IDL
	
	/**
	 * Permet de modifier l'hôte
	 * @param host Nom de l'hôte à utiliser
	 */
	public void host( String host )
	{
		_host = host;
	}

	/**
	 * Retourne le nom de l'hôte
	 * @return Nom de l'hôte
	 */
	public String host()
	{
		return _host;
	}
	
	
	/**
	 * Permet de modifier le port d'écoute
	 * @param port Port d'écoute.
	 */
	public void port( int port )
	{
		_port = port;
	}

	/**
	 * Retourne le port d'écoute
	 * @return Port d'écoute
	 */
	public int port()
	{
		return _port;
	}
	
	
	/**
	 * Constructeur
	 * @param host Nom de l'hote.
	 * @param port Port d'écoute.
	 */
	public ListenPoint( String host, int port )
	{
		_host = host;
		_port = port;
	}
}
