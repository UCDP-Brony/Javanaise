package jvn;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CoordLauncher {
	

	static String address = "//localhost:1050/coord";
	
	public static void main(String [] args){
		Registry rmiRegistry;
		JvnCoordImpl JVI = null;
		try {
			JVI = new JvnCoordImpl();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try{
			rmiRegistry = LocateRegistry.createRegistry(1050);
			Naming.rebind(address, JVI);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		while(true){
			//infite loop, we want the coordinator to keep going
			//TODO : create a wait to stop the launcher and coordinator properly
		}
	}
}
