package com.remainsoftware.e4.model.importer;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class Activator implements BundleActivator {

	
	private static BundleContext context;

	@Override
	public void start(final BundleContext context) throws Exception {
		Activator.context = context;
	}

	public static <T> T getService(Class<T> clazz) {
		ServiceReference<T> serviceReference = context.getServiceReference(clazz);
		return context.getService(serviceReference);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}
	
	public static BundleContext getContext() {
		return context;
	}

}
