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
