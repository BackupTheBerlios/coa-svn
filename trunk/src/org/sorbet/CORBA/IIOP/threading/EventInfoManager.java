package org.sorbet.CORBA.IIOP.threading;

import java.util.Vector;
import java.io.IOException;
/**
 *Cette classe est un container d'obget Handle Info
 *Elle est utilisé par l'Initiation Dispatcher pour garder un lien avec tout
 *les objets EventHandler qu'il gère
* @author Millo Jean-Vivien
* @version 2.5
 **/
public class EventInfoManager
{
	private Vector listOfHI=null;
	private int currentIndex;
	
	public EventInfoManager()
	{
		listOfHI=new Vector();
		currentIndex=-1;
	}
	/**
	 *Ajouter un EventHandler
	 *@param Concrete Acceptor ou Concrete connector ou Concrete ServiceHandler
	 *@param List des evenements legaux
	 **/
	public void registerHandler(EventHandler EH)
	{
		listOfHI.add(EH);
	}
	/**
	 *Supprimer un EventHandler de la liste
	 *Ferme les sockets du Handle associé
	 **/
	public void unregisterHandler(EventHandler EH)
	{
		boolean trouve=false;
		int index=0;
		while(!trouve&&index<listOfHI.size())
		{
			if(((EventHandler)listOfHI.get(index))==EH)
			{
				try{
					if(EH.getHandle()!=null)EH.getHandle().close();
					}
				catch(IOException ioe) { System.out.println(ioe.getMessage()); }
				listOfHI.remove(index);
				if(currentIndex>index)//pour ne pas sauter un handle en cas de suppression
					currentIndex--;
				trouve=true;
			}
			else
				index++;
		}
	}
	public EventHandler getCurrentEventHandler(int currentIndex)
	{
		return ((EventHandler)listOfHI.get(currentIndex));
	}
	public EventHandler getNextEventHandler(int currentIndex)
	{	
		return ((EventHandler)listOfHI.get(currentIndex++));
	}
	public Handle getHandle(int currentIndex)
	{
		return ((EventHandler)listOfHI.get(currentIndex)).getHandle();
	}
	/**
	 *@return l'index suivant de celui qui vient d'être pris
	 **/
	public int nextIndex()
	{
		if(listOfHI.size()==0)
			return -1;
		else
		{
			currentIndex=(currentIndex+1)%listOfHI.size();
			return currentIndex;
		}
	}
}
