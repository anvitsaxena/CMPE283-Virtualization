package com.vstack.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;

/**
 * Utility Class for the Aegis Toolkit
 */

public class VStackUtils {

	private static ServletContext servletContext = null;

	private static String contextPath = null;

	/**
	 * File Separator
	 */
	public static String FS = System.getProperty("file.separator");

	/**
	 * Temporary Directory for putting Logs and Scripts
	 */
	public static String SYSTEM_TMP_DIR = System.getProperty("java.io.tmpdir");

	/**
	 * Log Directory for putting Logs and Scripts
	 */
	public static String LOG_DIR = SYSTEM_TMP_DIR + FS + "logs";

	/**
	 * Temporary Directory for Charts
	 */
	public static String CHARTS_DIR = "charts";

	/**
	 * Throw exception
	 * 
	 * @param ex
	 * @return
	 */
	public static String returnExceptionTrace(Exception ex) {
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		return writer.toString();
	}

	/**
	 * Return the ServletContext from the Controllers
	 * 
	 * @return
	 */
	public static ServletContext getServletContext() {
		return servletContext;
	}

	/**
	 * Sets the ServletContext
	 * 
	 * @param servletContext
	 */
	public static void setServletContext(ServletContext servletContext) {
		VStackUtils.servletContext = servletContext;
	}

	/**
	 * Get the servlet context PAth
	 * 
	 * @return
	 */
	public static String getContextPath() {
		return contextPath;
	}

	/**
	 * Set the servlet context path
	 * 
	 * @param contextPath
	 */
	public static void setContextPath(String contextPath) {
		VStackUtils.contextPath = contextPath;
	}

	// Author: Tran.Pham
	// Create date: Oct 1st, 2016
	// Desc: execute the http call to url
	public static String executeHttpPostRequest(String token, String url, String body)
			throws IOException, ClientProtocolException, JSONException {

		HttpPost Req = new HttpPost(url);
		Req.addHeader("Content-Type", "application/json");
		Req.addHeader("accept", "application/json");
		if (!token.isEmpty())
			Req.addHeader("X-Auth-Token", token);
		if (!body.isEmpty()) {
			HttpEntity entity = new ByteArrayEntity(body.getBytes());
			Req.setEntity(entity);
		}

		return executeHttpRequest(Req);
	}

	// Author: Tran.Pham
	// Create date: Oct 1st, 2016
	// Desc: execute the http call to url
	public static String executeHttpGetRequest(String token, String url)
			throws IOException, ClientProtocolException, JSONException {

		HttpGet Req = new HttpGet(url);
		Req.addHeader("Content-Type", "application/json");
		Req.addHeader("accept", "application/json");
		if (!token.isEmpty())
			Req.addHeader("X-Auth-Token", token);

		return executeHttpRequest(Req);
	}

	/**
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String executeHttpRequest(HttpUriRequest request) throws IOException, ClientProtocolException {
		 
		HttpClient httpClient1 = HttpClientBuilder.create().build();
		HttpResponse response1 = httpClient1.execute(request);
		int statusCode = response1.getStatusLine().getStatusCode();
		switch (statusCode) {
		case 200:
			break;
		// Auth-Token-Response
		case 201: {
			String xToken = response1.getFirstHeader("X-Subject-Token").getValue();
			return xToken;
		}
		// POST /Servers response
		case 202: {
			break;
		}
		case 300:
			throw new RuntimeException("Multiple version of API detected : " + statusCode);
		default:
			throw new RuntimeException("Failed : HTTP error code : " + statusCode);
		}
		;

		BufferedReader br = new BufferedReader(new InputStreamReader((response1.getEntity().getContent())));

		String jsonData = "";
		String line;
		while ((line = br.readLine()) != null)
			jsonData += line + "\n";

		httpClient1.getConnectionManager().shutdown();

		return jsonData;
	}

	/**
	 * Handle RuntimeException
	 * 
	 * @param ex
	 * @param response
	 * @param message
	 * @throws IOException
	 */
	public static void handleRuntimeException(Exception ex, HttpServletResponse response, String message)
			throws IOException {
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		response.getWriter().write(message + " " + ex.getMessage());
		response.flushBuffer();
	}

	/**
	 * Handle Runtime Error
	 * 
	 * @param ex
	 * @param response
	 * @param message
	 * @throws IOException
	 */
	public static void handleRuntimeError(HttpServletResponse response, String message) throws IOException {
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		if (message != null) {
			response.getWriter().write(message);
		} else {
			response.getWriter().write("");
		}
		response.flushBuffer();
	}

	/**
	 * Handle Runtime Error
	 * 
	 * @param ex
	 * @param response
	 * @param message
	 * @throws IOException
	 */
	public static void handleResponse(HttpServletResponse response, String message) throws IOException {
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().write(message);
		response.flushBuffer();
	}

}
