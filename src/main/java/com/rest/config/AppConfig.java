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
