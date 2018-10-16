Rest Security
=============


In SOAP Based Webservices, 2 actors will be involoed...

	1) Provider 

	2) Consumer

In RESTful Services, 2 actors will be involed 

	1) Server (WebResources will be deployed in Server)

	2) Client 


Once Software webcomponent development and deployment is completed, anybody can access that component who is capable of sending http request.


But we don't want to allow all the users to access my software resource.... We want to perform validation of the client before accessing the resource.

Before Starting Rest Security , first we will have glance on traditional web application flow 


								uname & pwd
			Client sends--------------------------------------------> Server : Hey server this is who i am 

Once this call is made... sever validates this usname and pwd.... 

Server makes a record and save this info in session object... server maintains user info in session. Session will have some id .... and sends back that session token...

								session token
			Client <---------------------------------------------------- Server 

Next time client sends token to server.... hey server this is my id... can u recognize me..

Client ----------session token ----------> Server 

Client -----------Session token ------------> Server... and so on.... 

When server log off... session will be removed.. most of the applications will use this kind of authentication....

But this is problem in Rest API... bcz Rest API is Stateless... Rest App will not maintain anything in the Server session....

Client..... reuqest---> ..... server 

client <-----------response----------- server 

After sending the response to client... server will  not remember any info related to client...

Server will not have any memory to remember the clients.... Session based authentication will not work in Rest API.... 


Java Restful Services with HTTP Basic Authentication
====================================================
In the context of a HTTP transaction, basic access authentication is a method for an HTTP user agent to provide a user name and password when making a request.

HTTP Basic authentication implementation is the simplest technique for enforcing access controls to web resources because it doesn't require cookies, session identifier and login pages. 
Rather, HTTP Basic authentication uses static, standard HTTP headers which means that no handshakes have to be done in anticipation.

In this approach, client will send uname & pwd for every request in header 

							uname & pwd 
Client ----------------------------------------------------------> Server 

							uname & pwd 
Client ----------------------------------------------------------> Server 


							uname & pwd 
Client ----------------------------------------------------------> Server 


							uname & pwd 
Client ----------------------------------------------------------> Server 


When the user agent wants to send the server authentication credentials it may use the Authorization header. The Authorization header is constructed as follows:

1) Username and password are combined into a string "username:password"
2) The resulting string is then encoded using Base64 encoding
3) The authorization method and a space i.e. "Basic " is then put before the encoded string.

For example, if the user agent uses 'admin' as the username and 'admin123' as the password then the header is formed as follows:


Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==

(Key)				(value)


Note : Resource developer will provide some predefined credentials to Clients... Clients should send that username and pwd to server in every request.

Server side resource will read uname and pwd from header and do will do authentication... 

Every resource method has to do authentication... as it is difficult to maintain, we can use Filters to perform pre-processing logic like below 

-------------------------------------SecurityFilter.java--------------------------------------

@Provider
public class SecurityFilter implements ContainerRequestFilter {

	public void filter(ContainerRequestContext requestContext) throws IOException {
		System.out.println("SecurityFilter : Executed");
		
		//Reading Header String
		String auth = requestContext.getHeaderString("Authorization");
		
		
		//Checking header availability
		if (auth != null && !auth.equals("")) {
		
			//Replacing Basic first occurance with empty 
			String authInfo = auth.replaceFirst("Basic", "");
			
			
			//Decoding uname and pwd using Base64
			byte[] decodedStr = Base64.getDecoder().decode(authInfo);
		
			String unameAndPwd = new String(decodedStr, "UTF-8");
			
			
			//Tokenization
			StringTokenizer tokenizer = new StringTokenizer(unameAndPwd,":");
			
			
			//Grabbing uname and pwd
			String uname = tokenizer.nextToken();
			String pwd = tokenizer.nextToken();
			
			
			 //validating uname and pwd 
			if (uname.equals("admin") && pwd.equals("admin123")) {
				return;
			}
		}
		
		//if credentials invalid, Constructing Error Response to client 
		Response response = Response.status(Status.UNAUTHORIZED).entity("User Can't Access this Resource").build();
		
		
		//Stopping Request processing and returing error response to client 
		requestContext.abortWith(response);
	}
}
------------------------------------------------------PayslipGeneratorResource---------------------------------------------------------------
package com.rest.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/payslip")
public class PayslipResource {
	
	
	@GET
	public String generatePayslip(@QueryParam("eid") String empId){
		return "Payslip Getting Generated..!";
	}

}
-----------------------------AppConfig.java--------------------------------------------------------------------------------------------------------
package com.rest.config;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.rest.filter.SecurityFilter;
import com.rest.resources.DashBoardResource;

@ApplicationPath(value = "/rest/*")
public class AppConfig extends Application {

	@Override
	public Set<Object> getSingletons() {
		Set<Object> objs = new HashSet<Object>();
		objs.add(new PayslipResource());
		return objs;

	}

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		classes.add(SecurityFilter.class);
		return classes;
	}

}
--------------------------------------------Resource Client to send Authorization header with uname and pwd ---------------------------------------------
package com.rest.resource.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

public class DashboardResourceClient {

	static String uname = "admin1";
	static String pwd = "admin123";
	static String usernameAndPassword = uname + ":" + pwd;
	
	static String headerValue = "Basic"
			+ java.util.Base64.getEncoder().encodeToString(usernameAndPassword.getBytes());

	private static final String RESOURCE_URI = "http://localhost:6060/RestApp-BasicAuth/rest/dashboard";

	public static void main(String[] args) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(RESOURCE_URI);
		String response = target.request()
				.header("Authorization", headerValue)
				.get(String.class);
				
				
		if(resp.getStatus() != 200){
            System.err.println("Unable to connect to the server");
        }
		
		System.out.println("Response : " + response);
	}

}
-------------------------------------------------------------------------------------------------------------






