package com.vstack.openstack.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.vstack.beans.ComputeInstance;
import com.vstack.services.VStackException;
import com.vstack.services.VStackUtils;

/**
 * Parse the API Response
 * @author poojashah
 *
 */
public class InstanceService {
	
	private static Logger logger = Logger.getLogger("InstanceService");
	
	private String server, token ="";
	
	public InstanceService(String server, String token) {
		// TODO Auto-generated constructor stub
		this.server = server;
		this.token = token;
	}
	 
	/**
	 * Get all Instances
	 * 
	 * @throws Exception
	 */
	public Map<String, String> getInstances() throws  VStackException {
		Map<String, String> instanceList = new HashMap<String, String>();
		 
		try { 
			OpenStackApiService apiService = new OpenStackApiService(server, token);
			String response = apiService.getInstanceList();
			
			JSONObject jsonObj = new JSONObject(response);
			JSONArray jsonToken = jsonObj.getJSONArray("servers");
			int i = 0;

			while (i < jsonToken.length()) {
				String instance = jsonToken.getJSONObject(i).getString("name");
				String instanceID = jsonToken.getJSONObject(i).getString("id");
				instanceList.put(instance, instanceID);
				i++;
			}
		} catch (Exception ex) {
			logger.fatal(ex.getMessage());
			VStackUtils.returnExceptionTrace(ex);
			throw new VStackException(ex);
		}
		return instanceList;
	}
	
	/**
	 * Get an Instance
	 * 
	 * @throws Exception
	 */
	public ComputeInstance getInstanceByName(String name) throws  VStackException {
		ComputeInstance instanceDetails = new ComputeInstance();
		 
		try { 
			OpenStackApiService apiService = new OpenStackApiService(server, token);
			Map<String, String> list = getInstances();
			
			String response = apiService.getInstance(list.get(name));
			logger.info("Find instance info for " + name);
			JSONObject jsonObj = new JSONObject(response).getJSONObject("server");
			
			String status = jsonObj.getString("status");
			JSONObject ifArray = jsonObj.getJSONObject("addresses");
			if(ifArray.length() > 0) {
				JSONArray ipAddresses = ifArray.getJSONArray("provider");
				int i =0; 
				List<String> networkList = new ArrayList<String>();
				while(i< ipAddresses.length()) {
					String ip = ipAddresses.getJSONObject(i).getString("addr");
					networkList.add(ip);
					i++;
				}
			}
			
			
			String flavor = jsonObj.getJSONObject("flavor").getString("id");
			String image = jsonObj.getJSONObject("image").getString("id");
			//Get Image and flavor name
			//Map<String, String> imgList = getImages();
			
			instanceDetails.setFlavor(flavor);
			instanceDetails.setStatus(status);
			instanceDetails.setImage(image);
			instanceDetails.setInstanceName(name);
			
		} catch (Exception ex) {
			logger.fatal(ex.getMessage());
			VStackUtils.returnExceptionTrace(ex);
			throw new VStackException(ex);
		}
		return instanceDetails;
	}
	
