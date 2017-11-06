package jvn;

public class JvnObjectRegistry {

	private static final int MAXENTRY = 100;
	private RegisteredObject[] registry;
	private int idMachine = -1;
	
	public JvnObjectRegistry(){
		this.registry = new RegisteredObject[MAXENTRY];
		for(int i = 0; i < MAXENTRY;i++)
			registry[i] = new RegisteredObject();
	}
	
	public int getUniqueID() throws JvnException{
		if (idMachine == 100){
			throw new JvnException("Error, number of registered objects exceeded");
		}
		idMachine++;
		return idMachine;
	}
	
	public void registerObject(String jon, JvnObject o, JvnRemoteServer server){
		int id = 0;
		try {
			id = o.jvnGetObjectId();
		} catch (JvnException e) {
			e.printStackTrace();
		}
		registry[id].setObject(o);
		registry[id].addServers(server);
		registry[id].addWriter(server);
		registry[id].setName(jon);
	}
	
	public RegisteredObject getRegisteredObject(int id){
		return registry[id];
	}
	
	public void setObject(int id, JvnObject o){
		registry[id].setObject(o);
	}
	
	
	
	 public JvnObject lookupObject(String jon, JvnRemoteServer js) throws jvn.JvnException{
		 int i = 0;
		 while(i < MAXENTRY && !jon.equals(registry[i].getName())){
			 i++;
		 }
			 
		 if(i == MAXENTRY){
			 return null;
		 }
		 registry[i].addServers(js);
		 registry[i].getObject().jvnSetToNoLock();
		 return registry[i].getObject();
		 
	 }
	 
	 /**
	  * 
	  * @param js the server terminating
	  * remove all occurrences of the server in the registered objects
	  */
	 public void terminateServer(JvnRemoteServer js){
		 int i = 0;
		 while(i < MAXENTRY){
			 registry[i].removeFromAll(js);
			 i++;
		 }
	 }
	 
	 
	
}



