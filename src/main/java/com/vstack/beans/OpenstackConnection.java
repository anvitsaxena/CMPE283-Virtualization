package com.vstack.beans;

public class OpenstackConnection {
	private String server = null;
	//private String port = null;
	private String username = null;
	private String password = null;

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	/*public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
*/
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return String.format("{server:%s}", server);
	}
}
