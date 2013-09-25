package com.peers;

/****************
 * Method:
 * void peerDownload(String filename, ServerInterface sInterface)
 * ArrayList<String> scanDirectoryContents(File dir)
 */

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.peers.interf.PeerInterface;
import com.peers.interf.PeerInterfaceImpl;
import com.server.interf.ServerInterface;

import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

public class Peers {
	// Random generate a port for peer
	static Random random = new Random();
	final static int PORT2 = random.nextInt(100) + 10002;
//	 final static int PORT2 = 10005;

	private static ServerInterface bindingServer;
	private static String peerURL;

	// Ready for creating peers local path
	static File peerDir = new File("." + "/peer" + PORT2);

	// Create a HashMap for store its own File List
	private static HashMap<String, String> peersFileMap = new HashMap<String, String>();

	// ArrayList stored current files names.
	private static ArrayList<String> checkList = new ArrayList<String>();

	// Peers Download the file from other server
	private static void peerDownload(String filename, PeerInterface peerServer)
			throws IOException {
		File fout = new File(peerDir.getCanonicalPath() + "//" + filename);
		BufferedOutputStream fos = new BufferedOutputStream(
				new FileOutputStream(fout));

		// convert file to byte stream
		byte[] buffer = peerServer.fileToByte(filename);

		System.out.println("\nDownloading ... \n");
		fos.write(buffer, 0, buffer.length);
		System.out.println("Download is completed!\n");
		fos.close();
	}

	// scan local files. Form a File List.
	private static ArrayList<String> scanDirectoryContents(File dir) {
		ArrayList<String> fileList = new ArrayList<String>();
		try {
			File[] files = dir.listFiles();

			for (File file : files) {
				if (!file.isDirectory()) {
					fileList.add(file.getName());
				} else {
					scanDirectoryContents(file);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		return fileList;
	}

	public static void updateFileList() {
		checkList = scanDirectoryContents(peerDir);
		try {
			bindingServer.registry(checkList, peerURL);
			System.out.println("update\n");
		} catch (RemoteException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Start to invoke remote method ...\n");
		String serverURL = "rmi://192.168.1.121:10001/comm";

		try {
			// Open the server of peer
			// get Host IP
			InetAddress host = InetAddress.getLocalHost();
			String hostURL = host.getHostAddress();
			System.out.println(hostURL);
			peerURL = "rmi://" + hostURL + ":" + PORT2 + "/peer";

			// Establish Peer's Server
			if (!peerDir.exists()) // Check Shared Directory exist or not
				peerDir.mkdirs(); // if not, then create a folder.
			LocateRegistry.createRegistry(PORT2); // Registry a port for peer
													// server
			System.out.println("Peer "+ PORT2 +" Binding the server ...");
			PeerInterfaceImpl ps = new PeerInterfaceImpl(peerDir); // Create a
																	// peer's
																	// stub
			Naming.rebind(peerURL, ps); // Binding to
										// peer's server
			System.out.println("Waiting for Connecting!");

			// Connect to Binding Server
			ArrayList<String> p1FileList = new ArrayList<String>(); // construct
																	// peer's
																	// own
																	// fileList
			p1FileList = scanDirectoryContents(peerDir);
			bindingServer = (ServerInterface) Naming.lookup(serverURL); // lookup
																		// Binding
																		// Server
			System.out.println("Server binded!\n");
			bindingServer.registry(p1FileList, peerURL);
			
			
			long interval = TimeUnit.SECONDS.toMillis(5);
			FileAlterationObserver observer = new FileAlterationObserver(peerDir);
			observer.addListener(new peerFileListener());
			FileAlterationMonitor monitor = new FileAlterationMonitor(interval, observer);
			monitor.start();
			
			// Create a Scanner for command parser
			Scanner scan = new Scanner(System.in);
				
			// Download File
			while (true) {

				// File monitor
				/*
				HashMap<String, String> tempMap = new HashMap<String, String>();
				checkList = scanDirectoryContents(peerDir, tempMap);
				if (checkList.size() != peersFileMap.size())
					for (String list : checkList) {
						if (!peersFileMap.containsKey(list)) {
							System.out.println("Server binded!\n");
							peersFileMap = tempMap;
							bindingServer.registry(checkList, peerURL);
						}
					}*/
				
				

				// command Parser window
				System.out.println("=======================>>\n");
				System.out.println("Peers Download System:\n");
				System.out.println("eg: download -filename.txt");
				System.out.println("(Remember Enter the file type)\n");

				String input = scan.nextLine();
				String cmd[] = input.split(" -");

				ps.updatefile();
				long startTime = System.nanoTime();
				if (cmd.length != 2)
					// # of parameters or typing error
					System.out.println("Error: Syntax Error! PLZ, reENTER!\n");
				else {
					// Search the file from binding server
					String targetURL = bindingServer.search(cmd[1]);
					// System.out.println("\nTest: URL - " + targetURL);
					if (targetURL != null) {
						// binding the target peerServer
						PeerInterface peerServer = (PeerInterface) Naming
								.lookup(targetURL);
						System.out.println("Target File Server binded!\n");
						peerDownload(cmd[1], peerServer);
					} else {
						System.out.println("File doesnt exit!\n");
					}
				}
				long endTime = System.nanoTime();
				System.out.println("Elasped Time for Download a File:\n"
						+ (endTime - startTime) / 1000000 + "ms\n");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
