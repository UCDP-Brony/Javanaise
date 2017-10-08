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
	
	public int getUniqueID(){
		//TODO check MAXENTRY and throw exception
		idMachine++;
		return idMachine;
	}
	
	public void registerObject(String jon, JvnObject o, JvnRemoteServer owner){
		int id = 0;
		try {
			id = o.jvnGetObjectId();
		} catch (JvnException e) {
			e.printStackTrace();
		}
		registry[id].setObject(o);
		registry[id].setCurrentOwner(owner);
		registry[id].setName(jon);
	}
	
	public JvnObject getObject(int id){
		return registry[id].getObject();
	}
	
	public JvnRemoteServer getObjectOwner(int id){
		return registry[id].getCurrentOwner();
	}
	
	public void setObject(int id, JvnObject o){
		registry[id].setObject(o);
	}
	
	public void setObjectOwner(int id, JvnRemoteServer s){
		registry[id].setCurrentOwner(s);
	}
	
	 public JvnObject lookupObject(String jon, JvnRemoteServer js) throws jvn.JvnException{
		 int i = 0;
		 while(i < MAXENTRY && !(jon.equals(registry[i].getName()) && registry[i].getCurrentOwner() == js)){
			 System.out.println(i);
			 i++;
		 }
			 
		 if(i == MAXENTRY){
			 return null;
		 }
		 return registry[i].getObject();
		 
	 }
	
}



