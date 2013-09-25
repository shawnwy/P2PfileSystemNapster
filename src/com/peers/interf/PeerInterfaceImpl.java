package com.peers.interf;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

// Implementation of Peers' Interface
// PeerInterfaceImpl.java

public class PeerInterfaceImpl extends UnicastRemoteObject implements PeerInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private File initDir;
	
	public PeerInterfaceImpl(File dir) throws RemoteException {
		super();
		initDir = dir;
		// TODO Auto-generated constructor stub
	}


	@Override
	public void updatefile() throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	// transform file to Byte Stream
	public byte [] fileToByte (String filename) throws RemoteException{
		// TODO Auto-generated method stub
		byte [] buffer = null;
		try{
			File fin = new File(initDir+"//"+filename);
			System.out.println(fin.getCanonicalPath()+"\n Packing the file ...\n");
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fin));

			buffer = new byte[(int) fin.length()];
			bis.read(buffer,0,buffer.length);
			bis.close();
		} catch (FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		return buffer;
	}

	/*
	@Override
	public ArrayList<String> scanDirectoryContents(File dir) throws RemoteException {
		// TODO Auto-generated method stub
		// scan local files. Form a File List.
		ArrayList <String> fileList = new ArrayList <String>();
		try{
			File [] files = dir.listFiles();
			
			for(File file:files) {
				if(!file.isDirectory())
					fileList.add(file.getName());
			}
		} catch (Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		return fileList;
	}*/
	

}
