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

How to test
===========
Download Luna M4, import these three projects and create a run configuration with -console and run all bundles. Open the Java perspective. Then go to the console, find the _importer_ bundle and start it. The pure e4 view will be extracted from the model and loaded in the IDE.  



The Projects
============
This repo contains three projects. 

### pure.e4.app
Contains a pure e4 app and the model from which you want to extract a part and host it in the IDE.

### bridge
Only contains the extension point to indicate which part must be hosted in the IDE

### importer
Parses the extension point en adds the part from the pure e4 application to the IDE. Please note that this bundle must be started.


How it works
============
1. The importer bundle reads the extension point and loads the e4xmi file into a secondary application model. 
2. It creates a temporary EModelService to query this tempory application model for the element with the _modelId_. 
3. Then it finds from the main model the element with the _reparentId_.
4. The imported element (and all its children) are added to the _preparentId_ element from the main model.

