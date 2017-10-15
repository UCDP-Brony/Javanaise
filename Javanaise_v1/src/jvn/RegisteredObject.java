package jvn;
import java.util.ArrayList;

public class RegisteredObject {
	    private JvnObject object;
	    private String name;
		private JvnRemoteServer writer;
		private ArrayList<JvnRemoteServer> readers;
		private ArrayList<JvnRemoteServer> servers;
		
		public RegisteredObject(){
			this.servers = new ArrayList<JvnRemoteServer>();
			this.readers = new ArrayList<JvnRemoteServer>();
		}
		
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
		public JvnRemoteServer getCurrentWriter() {
			return this.writer;
		}
		public void addWriter(JvnRemoteServer writer) {
			this.writer = writer;
		}
		public void removeWriter() {
			this.writer = null;
		}
		public ArrayList<JvnRemoteServer> getCurrentReaders() {
			return readers;
		}
		public void addReaders(JvnRemoteServer reader) {
			readers.add(reader);
		}
		public void removeReader(JvnRemoteServer server){
			readers.remove(server);
		}
		public void removeAllReaders(){
			readers.clear();
		}
		public ArrayList<JvnRemoteServer> getCurrentServers() {
			return servers;
		}
		public void addServers(JvnRemoteServer server) {
			servers.add(server);
		}
		public void removeServer(JvnRemoteServer server){
			servers.remove(server);
		}
}
