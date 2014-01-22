package com.remainsoftware.e4.model.importer;

import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.internal.workbench.E4XMIResourceFactory;
import org.eclipse.e4.ui.internal.workbench.ModelServiceImpl;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.impl.ApplicationPackageImpl;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.services.internal.events.EventBroker;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

public class ApplicationModelUtil {

	private static ResourceSet resourceSet = new ResourceSetImpl();

	public static MUIElement loadModel(String platformURI, String findId,
			IExtensionRegistry registry) {
		ApplicationPackageImpl.init();

		URI uri = URI.createURI(platformURI);

		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put("e4xmi", new E4XMIResourceFactory());

		Resource res = resourceSet.getResource(uri, true);
		MApplication app = (MApplication) res.getContents().get(0);
		IEclipseContext context = EclipseContextFactory.create();
		context.modify(IEventBroker.class, new EventBroker());
		context.modify(IExtensionRegistry.class, registry);
		EModelService service = new ModelServiceImpl(context);
		return service.find(findId, app);
	}
}