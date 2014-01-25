package com.remainsoftware.e4.model.importer;

import java.io.IOException;

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

	private static final String FIRST = "first";
	private static final String SECOND = "second";
	private static final String THIRD = "third";
	private static final String FOURTH = "fourth";

	private static ResourceSet resourceSet = new ResourceSetImpl();

	/**
	 * Loads the model that <code>platformURI</code> points to. You will receive
	 * an {@link IOException} if there are problems finding the URI.
	 * 
	 * @param platformURI
	 * @return MApplication the loaded application
	 */
	public static MApplication loadModel(String platformURI) {
		ApplicationPackageImpl.init();
		URI uri = URI.createURI(platformURI);
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put("e4xmi", new E4XMIResourceFactory());
		Resource res = resourceSet.getResource(uri, true);
		return (MApplication) res.getContents().get(0);
	}

	/**
	 * Merge the element <code>id</code> from an application model pointed to by
	 * <code>uri</code> into the current active application model either as
	 * {@value #FIRST} of element <code>targetId</code> or as {@value #SECOND}
	 * indicated by <code>relationship</code>. .
	 * 
	 * @param uri
	 * @param id
	 * @param referenceId
	 * @param relationship
	 */
	public static void mergeModel(String uri, String id, String referenceId, String relationship) {

		// Get the main model
		IEclipseContext serviceContext = (IEclipseContext) PlatformUI.getWorkbench().getService(
				IEclipseContext.class);
		EModelService modelService = serviceContext.get(EModelService.class);
		MApplication application = serviceContext.get(MApplication.class);

		// Do only if the id is not in the main model
		if (modelService.find(id, application) == null) {

			MApplication app = loadModel(uri);
			EModelService service = createModelService();
			MUIElement muiElement = service.find(id, app);

			MUIElement parentElement = modelService.find(referenceId, application);
			if (relationship.equals(SECOND))
				parentElement = parentElement.getParent();
			else if (relationship.equals(THIRD))
				parentElement = parentElement.getParent().getParent();
			else if (relationship.equals(FOURTH))
				parentElement = parentElement.getParent().getParent().getParent();

			/*
			 * The addition below is just a quick hack and probably needs more
			 * work. The construct below is enough add a part to a part stack
			 * and migh.
			 */
			if (parentElement instanceof MElementContainer) {
				MElementContainer cont = (MElementContainer) parentElement;
				cont.getChildren().add(muiElement);
			}
		}
	}

	/**
	 * Creates a model service just for the purpose of using the find methods.
	 * The find methods should not be so tightly coupled to an instance of the
	 * model service.
	 * 
	 * @return a freshly created model service
	 */
	public static EModelService createModelService() {
		IEclipseContext context = EclipseContextFactory.create();
		context.modify(IEventBroker.class, new EventBroker());
		context.modify(IExtensionRegistry.class, Activator.getService(IExtensionRegistry.class));
		EModelService service = new ModelServiceImpl(context);
		return service;
	}
}