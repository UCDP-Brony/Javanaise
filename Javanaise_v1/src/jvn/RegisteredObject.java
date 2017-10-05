package jvn;

public class RegisteredObject {
	    private JvnObject object;
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
		private JvnRemoteServer currentOwner;
	    
}
