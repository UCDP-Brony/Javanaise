/***
 * JAVANAISE Implementation
 * JvnServerImpl class
 * Contact: 
 *
 * Authors: 
 */

package jvn;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;
import java.io.*;
import java.net.InetAddress;
import java.net.MalformedURLException;



public class JvnServerImpl 	
              extends UnicastRemoteObject 
							implements JvnLocalServer, JvnRemoteServer{
	
  // A JVN server is managed as a singleton 
	private static JvnServerImpl js = null;
	
	private static JvnRemoteCoord coord ;

	private final static String COORD = "//localhost:1050/coord";
	
	private Hashtable<Integer, JvnObject> localObjectTable;
	
	
  /**
  * Default constructor
  * @throws JvnException
  **/
	private JvnServerImpl() throws Exception {
		super();
		//TODO : agree on a static address
		try{
			this.coord = (JvnRemoteCoord) Naming.lookup(COORD);
		}
		catch(NotBoundException e){
			System.out.println("fail");
			e.printStackTrace();
		}
		catch(MalformedURLException e){
			System.out.println("fail");
			e.printStackTrace();
		}
		catch(RemoteException e){
			System.out.println("fail");
			
		}
		localObjectTable = new Hashtable<Integer, JvnObject>();
		
	}
	
  /**
    * Static method allowing an application to get a reference to 
    * a JVN server instance
    * @throws JvnException
    **/
	public static JvnServerImpl jvnGetServer() {
		if (js == null){
			try {
				js = new JvnServerImpl();
			} catch (Exception e) {
				return null;
			}
		}
		return js;
	}
	
	/**
	* The JVN service is not used anymore
	* @throws JvnException
	**/
	public  void jvnTerminate()
	throws jvn.JvnException {
		try {
			coord.jvnTerminate(js);
		} catch (RemoteException e) {
			throw new jvn.JvnException("terminate error : "+e);
		}
	} 
	
	/**
	* creation of a JVN object
	* @param o : the JVN object state
	* @throws JvnException
	**/
	public  JvnObject jvnCreateObject(Serializable o)
	throws jvn.JvnException { 
		JvnObject object = null;
		try {
			int id = coord.jvnGetObjectId();
			object = new JvnObjectImpl(o,id);
			localObjectTable.put(id,object);
		} catch (RemoteException e) {
			throw new jvn.JvnException("createObject error : "+e);
		}
		return object; 
	}
	
	/**
	*  Associate a symbolic name with a JVN object
	* @param jon : the JVN object name
	* @param jo : the JVN object 
	* @throws JvnException
	**/
	public  void jvnRegisterObject(String jon, JvnObject jo)
	throws jvn.JvnException {
		try {
			coord.jvnRegisterObject(jon, jo, this);
		} catch (RemoteException e) {
			throw new jvn.JvnException("registerObject error : "+e);
		}
	}
	
	/**
	* Provide the reference of a JVN object beeing given its symbolic name
	* @param jon : the JVN object name
	* @return the JVN object 
	* @throws JvnException
	**/
	public  JvnObject jvnLookupObject(String jon)
	throws jvn.JvnException {
		JvnObject o = null;
		try {
			o = coord.jvnLookupObject(jon, js);
		} catch (RemoteException e) {
			throw new jvn.JvnException("lookupObject error : "+e);
		}
		//check if the object is already in our cache, if it's not, add it
		if(localObjectTable.get(o.jvnGetObjectId()) == null){
			localObjectTable.put(o.jvnGetObjectId(), o);
		}
		return o;
	}	
	
	/**
	* Get a Read lock on a JVN object 
	* @param joi : the JVN object identification
	* @return the current JVN object state
	* @throws  JvnException
	**/
   public Serializable jvnLockRead(int joi)
	 throws JvnException {
	   Serializable objectState = localObjectTable.get(joi);
	   try {
		   //update our cache
		   objectState = coord.jvnLockRead(joi, js);
	   } catch (RemoteException e) {
		e.printStackTrace();
	   }
	   return objectState;
	}	
	/**
	* Get a Write lock on a JVN object 
	* @param joi : the JVN object identification
	* @return the current JVN object state
	* @throws  JvnException
	**/
   public Serializable jvnLockWrite(int joi)
	 throws JvnException {
	   Serializable objectState = localObjectTable.get(joi);
	   try {
		   objectState = coord.jvnLockWrite(joi, js);
		   } catch (RemoteException e) {
			e.printStackTrace();
		   }
		   return objectState;
	}	

	
  /**
	* Invalidate the Read lock of the JVN object identified by id 
	* called by the JvnCoord
	* @param joi : the JVN object id
	* @return void
	* @throws java.rmi.RemoteException,JvnException
	**/
  public void jvnInvalidateReader(int joi)
	throws java.rmi.RemoteException,jvn.JvnException {
		localObjectTable.get(joi).jvnInvalidateReader();
	};
	    
	/**
	* Invalidate the Write lock of the JVN object identified by id 
	* @param joi : the JVN object id
	* @return the current JVN object state
	* @throws java.rmi.RemoteException,JvnException
	**/
  public Serializable jvnInvalidateWriter(int joi)
	throws java.rmi.RemoteException,jvn.JvnException { 
	  return localObjectTable.get(joi).jvnInvalidateWriter();
	};
	
	/**
	* Reduce the Write lock of the JVN object identified by id 
	* @param joi : the JVN object id
	* @return the current JVN object state
	* @throws java.rmi.RemoteException,JvnException
	**/
   public Serializable jvnInvalidateWriterForReader(int joi)
	 throws java.rmi.RemoteException,jvn.JvnException { 
	   return localObjectTable.get(joi).jvnInvalidateWriterForReader();
	 };

}

 
