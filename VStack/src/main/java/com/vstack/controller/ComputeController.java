package com.vstack.controller;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vstack.beans.ComputeInstance;
import com.vstack.beans.OpenstackConnection;
import com.vstack.openstack.services.AuthService;
import com.vstack.openstack.services.FlavorService;
import com.vstack.openstack.services.InstanceService;
import com.vstack.openstack.services.OpenStackApiService;
import com.vstack.openstack.services.OpenstackAPI;
import com.vstack.services.Bootstrap;
import com.vstack.services.VStackUtils;

@Controller
public class ComputeController implements OpenstackAPI {

	@Autowired
	ServletContext context;

	@Autowired
	Bootstrap bootstrap;

	@Autowired
	AuthService authentication;

	@Autowired
	OpenstackConnection connection;
	
	private static Logger logger = Logger.getLogger("ComputeController");

	/**
	 * Get Flavors
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getFlavor", method = RequestMethod.GET)
	public @ResponseBody String getFlavor(HttpServletResponse response) {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		logger.info("\n--------- Getting Flavor Data ---------");

		try {
			
			FlavorService flavorAPI = new FlavorService();
			Map<String, String> flavorList = flavorAPI.getFlavors(connection.getServer(), authentication.getAuthToken());
			
			return ow.writeValueAsString(flavorList);

		} catch (Exception ex) {
			logger.fatal(ex.getMessage());
			logger.fatal(VStackUtils.returnExceptionTrace(ex));
			return null;
		}
	}
	
	/**
	 * Get Instances
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getInstances", method = RequestMethod.GET)
	public @ResponseBody String getInstances(HttpServletResponse response) {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		logger.info("\n--------- Getting Instance List ---------");

		try {
			
			InstanceService instanceService = new InstanceService(connection.getServer(), authentication.getAuthToken());
			Map<String, String> instanceList = instanceService.getInstances();
			
			return ow.writeValueAsString(instanceList);

		} catch (Exception ex) {
			logger.fatal(ex.getMessage());
			logger.fatal(VStackUtils.returnExceptionTrace(ex));
			return null;
		}
	}
	
	/**
	 * Get Instance
	 * @param instance
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/getInstance", method = RequestMethod.POST)
	public void getInstance(@RequestBody String instance, HttpServletResponse response) throws IOException {
		
		if (connection != null) {
			try {

				logger.info("------ Get Instance ------");
				InstanceService instanceService = new InstanceService(connection.getServer(), authentication.getAuthToken());
				ComputeInstance details = instanceService.getInstanceByName(instance);

				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				

				VStackUtils.handleResponse(response, ow.writeValueAsString(details));
			} catch (UnknownHostException ex) {
				logger.fatal(VStackUtils.returnExceptionTrace(ex));
				VStackUtils.handleRuntimeException(ex, response, "Unknown Host " + connection.getServer());
			} catch (Exception ex) {
				logger.fatal(ex.getMessage());
				logger.fatal(VStackUtils.returnExceptionTrace(ex));
				VStackUtils.handleRuntimeException(ex, response, "Failed to establish connection. ");
			}
		} else {
			VStackUtils.handleRuntimeError(response, "Sorry, an error has occurred. Connection not added.");
		}
	}
	
	 /* Get Images
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getImages", method = RequestMethod.GET)
	public @ResponseBody String getImages(HttpServletResponse response) {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		logger.info("\n--------- Getting Image Data ---------");

		try {
			
			InstanceService imageService = new InstanceService(connection.getServer(), authentication.getAuthToken());
			Map<String, String> imageList = imageService.getImages();
			
			return ow.writeValueAsString(imageList);

		} catch (Exception ex) {
			logger.fatal(ex.getMessage());
			logger.fatal(VStackUtils.returnExceptionTrace(ex));
			return null;
		}
	}

	/**
	 * Launch Instance
	 * @param instance
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/launchInstance", method = RequestMethod.POST)
	public void launchInstance(@RequestBody ComputeInstance instance, HttpServletResponse response) throws IOException {
		
		if (connection != null) {
			try {

				logger.info("------ Launch Instance ------");
				InstanceService instanceService = new InstanceService(connection.getServer(), authentication.getAuthToken());
				Map<String, String> instanceId = instanceService.launchInstance(instance);
				ComputeInstance instanceDetail = instanceService.getInstanceByName(instance.getInstanceName());
				
				VStackUtils.handleResponse(response, "Instance Created. Status :" + instanceDetail.getStatus());
			} catch (UnknownHostException ex) {
				logger.fatal(VStackUtils.returnExceptionTrace(ex));
				VStackUtils.handleRuntimeException(ex, response, "Unknown Host " + connection.getServer());
			} catch (Exception ex) {
				logger.fatal(ex.getMessage());
				logger.fatal(VStackUtils.returnExceptionTrace(ex));
				VStackUtils.handleRuntimeException(ex, response, "Failed to launch instance ");
			}
		} else {
			VStackUtils.handleRuntimeError(response, "Sorry, an error has occurred. Connection not added.");
		}
	}

	/**
	 * Delete Instance
	 * @param instance
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/deleteInstance", method = RequestMethod.POST)
	public void deleteInstance(@RequestBody String instance, HttpServletResponse response) throws IOException {
		
		if (connection != null) {
			try {

				logger.info("------ Launch Instance ------");
				InstanceService instanceService = new InstanceService(connection.getServer(), authentication.getAuthToken());
				 Map<String, String> instances= instanceService.getInstances();
				
				OpenStackApiService apiService = new OpenStackApiService(connection.getServer(), authentication.getAuthToken());
				 apiService.deleteInstance(instances.get(instance));
				
				VStackUtils.handleResponse(response, "Instance Deleted.");
			} catch (UnknownHostException ex) {
				logger.fatal(VStackUtils.returnExceptionTrace(ex));
				VStackUtils.handleRuntimeException(ex, response, "Unknown Host " + connection.getServer());
			} catch (Exception ex) {
				logger.fatal(ex.getMessage());
				logger.fatal(VStackUtils.returnExceptionTrace(ex));
				VStackUtils.handleRuntimeException(ex, response, "Failed to establish connection. ");
			}
		} else {
			VStackUtils.handleRuntimeError(response, "Sorry, an error has occurred. Connection not added.");
		}
	}
	/**
	 * Get Projects
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getProject", method = RequestMethod.GET)
	public @ResponseBody String getProject(HttpServletResponse response) {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		logger.info("\n--------- Getting Project Data ---------");

		try {
			
			AuthService service = new AuthService();
			List<String> projects = service.getOpenstackProjects(connection, authentication.getAuthToken());
			return ow.writeValueAsString(projects);

		} catch (Exception ex) {
			logger.fatal(ex.getMessage());
			logger.fatal(VStackUtils.returnExceptionTrace(ex));
			return null;
		}
	}
	
	
	/**
	 * Stop Instance
	 * @param instance
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/stopInstance", method = RequestMethod.POST)
	public void stopInstance(@RequestBody String instance, HttpServletResponse response) throws IOException {
		
		if (connection != null) {
			try {

				logger.info("------ stop Instance ------");
				InstanceService instanceService = new InstanceService(connection.getServer(), authentication.getAuthToken());
				 Map<String, String> instances= instanceService.getInstances();
				
				OpenStackApiService apiService = new OpenStackApiService(connection.getServer(), authentication.getAuthToken());
				apiService.stopInstance(instances.get(instance));
				
				VStackUtils.handleResponse(response, "Instance Deleted.");
			} catch (UnknownHostException ex) {
				logger.fatal(VStackUtils.returnExceptionTrace(ex));
				VStackUtils.handleRuntimeException(ex, response, "Unknown Host " + connection.getServer());
			} catch (Exception ex) {
				logger.fatal(ex.getMessage());
				logger.fatal(VStackUtils.returnExceptionTrace(ex));
				VStackUtils.handleRuntimeException(ex, response, "Failed to establish connection. ");
			}
		} else {
			VStackUtils.handleRuntimeError(response, "Sorry, an error has occurred. Connection not added.");
		}
	}
}
