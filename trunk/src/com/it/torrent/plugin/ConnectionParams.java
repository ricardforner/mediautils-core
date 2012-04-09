package com.it.torrent.plugin;

public class ConnectionParams {

	private String hostIP;
	private int hostPort;
	private String authUsername;
	private String authPassword;
	
	/**
	 * @return the hostIP
	 */
	public String getHostIP() {
		return hostIP;
	}
	
	/**
	 * @param hostIP the hostIP to set
	 */
	public void setHostIP(String hostIP) {
		this.hostIP = hostIP;
	}
	
	/**
	 * @return the hostPort
	 */
	public int getHostPort() {
		return hostPort;
	}
	
	/**
	 * @param hostPort the hostPort to set
	 */
	public void setHostPort(int hostPort) {
		this.hostPort = hostPort;
	}
	
	/**
	 * @return the authUsername
	 */
	public String getAuthUsername() {
		return authUsername;
	}
	
	/**
	 * @param authUsername the authUsername to set
	 */
	public void setAuthUsername(String authUsername) {
		this.authUsername = authUsername;
	}
	
	/**
	 * @return the authPassword
	 */
	public String getAuthPassword() {
		return authPassword;
	}
	
	/**
	 * @param authPassword the authPassword to set
	 */
	public void setAuthPassword(String authPassword) {
		this.authPassword = authPassword;
	}
	
}
