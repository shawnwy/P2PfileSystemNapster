package com.server.interf;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

// Interface of Binding Server
// ServerInterface.java

public interface ServerInterface extends Remote {
	// Search file "filename" from List of Server
	public String search (String filename) 
			throws RemoteException; 
	
	// Registry connected peers to Binding Server
	public void registry (ArrayList <String> fileList, String peersURL) 
			throws RemoteException;

	byte[] fileToByte(String filename) 
			throws RemoteException;
}
