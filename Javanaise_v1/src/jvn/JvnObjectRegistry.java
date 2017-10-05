package jvn;

public class JvnObjectRegistry {

	private static final int MAXENTRY = 1000;
	private RegisteredObject[] registry;
	
	public JvnObjectRegistry(){
		registry = new RegisteredObject[MAXENTRY];
	}
	
	public void registerObject(int id, JvnObject o, JvnRemoteServer owner){
		registry[id].setObject(o);
		registry[id].setCurrentOwner(owner);
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
	
}



