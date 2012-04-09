package com.it.torrent.plugin.common;

import com.it.torrent.plugin.ConnectionParams;

public class PluginBase {

	private boolean bCredentials;
	private ConnectionParams params;
    
	public PluginBase(ConnectionParams params) {
		this.bCredentials = true;
		this.params = (params == null) ?  new ConnectionParams() : params;
	}

	public void initialize() {
	}

	public void setCredentials(boolean bCredentials) {
		this.bCredentials = bCredentials;
	}

	public boolean hasCredentials() {
		return bCredentials;
	}

	public ConnectionParams getParams() {
		return params;
	}
	
}
