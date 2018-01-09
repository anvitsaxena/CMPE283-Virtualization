
package com.vstack.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vstack.beans.OpenstackConnection;
import com.vstack.openstack.services.AuthService;
import com.vstack.services.Bootstrap;
import com.vstack.services.VStackUtils;

@Controller
public class HomeController {

	@Autowired
	ServletContext context;

	@Autowired
	Bootstrap bootstrap;
	
	@Autowired
	AuthService authentication;

	@Autowired
	OpenstackConnection connection;
	
	private static Logger logger = Logger.getLogger("HomeController");
	
	ApplicationContext dbContext = new ClassPathXmlApplicationContext("spring.xml");

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public void login(@RequestBody OpenstackConnection connection, HttpServletResponse response) throws IOException {
		
		if (connection != null) {
			try {

				logger.info("------ Login Controller ------");

				if (isServerRachable(connection)) {

					logger.info("Openstack server " + connection.getServer() + "is reachable");

					//Set Auth Token
					 
					this.authentication.setAuthToken(connection);
					this.connection.setServer(connection.getServer());
					this.connection.setUsername(connection.getUsername());
					this.connection.setPassword(connection.getPassword());
							
					/* Set Auth Token
					customUserDetailsService.setUserDao(userDao);
					UserDetails userDetails = customUserDetailsService.loadUserByUsername(connection.getUsername()); 
					Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication( authentication);
					 */
					
					logger.info("Openstack connection " + connection.getServer() + " added");
					VStackUtils.handleResponse(response, "Connection Added");

				} else {
					throw new Exception(connection.getServer() + " - Openstack Server is not reachable. ");
				}
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

		logger.info("--------------------------- Creating/Updating Connection Information Done ---------------------------\n");
	}

	
	/**
	 * GET Call for Loggedin Users
	 * 
	 * @return
	 */
	@RequestMapping(value = "/loggedinuser", method = RequestMethod.GET)
	public @ResponseBody String getLoggedinBLUsers(HttpServletResponse response) {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		logger.info("\n--------- Getting Logged in User Data ---------");

		try {
			return ow.writeValueAsString(connection);

		} catch (Exception ex) {
			logger.fatal(ex.getMessage());
			logger.fatal(VStackUtils.returnExceptionTrace(ex));
			return null;
		}
	}
	
	
	/**
	 * Check Openstack connection
	 * @param connection
	 * @return
	 * @throws IOException
	 */
	public boolean isServerRachable(OpenstackConnection connection) throws IOException {

		// Check for empty values
		if (connection.getServer().trim().isEmpty() || connection.getUsername().trim().isEmpty()
				|| connection.getPassword().trim().isEmpty()) {
			logger.fatal("No server found");
			return false;
		}

		logger.info("Checking if the Openstack " + connection.getServer() + "is alive");
		InetAddress address = InetAddress.getByName(connection.getServer());
		boolean ifreachable = address.isReachable(10000);;
		return ifreachable;
		
	}
}
