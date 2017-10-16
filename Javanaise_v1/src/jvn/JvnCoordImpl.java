/***
 * JAVANAISE Implementation
 * JvnServerImpl class
 * Contact: 
 *
 * Authors: 
 */

package jvn;


import java.rmi.server.UnicastRemoteObject;
import java.io.Serializable;



public class JvnCoordImpl 	
              extends UnicastRemoteObject 
							implements JvnRemoteCoord{
	

	private JvnObjectRegistry objectRegistry;
	
  /**
  * Default constructor
  * @throws JvnException
  **/
	public JvnCoordImpl() throws Exception {
		this.objectRegistry = new JvnObjectRegistry();
	}

  /**
  *  Allocate a NEW JVN object id (usually allocated to a 
  *  newly created JVN object)
  * @throws java.rmi.RemoteException,JvnException
  **/
  public synchronized int jvnGetObjectId()
  throws java.rmi.RemoteException,jvn.JvnException {
	  return objectRegistry.getUniqueID();
  }
  
  /**
  * Associate a symbolic name with a JVN object
  * @param jon : the JVN object name
  * @param jo  : the JVN object 
  * @param joi : the JVN object identification
  * @param js  : the remote reference of the JVNServer
  * @throws java.rmi.RemoteException,JvnException
  **/
  public synchronized void jvnRegisterObject(String jon, JvnObject jo, JvnRemoteServer js)
  throws java.rmi.RemoteException,jvn.JvnException{
	  objectRegistry.registerObject(jon, jo, js);
  }
  
  /**
  * Get the reference of a JVN object managed by a given JVN server 
  * @param jon : the JVN object name
  * @param js : the remote reference of the JVNServer
  * @throws java.rmi.RemoteException,JvnException
  **/
  public synchronized JvnObject jvnLookupObject(String jon, JvnRemoteServer js)
  throws java.rmi.RemoteException,jvn.JvnException{
	  //TODO : very poorly optimized
	  return objectRegistry.lookupObject(jon, js);
	}
  
  /**
  * Get a Read lock on a JVN object managed by a given JVN server 
  * @param joi : the JVN object identification
  * @param js  : the remote reference of the server
  * @return the current JVN object state
  * @throws java.rmi.RemoteException, JvnException
  **/
   public synchronized Serializable jvnLockRead(int joi, JvnRemoteServer js)
   throws java.rmi.RemoteException, JvnException{
	   RegisteredObject ro = objectRegistry.getRegisteredObject(joi);
	   Serializable o = null;
	   if(ro.getCurrentWriter() != null){
		   //Writing ongoing
		   //checking if it's not us just in case
		   if(!ro.getCurrentWriter().equals(js)){
			   o = ro.getCurrentWriter().jvnInvalidateWriterForReader(joi);
			   ro.removeWriter();
			   ro.addReaders(js);
		   }
		   ro.getObject().setSerializable(o);
	   }
	   else{
		   ro.addReaders(js);
		   o = ro.getObject().jvnGetObjectState();
	   }
	   return o;
   }

  /**
  * Get a Write lock on a JVN object managed by a given JVN server 
  * @param joi : the JVN object identification
  * @param js  : the remote reference of the server
  * @return the current JVN object state
  * @throws java.rmi.RemoteException, JvnException
  **/
   public synchronized Serializable jvnLockWrite(int joi, JvnRemoteServer js)
   throws java.rmi.RemoteException, JvnException{
	   RegisteredObject ro = objectRegistry.getRegisteredObject(joi);
	   Serializable o = null;
	   if(ro.getCurrentWriter() != null){
		   o = ro.getCurrentWriter().jvnInvalidateWriter(joi);
		   ro.getObject().setSerializable(o);
	   }
	   else{
		   o = ro.getObject().jvnGetObjectState();
	   }
	   //signal all readers
	   for(JvnRemoteServer server : ro.getCurrentReaders()){
		   if(!js.equals(server)){
			   server.jvnInvalidateReader(joi);
		   }
	   }
	   ro.removeAllReaders();
	   ro.addWriter(js);
	   return o;
   }

	/**
	* A JVN server terminates
	* @param js  : the remote reference of the server
	* @throws java.rmi.RemoteException, JvnException
	**/
    public synchronized void jvnTerminate(JvnRemoteServer js)
	 throws java.rmi.RemoteException, JvnException {
	 // to be completed
    }
}

 
