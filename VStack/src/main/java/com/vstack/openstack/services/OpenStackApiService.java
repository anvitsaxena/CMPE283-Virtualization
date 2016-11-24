package com.vstack.openstack.services;

import java.io.IOException;

import org.json.JSONException;

import com.vstack.services.IOpenStackAPIService;
import com.vstack.services.VStackUtils;

public class OpenStackApiService implements IOpenStackAPIService, OpenstackAPI {

	private String authToken = null;
	private String computeUrl = "";
	
	public OpenStackApiService(String server, String authToken) {
		// TODO Auto-generated constructor stub
		this.authToken = authToken;
		this.computeUrl = "http://" + server + NOVA_COMPUTE;
	}

	public String getInstance(String id) {
		// TODO Auto-generated method stub
		String json = "";
		try {
			String url =  computeUrl + "/servers/" + id;
			json = VStackUtils.executeHttpGetRequest(authToken, url);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
	@Override
	public String getInstanceList() {
		// TODO Auto-generated method stub
		String json = "";
		try {
			String url =  computeUrl + "/servers";
			json = VStackUtils.executeHttpGetRequest(authToken, url);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	@Override
	public String getFlavorList() {
		// TODO Auto-generated method stub
		String json = "";
		try {
			String url = computeUrl + "/flavors";
			json = VStackUtils.executeHttpGetRequest(authToken, url);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	@Override
	public String getImageList() {
		// TODO Auto-generated method stub
		String json = "";
		try {
			String url = computeUrl + "/images";
			json = VStackUtils.executeHttpGetRequest(authToken, url);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	// The API URL is the same with get instance list,
	// however for create new instance it's post request
	@Override
	public String launchInstance(String jsonInput) {
		// TODO Auto-generated method stub
		String json = "";
		try {
			String url = computeUrl + "/servers";
			json = VStackUtils.executeHttpPostRequest(authToken, url, jsonInput);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	// Pauses a server. Changes its status to PAUSED.
	// Specify the pause action in the request body.
	// Policy defaults enable only users with the administrative role
	// or the owner of the server to perform this operation. Cloud providers can
	// change these permissions through the policy.json file.
	@Override
	public void pauseInstance(String server_id) {
		// TODO Auto-generated method stub
		String url = computeUrl + "/servers/" + server_id + "/action";
		String body = "{\"pause\": null}";
		try {
			VStackUtils.executeHttpPostRequest(authToken, url, body);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void startInstance(String server_id) {
		// TODO Auto-generated method stub
		String url = computeUrl + "/servers/" + server_id + "/action";
		String body = "{\"os-start\": null}";
		try {
			VStackUtils.executeHttpPostRequest(authToken, url, body);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void stopInstance(String server_id) {
		// TODO Auto-generated method stub
		String url = computeUrl + "/servers/" + server_id + "/action";
		String body = "{\"os-stop\": null}";
		try {
			VStackUtils.executeHttpPostRequest(authToken, url, body);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void resumeInstance(String server_id) {

		String url = computeUrl + "/servers/" + server_id + "/action";
		String body = "{\"resume\": null}";
		try {
			VStackUtils.executeHttpPostRequest(authToken, url, body);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void deleteInstance(String server_id) {
		// TODO Auto-generated method stub
		String url = computeUrl + "/servers/" + server_id + "/action";
		String body = "{\"forceDelete\": null}";
		try {
			VStackUtils.executeHttpPostRequest(authToken, url, body);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
