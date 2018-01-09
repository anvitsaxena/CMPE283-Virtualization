package com.vstack.openstack.services;

public interface OpenstackAPI {

	public String API_VERSION2 = "v2";
	public String API_VERSION3 = "v3";
	
	//GET APIs
	public String AUTH_API =  "/auth/tokens";
	public String GET_PROJECT_API =  "/projects";
	public String GET_FLAVORS = "/flavors";
	public String GET_IMAGES = "/images";
	
	//POST APIs
	public String CREATE_PROJECT_API ="";
	public String CREATE_FLAVOR = "flavor";
	
	public static final String KEYSTONE_IDENTITY = ":5000/v3";
	public static final String NOVA_COMPUTE = ":8774/v2.1";
	public static final String GLANCE_IMAGE = ":9292/v3";
	public static final String NEUTRON_NETWORK = ":9696/v3";

}
