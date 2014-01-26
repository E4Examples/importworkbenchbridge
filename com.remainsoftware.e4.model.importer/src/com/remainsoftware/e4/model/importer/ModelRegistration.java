package com.remainsoftware.e4.model.importer;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.ui.IStartup;

public class ModelRegistration implements IStartup {

	@Override
	public void earlyStartup() {
		loadWimsImport();
		ApplicationModelUtil.assembleModel();
		// loadFragments();
		// loadProcessors();
	}

	private void loadWimsImport() {
		IExtensionRegistry registry = Activator.getService(IExtensionRegistry.class);
		IConfigurationElement[] elements = registry.getConfigurationElementsFor(Activator
				.getContext().getBundle().getSymbolicName()
				+ ".modelimport");
		for (IConfigurationElement element : elements) {
			if (element.getName().equals("model")) {
				String uri = element.getAttribute("modelURI");
				if (!uri.startsWith("platform")) {
					uri = "platform:/plugin/" + element.getContributor().getName() + "/" + uri;
				}
				String elementId = element.getAttribute("elementId");
				String referenceId = element.getAttribute("referenceId");
				String relationship = element.getAttribute("relationship");
				boolean fragment = element.getAttribute("fragment").equals("true");
				if (fragment)
					ApplicationModelUtil.mergeFragmentElement(uri, elementId, referenceId,
							relationship);
				else
					ApplicationModelUtil.mergeModelElement(uri, elementId, referenceId,
							relationship);
			}
		}
	}

	private void loadFragments() {
		IExtensionRegistry registry = Activator.getService(IExtensionRegistry.class);
		IConfigurationElement[] elements = registry
				.getConfigurationElementsFor("org.eclipse.e4.workbench.model");
		for (IConfigurationElement element : elements) {
			if (element.getName().equals("fragment")) {
				String uri = element.getAttribute("uri");
				if (!uri.startsWith("platform")) {
					uri = "platform:/plugin/" + element.getContributor().getName() + "/" + uri;
				}
				ApplicationModelUtil.mergeFragment(uri);
			}
		}
	}

	private void loadProcessors() {
	}
}
