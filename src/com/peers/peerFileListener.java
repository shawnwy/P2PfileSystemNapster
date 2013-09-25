package com.peers;

import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;


public class peerFileListener extends FileAlterationListenerAdaptor {
	@Override
	public void onFileCreate(File file) {
		Peers.updateFileList();
	}

	@Override
	public void onFileChange(File file) {
	}

	@Override
	public void onFileDelete(File file) {
		Peers.updateFileList();
	}

}

