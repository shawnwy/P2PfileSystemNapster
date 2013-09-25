package com.server;

import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import com.server.interf.ServerInterfaceImpl;

public class Server {
	// configure Server's Port
	private static final int PORT = 10001;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			// get the LOCAL HOST IP
			InetAddress host = InetAddress.getLocalHost();
			String hostURL = host.getHostAddress();
			System.out.println(hostURL + "\n");

			// construct Server on RMI registry
			System.out.println("Establishing Server ... \n");
			LocateRegistry.createRegistry(PORT);
			ServerInterfaceImpl s1 = new ServerInterfaceImpl();
			System.out.println("Binding Server implementations to registry");
			Naming.rebind("rmi://" + hostURL + ":" + PORT + "/comm", s1);
			System.out.println("Waiting for clients ...");
		} catch (Exception e) {
			System.out.println("Error:\n" + e);
		}
	}

}
