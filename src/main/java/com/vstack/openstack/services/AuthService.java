package com.vstack.openstack.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.vstack.beans.OpenstackConnection;
import com.vstack.services.VStackException;
import com.vstack.services.VStackUtils;

public class AuthService implements OpenstackAPI {

	private static Logger logger = Logger.getLogger("AuthService");
	private String authToken = null;

	public String getAuthToken() {
		return authToken;
	}

	/**
	 * Author: Tran.Pham Create date: Oct 1st, 2016 Desc: execute the http call
	 * to url
	 * 
	 * @param conn
	 * @throws VStackException
	 */
	public void setAuthToken(OpenstackConnection conn) throws VStackException {

		String api = "v3"; // TODO code for v2
		String url = "http://" + conn.getServer() + KEYSTONE_IDENTITY + AUTH_API;
		String reqBody;
		if (api.equals(API_VERSION2)) {
			reqBody = "{\"auth\":{\"passwordCredentials\":{\"username\":\"" + conn.getUsername() + "\",\"password\":\""
					+ conn.getPassword() + "\"}}}";
		} else {
			reqBody = "{\"auth\": {\"identity\": {\"methods\": [\"password\"],"
					+ "\"password\": {\"user\": {\"domain\": {\"name\": \"default\"}," + "\"name\": \""
					+ conn.getUsername() + "\",\"password\": \"" + conn.getPassword() + "\"}"
					+ "}},\"scope\": {\"project\": {\"domain\": {\"name\": \"default\"}," + "\"name\": \"admin\"}}}}";
		}
		try {

			String jsonData = VStackUtils.executeHttpPostRequest("", url, reqBody);

			if (api.equals(API_VERSION2)) {
				JSONObject jsonToken;
				JSONObject json = new JSONObject(jsonData);
				jsonToken = json.getJSONObject("access").getJSONObject("token");
				System.out.println(jsonToken.getString("id"));
				this.authToken = jsonToken.getString("id");
			} else {
				System.out.println(jsonData);
				this.authToken = jsonData;
			}

		} catch (Exception ex) {
			throw new VStackException(ex.getMessage());
		}
	}

	/**
	 * Get Openstack Projects
	 * 
	 * @throws Exception
	 */
	public List<String> getOpenstackProjects(OpenstackConnection conn, String authToken) throws VStackException {
		List<String> projectList = new ArrayList<String>();
		String url = "http://" + conn.getServer() + KEYSTONE_IDENTITY + GET_PROJECT_API;

		try {
			String json = VStackUtils.executeHttpGetRequest(authToken, url);

			JSONObject jsonObj = new JSONObject(json);
			JSONArray jsonToken = jsonObj.getJSONArray("projects");
			int i = 0;

			while (i < jsonToken.length()) {
				String projects = jsonToken.getJSONObject(i).getString("name");
				projectList.add(projects);
				i++;
			}
		} catch (Exception ex) {
			logger.fatal(ex.getMessage());
			VStackUtils.returnExceptionTrace(ex);
			throw new VStackException(ex);
		}

		return projectList;
	}

}
