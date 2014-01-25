package com.remainsoftware.e4.model.importer;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.ui.IStartup;

public class ModelRegistration implements IStartup {

	@Override
	public void earlyStartup() {
		IExtensionRegistry registry = Activator.getService(IExtensionRegistry.class);
		IConfigurationElement[] elements = registry.getConfigurationElementsFor(Activator
				.getContext().getBundle().getSymbolicName()
				+ ".modelimport");
		for (IConfigurationElement element : elements) {
			String uri = element.getAttribute("modelURI");
			String elementId = element.getAttribute("elementId");
			String referenceId = element.getAttribute("referenceId");
			String relationship = element.getAttribute("relationship");
			ApplicationModelUtil.mergeModel(uri, elementId, referenceId, relationship);
		}
	}
}
