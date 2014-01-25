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
			String id = element.getAttribute("modelId");
			String targetId = element.getAttribute("targetId");
			String relationship = element.getAttribute("relationship");
			ApplicationModelUtil.mergeModel(uri, id, targetId, relationship);
		}
	}
}