	/**
	 * Get an Instance
	 * 
	 * @throws Exception
	 */
	public ComputeInstance getInstanceById(String id) throws  VStackException {
		ComputeInstance instanceDetails = new ComputeInstance();
		 
		try { 
			OpenStackApiService apiService = new OpenStackApiService(server, token);
			String response = apiService.getInstance(id);
			logger.info("Find instance info for " + id);
			JSONObject jsonObj = new JSONObject(response).getJSONObject("server");
			
			String status = jsonObj.getString("status");
			
			JSONArray ipAddresses = jsonObj.getJSONObject("addresses").getJSONArray("provider");
			int i =0; 
			List<String> networkList = new ArrayList<String>();
			while(i< ipAddresses.length()) {
				String ip = ipAddresses.getJSONObject(i).getString("addr");
				networkList.add(ip);
				i++;
			}
			
			String flavor = jsonObj.getJSONObject("flavor").getString("id");
			String image = jsonObj.getJSONObject("image").getString("id");
			//Get Image and flavor name
			//Map<String, String> imgList = getImages();
			String name = jsonObj.getString("name");
			
			instanceDetails.setFlavor(flavor);
			instanceDetails.setStatus(status);
			instanceDetails.setImage(image);
			instanceDetails.setInstanceName(name);
			
		} catch (Exception ex) {
			logger.fatal(ex.getMessage());
			VStackUtils.returnExceptionTrace(ex);
			throw new VStackException(ex);
		}
		return instanceDetails;
	}
	/**
	 * Get Openstack Images
	 * 
	 * @throws Exception
	 */
	public Map<String, String> getImages() throws  VStackException {
		Map<String, String> instanceList = new HashMap<String, String>();
		 
		try { 
			OpenStackApiService apiService = new OpenStackApiService(server, token);
			String response = apiService.getImageList();
			
			JSONObject jsonObj = new JSONObject(response);
			JSONArray jsonToken = jsonObj.getJSONArray("images");
			int i = 0;

			while (i < jsonToken.length()) {
				String image = jsonToken.getJSONObject(i).getString("name");
				String imageID = jsonToken.getJSONObject(i).getString("id");
				System.out.println("Found Image " + image + " ID " + imageID);
				logger.info("Found Image " + image + " ID " + imageID);
				instanceList.put(image, imageID);
				i++;
			}
		} catch (Exception ex) {
			logger.fatal(ex.getMessage());
			VStackUtils.returnExceptionTrace(ex);
			throw new VStackException(ex);
		}
		return instanceList;
	}
	
	
	/**
	 * launch Instance
	 * 
	 * @throws Exception
	 */
	public Map<String, String> launchInstance(ComputeInstance computeInstance) throws  VStackException {
		Map<String, String> instanceList = new HashMap<String, String>();
		 
		InstanceService instanceService = new InstanceService(server, token);
		String imageref = instanceService.getImages().get(computeInstance.getImage());
		
		FlavorService flavorService = new FlavorService();
		String flavorref = flavorService.getFlavors(server, token).get(computeInstance.getFlavor());
		
		String jsonInput = "{\"server\": {\"name\": \""+ computeInstance.getInstanceName() +"\", " +
		"\"imageRef\": \"" + imageref + "\", "+
		"\"flavorRef\": \""+ flavorref + "\", "+
		"\"max_count\": 1, \"min_count\": 1,"+ 
		"\"security_groups\": [{\"name\": \"default\"}]}"+
		"}";
		
		try { 
			OpenStackApiService apiService = new OpenStackApiService(server, token);
			String response = apiService.launchInstance(jsonInput);
			
			JSONObject jsonObj = new JSONObject(response);
			String serverID = jsonObj.getJSONObject("server").getString("id");
			instanceList.put(computeInstance.getInstanceName(), serverID);
			
		} catch (Exception ex) {
			logger.fatal(ex.getMessage());
			VStackUtils.returnExceptionTrace(ex);
			throw new VStackException(ex);
		}
		return instanceList;
	}
	
	
	/**
	 * Stop Instance
	 * 
	 * @throws Exception
	 */
	public Map<String, String> stopInstance(ComputeInstance computeInstance) throws  VStackException {
		Map<String, String> instanceList = new HashMap<String, String>();
		 
		InstanceService instanceService = new InstanceService(server, token);
		String imageref = instanceService.getImages().get(computeInstance.getImage());
		
		FlavorService flavorService = new FlavorService();
		String flavorref = flavorService.getFlavors(server, token).get(computeInstance.getFlavor());
		
		String jsonInput = "{\"server\": {\"name\": \""+ computeInstance.getInstanceName() +"\", " +
		"\"imageRef\": \"" + imageref + "\", "+
		"\"flavorRef\": \""+ flavorref + "\", "+
		"\"max_count\": 1, \"min_count\": 1,"+ 
		"\"security_groups\": [{\"name\": \"default\"}]}"+
		"}";
		
		try { 
			OpenStackApiService apiService = new OpenStackApiService(server, token);
			String response = apiService.launchInstance(jsonInput);
			
			JSONObject jsonObj = new JSONObject(response);
			String serverID = jsonObj.getJSONObject("server").getString("id");
			instanceList.put(computeInstance.getInstanceName(), serverID);
			
		} catch (Exception ex) {
			logger.fatal(ex.getMessage());
			VStackUtils.returnExceptionTrace(ex);
			throw new VStackException(ex);
		}
		return instanceList;
	}
	
	/**
	 * Delete Instance
	 * 
	 * @throws Exception
	 */
	public Map<String, String> deleteInstance(ComputeInstance computeInstance) throws  VStackException {
		Map<String, String> instanceList = new HashMap<String, String>();
		 
		InstanceService instanceService = new InstanceService(server, token);
		String imageref = instanceService.getImages().get(computeInstance.getImage());
		
		FlavorService flavorService = new FlavorService();
		String flavorref = flavorService.getFlavors(server, token).get(computeInstance.getFlavor());
		
		String jsonInput = "{\"server\": {\"name\": \""+ computeInstance.getInstanceName() +"\", " +
		"\"imageRef\": \"" + imageref + "\", "+
		"\"flavorRef\": \""+ flavorref + "\", "+
		"\"max_count\": 1, \"min_count\": 1,"+ 
		"\"security_groups\": [{\"name\": \"default\"}]}"+
		"}";
		
		try { 
			OpenStackApiService apiService = new OpenStackApiService(server, token);
			String response = apiService.launchInstance(jsonInput);
			
			JSONObject jsonObj = new JSONObject(response);
			String serverID = jsonObj.getJSONObject("server").getString("id");
			instanceList.put(computeInstance.getInstanceName(), serverID);
			
		} catch (Exception ex) {
			logger.fatal(ex.getMessage());
			VStackUtils.returnExceptionTrace(ex);
			throw new VStackException(ex);
		}
		return instanceList;
	}
	
}
