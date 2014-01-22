Import Workbench Bridge
=======================

Provides an extension point that enables you to import parts of an application model into another application model (for example the model of the IDE)

This example exports a part from an Application.e4xmi into the parent _left_ of the current model (which in case of the IDE is the left partstack of the IDE JDT Perspective.)

      <extension
          point="com.remainsoftware.fde.workbench.core.modelimport">
             <model
                modelId="com.remainsoftware.fde.application.part.0"
                modelURI="platform:/plugin/com.remainsoftware.fde.application/Application.e4xmi"
                reparentId="left">
            </model>
      </extension>   

