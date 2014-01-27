package com.remainsoftware.e4processor;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.internal.ModelUtils;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

public class AddPartStackProcessor {

	@Inject
	private MApplication app;

	@Execute
	public void execute(@Optional final EPartService partservice, UISynchronize sync) {

		System.err.println("partstack2");

		final MUIElement part2 = (MUIElement) ModelUtils.findElementById(app,
				"com.remainsoftware.e3app.view");

		// Create a partstack in the IDE window.
		MPartStack partstack = (MPartStack) ModelUtils.findElementById(app, "partstack2");
		if (partstack == null) {
			partstack = org.eclipse.e4.ui.model.application.ui.basic.impl.BasicFactoryImpl.init()
					.createPartStack();
			partstack.setElementId("partstack2");
			partstack.setContributorURI("x.y.z");
			partstack.setToBeRendered(true);
			partstack.setVisible(true);
			part2.getParent().getParent().getChildren().add(partstack);
		}
	}
}