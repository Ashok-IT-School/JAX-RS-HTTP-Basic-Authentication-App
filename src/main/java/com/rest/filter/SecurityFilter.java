package com.rest.filter;

import java.io.IOException;
import java.util.Base64;
import java.util.StringTokenizer;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

@Provider
public class SecurityFilter implements ContainerRequestFilter {

	public void filter(ContainerRequestContext requestContext) throws IOException {
		System.out.println("SecurityFilter : Executed");
		
		String auth = requestContext.getHeaderString("Authorization");
		
		if (auth != null && !auth.equals("")) {
			String authInfo = auth.replaceFirst("Basic", "");
			byte[] decodedStr = Base64.getDecoder().decode(authInfo);

			String unameAndPwd = new String(decodedStr, "UTF-8");
			System.out.println("Decoded Str : " + unameAndPwd);
			
			StringTokenizer tokenizer = new StringTokenizer(unameAndPwd,":");
			
			String uname = tokenizer.nextToken();
			String pwd = tokenizer.nextToken();
			
			if (uname.equals("admin") && pwd.equals("admin123")) {
				return;
			}
		}
		Response response = Response.status(Status.UNAUTHORIZED).entity("User Can't Access this Resource").build();
		requestContext.abortWith(response);
	}
}
