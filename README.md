Import Workbench Bridge
=======================

Provides an extension point that enables you to import parts of an application model or fragment model into the running application model (for example the model of your E3 based RCP application or the model of the Eclipse IDE)


How to test
===========
Clone this repo:

git clone https://github.com/E4Examples/importworkbenchbridge.git


Download Luna (did not try it with Kepler but I don't see why it would not work), import the projects and open the manifest of the e3app. Press the run button to run the application. What you will see is that some of the views are imported from the model of e4app and the fragement of e4fragment which are pure E4.


The Projects
============
This repo contains five projects. 

### importer
Provides an extension point with a truly simple mechanism to load E4 code into the E3 based RCP app (IDE or what not). It parses the extension point en adds elements from pure e4 applications into an e3app. You don't need to include or depend on the importer bundle in order to use its extension point.

### e4app
Contains a pure e4 app and the model from which you want to extract a part and host it in the IDE or any other E3 based RCP application.

### e4fragment
Contains a pure e4 fragment model from which you want to extract a part and host it in the IDE or any other E3 based RCP application. You can also merge the complete fragment by using the default extension point.

### e4processor
Contains a processor that modifies the model of the E3 application programmatically.

### e3app
A very simple e3 project (with a view) generated from the template. It uses the extension point in _importer_ to refernce a view from the _e4app_ and _e4fragment_ bundles.



How it works
============
1. The importer bundle reads the extension point and loads the e4xmi file into a secondary application model. 
2. It creates a temporary EModelService to query this tempory application model for the element with the _modelId_. 
3. Then it finds from the main model the element with the _referenceId_.
4. The imported element (and all its children) are added to the _referenceId_ element (or its parent) from the main model.
5. The ModelAssembler is called to process all fragments and processors

### Example Import an element of an application model into an E3 RCP Application
This example imports a part _samplepart_ from an Application.e4xmi into the parent (_"second"_) of _com.remainsoftware.e3app.view_ of the current model.

     <extension
         point="com.remainsoftware.e4.model.importer.modelimport">
      <model
            modelURI="platform:/plugin/com.remainsoftware.e4app/Application.e4xmi"
            fragment="false"
            elementId="samplepart"
            referenceId="com.remainsoftware.e3app.view">
            relationship="second"
     </model>
    </extension>
    
### Example Import an element of an application model into an E3 RCP Application
This example imports the partstack _"partstack"_ from an Application.e4xmi into the parent of the parent ("third") of _com.remainsoftware.e3app.view_ of the current model.

     <extension
         point="com.remainsoftware.e4.model.importer.modelimport">
      <model
            modelURI="platform:/plugin/com.remainsoftware.e4app/Application.e4xmi"
            fragment="false"
            elementId="partstack"
            referenceId="com.remainsoftware.e3app.view"
            relationship="third">
      </model>
    </extension>
    
    
### Example Import an element of a fragment model into an E3 RCP Application
This example imports the view _"samplepart2"_ from a _"fragment.e4xmi"_ into the parent _("second")_ of _com.remainsoftware.e3app.view_ of the current model.

     
    <extension
         point="com.remainsoftware.e4.model.importer.modelimport">
      <model
            elementId="samplepart2"
            fragment="true"
            modelURI="fragment.e4xmi"
            referenceId="com.remainsoftware.e3app.view"
            relationship="second">
      </model>
    </extension>

### Example IDE
This example places a part from the e4app Application.e4xmi into the model element _left_ which is a partstack of the Java perspective.

     <extension
         point="com.remainsoftware.e4.model.importer.modelimport">
      <model
            modelURI="platform:/plugin/com.remainsoftware.e4app/Application.e4xmi"
            fragment="false"
            elementId="samplepart"
            referenceId="left">
            relationship="first"
     </model>
    </extension>
