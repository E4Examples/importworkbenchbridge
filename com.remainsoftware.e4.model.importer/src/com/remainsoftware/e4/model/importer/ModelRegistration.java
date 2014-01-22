package com.remainsoftware.e4.model.importer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.ui.IStartup;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

public class ModelRegistration implements IStartup {
	ExecutorService threadPool = Executors.newFixedThreadPool(10);
	
	@Override
	public void earlyStartup() {
		threadPool.execute(new Runnable() {

			@Override
			public void run() {

				IExtensionRegistry registry = Activator.getService(IExtensionRegistry.class);

				ServiceReference<?>[] allServiceReferences;
				try {
					allServiceReferences = Activator.getContext().getAllServiceReferences(null, null);
					for (ServiceReference<?> serviceReference : allServiceReferences) {

						String[] classes = ((String[]) serviceReference.getProperty("objectClass"));
						for (String string : classes) {
							if (string.contains("e4"))
								System.out.println(string);
						}
					}
				} catch (InvalidSyntaxException e) {
				}

				IConfigurationElement[] elements = registry.getConfigurationElementsFor(Activator.getContext()
						.getBundle().getSymbolicName() + ".modelimport");
				for (IConfigurationElement element : elements) {
					String uri = element.getAttribute("modelURI");
					String id = element.getAttribute("modelId");
					String reParent = element.getAttribute("reparentId");
					MUIElement muiElement = ApplicationModelUtil.loadModel(uri, id, registry);
					IWorkbench model = Activator.getService(IWorkbench.class);
					EModelService modelService = model.getApplication().getContext()
							.get(EModelService.class);
					MUIElement parentElement = modelService.find(reParent, model.getApplication());

					// this is just a hack (try adding a part to a partstack)
					if (parentElement instanceof MElementContainer) {
						MElementContainer cont = (MElementContainer) parentElement;
						cont.getChildren().add(muiElement);
					}
				}
			}
		});
	}

}
