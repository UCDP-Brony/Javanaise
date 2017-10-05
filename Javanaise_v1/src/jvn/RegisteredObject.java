package jvn;

public class RegisteredObject {
	    private JvnObject object;
	    private String name;
		private JvnRemoteServer currentOwner;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	    
	    public JvnObject getObject() {
			return object;
		}
		public void setObject(JvnObject object) {
			this.object = object;
		}
		public JvnRemoteServer getCurrentOwner() {
			return currentOwner;
		}
		public void setCurrentOwner(JvnRemoteServer currentOwner) {
			this.currentOwner = currentOwner;
		}
	    
}
