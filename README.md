Import Workbench Bridge
=======================

Provides an extension point that enables you to import parts of an application model into another application model (for example the model of the IDE)

This example exports a part from an Application.e4xmi into the parent _left_ of the current model (which in case of the IDE is the left partstack of the IDE JDT Perspective.)

      <extension
          point="com.remainsoftware.fde.workbench.core.modelimport">
             <model
                modelURI="platform:/plugin/com.remainsoftware.fde.application/Application.e4xmi"
                modelId="com.remainsoftware.fde.application.part.0"
                reparentId="left">
            </model>
      </extension>   


How to Test
===========
This repo contains three projects. 

### pure.e4.app
Contains a pure e4 app from which you want to extract a part and host it in the IDE.

### bridge
Only contains the extension point.

### importer
Parses the extension point en adds the part from the pure e4 application to the IDE. Please note that this bundle must be started.

