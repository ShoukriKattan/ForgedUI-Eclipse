# ForgedUI-Eclipse
ForgedUI is a Mobile  Drag and Drop editor for Appcelerator Titanium.
What the editor allows one to do; similar to Google Android Studio and to Apple's XCode is that it allows you to build your UI for your Appcelerator mobile apps by dragging and dropping various UI elements. Once the drag and drop is completed for various platforms, you can click a code generate button which takes care of generating Titanium JS code for this. 

* For a preview of what editor looked like you can view this YouTube video https://www.youtube.com/watch?v=FvzF-6dxuMs 
* Please note that this project has been implemented in 2010-2011 where Titanium was JS only and there was no XML /descriptive way to implement UI (currently called Alloy) 
* Please also note that the team discountinued work on this project, it is no longer maintained, and the reason the team decided to put this out there is that we think people would benefit from taking this and extending it. 
* If you need help getting started and wish to fork this project, feel free to do so, and feel free to contact if you need help.


##How the editor works :
This editor is based on Eclipse GEF (https://eclipse.org/gef/). In a nutshell GEF is an MVC framework for building various drag and drop editors.

This editor works as follows : 
- Expose the core Titanium UI model as a GEF model.
- Provide GEF views for each model element 
- Provide GEF controllers that dictate rules such as resizing, drag and drop behaviour, colors, etc.. 
- There is a Core Model, that is used by the GEF model.
- When someone builds a UI effectively they are generating a UI Tree. The code generation part parses this tree and generates the necessary code to build the UI from the tree by instantiating elements and attaching them to the Windows, views, etc. 
- The editor files are serialized to XML files that the editor uses to persist the tree and its properties. 


##The Eclipse projects 

The main editor feature is found in com.forgedui.features.editor

This feature requires the following plugins (Eclipse or various projects): 

###Third Party: 
- org.eclipse.core.runtime version=3.2.0 
- org.eclipse.gef version=3.5.0 
- org.eclipse.ui.ide version=3.2.0 
- org.eclipse.jface version=3.2.0 
- org.eclipse.core.resources version=3.2.0 
- org.eclipse.ui version=3.6.0 
- org.eclipse.ui.views version=3.2.0 
- org.eclipse.ui.workbench version=3.2.0 


###Project Plugins
- com.forgedui.core : This project contains the base model that code gen part uses to build JS files. 
- com.forgedui.editor: Eclipse GEF MVC project / Eclipse project that includes Editors, Perspectives, Palettes, etc. 
- com.forgedui.common.deps : Additional 3rd party depenencies that don't have their own repositories
- com.forgedui.xstream: XML Serialization library (XStream) 



