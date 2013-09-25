package com.server.interf;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

// Implementation of ServerInterface
// ServerInterfaceImpl.java

public class ServerInterfaceImpl extends UnicastRemoteObject implements
		ServerInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private File initDir = new File(".");

	// Creating a HashMap for storing the file List sent from peer.
	HashMap<String, String> peersMap = new HashMap<String, String>();

	public ServerInterfaceImpl() throws RemoteException {
		super();
	}

	@Override
	public String search(String filename) throws RemoteException {
		if (!peersMap.containsKey(filename))
			return null;
		else{
			String fileURL = peersMap.get(filename);
			String [] files = fileURL.split(",");
			return files[0];
		}
	}

	@Override	
	public void registry(ArrayList<String> fileList, String peersURL)
			throws RemoteException {
		for (int i = 0; i < fileList.size(); i++) {
			String filename = (String) fileList.get(i);
			boolean flag = false;
			if (!peersMap.containsKey(filename))
				peersMap.put(filename, peersURL);
			else {
				String [] urlList = peersMap.get(filename).split(",");
				for(String list: urlList){
					if(list.equals(peersURL)){
						flag = true;
						break;
						}
				}
				if(!flag)
					peersMap.put(filename, peersURL + "," + peersMap.get(filename));
			}
			System.out.println(filename + " URL: " + peersMap.get(filename));
		}
	}

	// Temporary
	@Override
	public byte[] fileToByte(String filename) throws RemoteException {
		byte[] buffer = null;
		try {
			File fin = new File(initDir + "//" + filename);
			System.out.println(fin.getCanonicalPath()
					+ "\n Packing the file ...\n");
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(fin));

			buffer = new byte[(int) fin.length()];
			bis.read(buffer, 0, buffer.length);
			bis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

}
