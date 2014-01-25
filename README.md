Import Workbench Bridge
=======================

Provides an extension point that enables you to import parts of an application model into another application model (for example the model of the IDE)


How to test
===========
Clone this repo:

git clone https://github.com/E4Examples/importworkbenchbridge.git


Download Luna (did not try it with Kepler but it might work), import the three projects and open the manifest of the e3app. Press the run button to run the application. What you will see is that one of the views is imported from the model of the e4app which is pure E4.


The Projects
============
This repo contains four projects. 

### importer
Provides an extension point with a truly simple mechanism to load E4 code into the E3 based RCP app (IDE or what not). It parses the extension point en adds elements from pure e4 applications into an e3app. You don't need to include or depend on the importer bundle in order to use its extension point.

### e4app
Contains a pure e4 app and the model from which you want to extract a part and host it in the IDE or any other E3 based RCP application.

### e4fragment
Contains a pure e4 fragment model from which you want to extract a part and host it in the IDE or any other E3 based RCP application.

### e3app
A very simple e3 project (with a view) generated from the template. It uses the extension point in _importer_ to refernce a view from the _e4app_ and _e4fragment_ bundles.



How it works
============
1. The importer bundle reads the extension point and loads the e4xmi file into a secondary application model. 
2. It creates a temporary EModelService to query this tempory application model for the element with the _modelId_. 
3. Then it finds from the main model the element with the _targetId_.
4. The imported element (and all its children) are added to the _targetId_ element (or its parent) from the main model.

### Example E3 RCP Apllication
This example exports a part from an Application.e4xmi into the parent of _samplepart_ of the current model.

     <extension
         point="com.remainsoftware.e4.model.importer.modelimport">
      <model
            modelURI="platform:/plugin/com.remainsoftware.e4app/Application.e4xmi"
            elementId="samplepart"
            referenceId="com.remainsoftware.e3app.view">
            relationship="second"
     </model>
    </extension>

### Example IDE
This example places a part from the e4app Application.e4xmi into the model element _left_ which is a partstack of the Java perspective.

     <extension
         point="com.remainsoftware.e4.model.importer.modelimport">
      <model
            modelURI="platform:/plugin/com.remainsoftware.e4app/Application.e4xmi"
            elementId="samplepart"
            referenceId="left">
            relationship="first"
     </model>
    </extension>
