package com.remainsoftware.e4processor;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.descriptor.basic.MPartDescriptor;
import org.eclipse.e4.ui.model.application.descriptor.basic.impl.BasicFactoryImpl;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.internal.ModelUtils;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

public class E4OnlyProcessor {

	@Inject
	private MApplication app;

	@Execute
	public void execute(@Optional final EPartService partservice, UISynchronize sync) {

		final MPart part5 = (MPart) ModelUtils.findElementById(app, "samplepart5");
		if (part5 != null)
			return;

		final MPart part = (MPart) ModelUtils.findElementById(app, "samplepart2");

		if (part != null) {
			sync.asyncExec(new Runnable() {
				@Override
				public void run() {
					// partservice.activate(part);
					BasicFactoryImpl factory = BasicFactoryImpl.init();
					MPartDescriptor pd = factory.createPartDescriptor();
					pd.setElementId("samplepart5");
					pd.setContributionURI(part.getContributionURI().replaceFirst("SamplePart2",
							"SamplePart5"));
					pd.setAllowMultiple(true);
					pd.setContributorURI("com.remainsoftware.e4fragment");
					app.getDescriptors().add(pd);
					final MPart part2 = partservice.createPart("samplepart5");
					part2.setLabel("E4 Only Processor");
					MPartStack partstack = (MPartStack) ModelUtils.findElementById(app,
							"partstack2");
					partstack.getChildren().add(part2);
					partservice.activate(part2);
				}
			});
		}
	}
}