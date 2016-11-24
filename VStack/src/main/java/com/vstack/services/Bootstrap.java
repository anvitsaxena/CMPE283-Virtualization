package com.vstack.services;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;


/**
 * BootStrap Class
 * 
 * Initializes the connection. Cleans the Cache and Rebuilds it.
 * 
 */

public class Bootstrap {

	/* Logger */
	private static Logger logger = Logger.getLogger("Bootstrap");

	/* Portal base Directory */
	private String baseDir = null;

	public static boolean initialized = false;

	@Autowired
	private ApplicationContext appContext;
	

	/** Initialization Method */
	@PostConstruct
	public void start() {
		logger.info(".. Starting up server ...");
		try {
			if (initialized == false) {
				// initialization Business
				String basePath = appContext.getResource("index.html").getFile().getParent();
				this.baseDir = basePath;
				VStackUtils.setContextPath(basePath);

				/* Create Working Directories */
				createWorkingDirectories();

				initialized = true;
			} else {
				logger.info("VStack Initialization Complete...!!");
			}

		} catch (IOException e) {
			if (e.getMessage().equals("Authentication Failure")) {
				// this is a warning and not critical
				logger.warn(e.getMessage());
				initialized = true;
			} else {
				logger.fatal("VStack Initiailization failed, cannot proceed. Error " + e.getMessage());
			}
		}

	}

	/**
	 * Return the base directory of the Application
	 * 
	 * @return
	 */
	public String getApplicationBaseDir() {
		return baseDir;
	}


	/**
	 * Directory creation
	 */
	private void createWorkingDirectories() {

		String chartDir = baseDir + VStackUtils.FS + VStackUtils.CHARTS_DIR;

		// Create charts folder
		if (new File(chartDir).exists() && new File(chartDir).isDirectory()) {
		} else {
			boolean success = new File(chartDir).mkdir();
			if (!success) {
				System.out.println("Create directory charts Failed");
			}
		}

	}

} // eof class
