package com.remainsoftware.e4.model.importer;

import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.internal.workbench.E4XMIResourceFactory;
import org.eclipse.e4.ui.internal.workbench.ModelServiceImpl;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.impl.ApplicationPackageImpl;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.services.internal.events.EventBroker;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.ui.PlatformUI;

public class ApplicationModelUtil {

	private static ResourceSet resourceSet = new ResourceSetImpl();

	public static MApplication loadModel(String platformURI) {
		ApplicationPackageImpl.init();
		URI uri = URI.createURI(platformURI);
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put("e4xmi", new E4XMIResourceFactory());
		Resource res = resourceSet.getResource(uri, true);
		return (MApplication) res.getContents().get(0);
	}

	public static void mergeModel(String uri, String id, String reParent, String relationship) {

		MApplication app = loadModel(uri);
		IEclipseContext context = EclipseContextFactory.create();
		context.modify(IEventBroker.class, new EventBroker());
		context.modify(IExtensionRegistry.class, Activator.getService(IExtensionRegistry.class));
		EModelService service = new ModelServiceImpl(context);
		MUIElement muiElement = service.find(reParent, app);
		IEclipseContext serviceContext = (IEclipseContext) PlatformUI.getWorkbench().getService(
				IEclipseContext.class);
		EModelService modelService = serviceContext.get(EModelService.class);
		MApplication application = serviceContext.get(MApplication.class);
		MUIElement parentElement = modelService.find(reParent, application);
		if (relationship.equals("sibling"))
			parentElement = parentElement.getParent();

		/*
		 * The addition below is just a quick hack and needs more work. For now
		 * it is enough to, for example, add a part to a partstack
		 */
		if (parentElement instanceof MElementContainer) {
			MElementContainer cont = (MElementContainer) parentElement;
			cont.getChildren().add(muiElement);
		}
	}
}