# ForgedUI-Eclipse
ForgedUI is a Mobile  Drag and Drop editor for Appcelerator Titanium.
What the editor allows one to do; similar to Google Android Studio and to Apple's XCode is that it allows you to build your UI for your Appcelerator mobile apps by dragging and dropping various UI elements. Once the drag and drop is completed for various platforms, you can click a code generate button which takes care of generating Titanium JS code for this. 

* For a preview of what editor looked like you can view this YouTube video https://www.youtube.com/watch?v=FvzF-6dxuMs * 
* Please note that this project has been implemented in 2010-2011 where Titanium was JS only and there was no XML /descriptive way to implement UI (currently called Alloy) * 
* Please also note that the team discountinued work on this project, it is no longer maintained, and the reason the team decided to put this out there is that we think people would benefit from taking this and extending it. * 
* If you need help getting started and wish to fork this project, feel free to do so, and feel free to contact if you need help.


##How the editor works at a high level:
This editor is based on Eclipse GEF (https://eclipse.org/gef/) In a nutshell 

##The projects are as follow : 

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
- com.forgedui.core
- - This project contains the base model. 
- com.forgedui.editor
- com.forgedui.common.deps
- com.forgedui.xstream



