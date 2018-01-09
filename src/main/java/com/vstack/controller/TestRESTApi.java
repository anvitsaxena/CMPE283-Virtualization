package com.vstack.controller;

import java.util.List;

import com.vstack.beans.OpenstackConnection;
import com.vstack.openstack.services.AuthService;

public class TestRESTApi {

	public static void main(String[] args) {
		try{
		AuthService authentication = new AuthService();
		
		OpenstackConnection conn = new OpenstackConnection();
		conn.setServer("10.0.0.11");
		conn.setUsername("admin");
		conn.setPassword("admin_user_secret");
		 
		
		} catch(Exception ex) {
			
		}
	}
}
