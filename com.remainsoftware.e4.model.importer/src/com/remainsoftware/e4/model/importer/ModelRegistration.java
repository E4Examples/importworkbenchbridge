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
			if (!uri.startsWith("platform")) {
				uri = "platform:/plugin/" + element.getContributor().getName() + "/" + uri;
			}
			String elementId = element.getAttribute("elementId");
			String referenceId = element.getAttribute("referenceId");
			String relationship = element.getAttribute("relationship");
			boolean fragment = element.getAttribute("fragment").equals("true");
			if (fragment)
				ApplicationModelUtil
						.mergeFragmentElement(uri, elementId, referenceId, relationship);
			else
				ApplicationModelUtil.mergeModelElement(uri, elementId, referenceId, relationship);
		}
	}
}
