package com.rest.resource.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

public class PayslipResourceClient {

	static String uname = "admin1";
	static String pwd = "admin123";
	static String usernameAndPassword = uname + ":" + pwd;
	
	static String headerValue = "Basic"
			+ java.util.Base64.getEncoder().encodeToString(usernameAndPassword.getBytes());

	private static final String RESOURCE_URI = "http://localhost:6060/RestApp-BasicAuth/rest/payslip";

	public static void main(String[] args) {
		
		Client client = ClientBuilder.newClient();
		
		WebTarget target = client.target(RESOURCE_URI);
		
		
		String response = target.request()
				.header("Authorization", headerValue)
				.get(String.class);
				
				
		System.out.println("Response : " + response);
	}

}
