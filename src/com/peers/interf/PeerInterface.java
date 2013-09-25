package com.peers.interf;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

// Interface of Peers'
// PeerInterface.java

public interface PeerInterface extends Remote {
	
	// Update file List in peer
	public void updatefile() throws RemoteException;
	
	// Divide the file "filename" to byte stream
	public byte [] fileToByte (String filename) throws RemoteException;
	
	// List all the names of Files in the directory "dir"
	//public ArrayList<String> scanDirectoryContents (File dir) throws RemoteException;
	
}
