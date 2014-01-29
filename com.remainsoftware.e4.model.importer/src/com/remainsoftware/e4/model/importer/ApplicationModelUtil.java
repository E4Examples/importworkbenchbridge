package com.remainsoftware.e4.model.importer;

import java.io.IOException;

import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.internal.workbench.E4XMIResourceFactory;
import org.eclipse.e4.ui.internal.workbench.ModelAssembler;
import org.eclipse.e4.ui.internal.workbench.ModelServiceImpl;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.MApplicationElement;
import org.eclipse.e4.ui.model.application.impl.ApplicationPackageImpl;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.fragment.MModelFragment;
import org.eclipse.e4.ui.model.fragment.MModelFragments;
import org.eclipse.e4.ui.services.internal.events.EventBroker;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.ui.PlatformUI;

public class ApplicationModelUtil {

	/**
	 * The elementId is added as a child to the referenceId
	 */
	public static final String FIRST = "first";

	/**
	 * The elementId is added to the parent of the referenceId
	 */
	public static final String SECOND = "second";

	/**
	 * The elementId is added to the parent of the parent of the referenceId
	 */
	public static final String THIRD = "third";

	/**
	 * The elementId is added to the parent of the parent of the parent of the
	 * referenceId
	 */
	public static final String FOURTH = "fourth";

	/**
	 * Loads the model that <code>platformURI</code> points to. You will receive
	 * an {@link IOException} if there are problems finding the URI.
	 * 
	 * @param platformURI
	 * @return MApplication the loaded application
	 */
	public static MApplication loadModel(String platformURI) {
		ResourceSet resourceSet = new ResourceSetImpl();
		ApplicationPackageImpl.init();
		URI uri = URI.createURI(platformURI);
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put("e4xmi", new E4XMIResourceFactory());
		Resource res = resourceSet.getResource(uri, true);
		return (MApplication) res.getContents().get(0);
	}

	/**
	 * Loads the fragment that <code>platformURI</code> points to. You will
	 * receive an {@link IOException} if there are problems finding the URI.
	 * 
	 * @param platformURI
	 * @return MModelFragment the loaded fragment
	 */
	public static MModelFragments loadFragment(String platformURI) {
		ResourceSet resourceSet = new ResourceSetImpl();
		ApplicationPackageImpl.init();
		URI uri = URI.createURI(platformURI);
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put("e4xmi", new E4XMIResourceFactory());
		Resource res = resourceSet.getResource(uri, true);
		return (MModelFragments) res.getContents().get(0);
	}

	/**
	 * Merge the element <code>elementId</code> from an application model
	 * pointed to by <code>platformURI</code> into the current active
	 * application model either as {@value #FIRST} of element
	 * <code>referenceId</code> or as {@value #SECOND} indicated by
	 * <code>relationship</code>. .
	 * 
	 * @param platformURI
	 *            the location of the model that contains the element to be
	 *            imported in the main model in the form of
	 *            <code>platform:/plugin/plugin.id/path/to/file.e4xmi</code>
	 * @param elementId
	 *            the id of the element that must be merged into the main model
	 * @param referenceId
	 *            the reference id of the main model where the element must be
	 *            inserted
	 * @param relationship
	 *            the relationship to the reference id ({@link #FIRST},
	 *            {@link #SECOND}, {@link #THIRD} or {@link #FOURTH}
	 */
	public static void mergeModelElement(String platformURI, String elementId, String referenceId,
			String relationship) {

		// Get the main model
		IEclipseContext serviceContext = (IEclipseContext) PlatformUI.getWorkbench().getService(
				IEclipseContext.class);
		EModelService modelService = serviceContext.get(EModelService.class);
		MApplication application = serviceContext.get(MApplication.class);

		// Do only if the id is not in the main model
		if (modelService.find(elementId, application) == null) {

			MApplication app = loadModel(platformURI);
			EModelService service = createModelService();
			MUIElement muiElement = service.find(elementId, app);

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
	 * Merge the element <code>elementId</code> from a fragment model pointed to
	 * by <code>platformURI</code> into the current active application model
	 * either as {@value #FIRST} of element <code>referenceId</code> or as
	 * {@value #SECOND} indicated by <code>relationship</code>.
	 * 
	 * @param platformURI
	 *            the location of the model fragment that contains the element
	 *            to be imported in the main model in the form of
	 *            <code>platform:/plugin/plugin.id/path/to/file.e4xmi</code>
	 * @param elementId
	 *            the id of the element that must be merged into the main model
	 * @param referenceId
	 *            the reference id of the main model where the element must be
	 *            inserted
	 * @param relationship
	 *            the relationship to the reference id ({@link #FIRST},
	 *            {@link #SECOND}, {@link #THIRD} or {@link #FOURTH}
	 */
	public static void mergeFragmentElement(String platformURI, String elementId,
			String referenceId, String relationship) {

		// Get the main model
		IEclipseContext serviceContext = (IEclipseContext) PlatformUI.getWorkbench().getService(
				IEclipseContext.class);
		EModelService modelService = serviceContext.get(EModelService.class);
		MApplication application = serviceContext.get(MApplication.class);

		// Do only if the id is not in the main model
		if (modelService.find(elementId, application) == null) {
			MUIElement muiElement = null;
			MModelFragments app = loadFragment(platformURI);
			for (MModelFragment mfs : app.getFragments()) {
				for (MApplicationElement mae : mfs.getElements()) {
					if (mae.getElementId().equals(elementId)) {
						muiElement = (MUIElement) mae;
						break;
					}
				}
			}

			MUIElement parentElement = modelService.find(referenceId, application);
			if (relationship.equals(SECOND))
				parentElement = parentElement.getParent();
			else if (relationship.equals(THIRD))
				parentElement = parentElement.getParent().getParent();
			else if (relationship.equals(FOURTH))
				parentElement = parentElement.getParent().getParent().getParent();

			/*
			 * The addition below is just a quick hack and probably needs more
			 * work. The construct below is enough add a part to a part stack.
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
		return new ModelServiceImpl(context);
	}

	/**
	 * @param uri
	 */
	public static void mergeFragment(String platformURI) {
		IEclipseContext serviceContext = (IEclipseContext) PlatformUI.getWorkbench().getService(
				IEclipseContext.class);
		MApplication application = serviceContext.get(MApplication.class);
		MModelFragments app = loadFragment(platformURI);
		for (MModelFragment mfs : app.getFragments()) {
			/*
			 * StringModelFragmentImpl impl = (StringModelFragmentImpl) mfs;
			 * 
			 * for (MApplicationElement element : mfs.getElements()) { String ff
			 * = impl.getParentElementId(); mergeFragmentElement(platformURI,
			 * element.getElementId(), ff, FIRST); }
			 */
			mfs.merge(application);
		}
	}

	public static void assembleModel() {
		// Get the main model
		IEclipseContext serviceContext = (IEclipseContext) PlatformUI.getWorkbench().getService(
				IEclipseContext.class);
		EModelService modelService = serviceContext.get(EModelService.class);
		MApplication application = serviceContext.get(MApplication.class);
		ModelAssembler modelAssembler = ContextInjectionFactory.make(ModelAssembler.class,
				serviceContext);
		modelAssembler.processModel(0);

	}
}