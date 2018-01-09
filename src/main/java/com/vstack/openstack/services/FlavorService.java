package com.vstack.openstack.services;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.vstack.services.VStackException;
import com.vstack.services.VStackUtils;

public class FlavorService {
	
	private static Logger logger = Logger.getLogger("FlavorService");
	
	/**
	 * Get Flavors
	 * 
	 * @throws Exception
	 */
	public Map<String, String> getFlavors(String server, String token) throws  VStackException {
		Map<String, String> flavorList = new HashMap<String, String>();
		 
		try { 
			OpenStackApiService apiService = new OpenStackApiService(server, token);
			String response = apiService.getFlavorList();
			
			JSONObject jsonObj = new JSONObject(response);
			JSONArray jsonToken = jsonObj.getJSONArray("flavors");
			int i = 0;

			while (i < jsonToken.length()) {
				String flavors = jsonToken.getJSONObject(i).getString("name");
				String flavorID = jsonToken.getJSONObject(i).getString("id");
				System.out.println("Found Flavor " + flavors + " ID " + flavorID);
				logger.info("Found Flavor " + flavors + " ID " + flavorID);
				flavorList.put(flavors, flavorID);
				i++;
			}
		} catch (Exception ex) {
			logger.fatal(ex.getMessage());
			VStackUtils.returnExceptionTrace(ex);
			throw new VStackException(ex);
		}
		return flavorList;
	}
	
}
