# AEM Image Service

This is project built from mvn archetype version 23. This project is intended to demonstrate the usage of OSGI config and services to render digital assets on the page component in a responsive grid. Below are the steps to build this project on local machine to use. Since this is generated from aem project archetype, it creates the complete boiler plate for the AEM project. 


## System Requirements.

Below are the system requirements needed to validate the code for this exercise.

* Install AEM 6.5.0. AEM 6.5.0 comes with We-Retail code and content.
* Core Components need for this project are provided by the archetype used in the mvn command to generate this project.
* Java 1.8
* Once the AEM is installed, create a service user named: **image-service-user** and grant read access to the repo. Make sure to grand read acceess to the /content path. (For this test, I gave complete read access to the repo for this service user).


## How to build

Get the latest code from the git repo to a local directory.

    cd ~/Downloads
    mkdir code-test
    cd code-test
    git clone https://github.com/mukrpe/aem-image-service.git
 
To build all the modules run in the project root directory the following command with Maven 3:

    mvn clean install

If you have a running AEM instance you can build and package the whole project and deploy into AEM with

    mvn clean install -PautoInstallSinglePackage
    
PS: sometimes observed that the core bundle isn't accessible immediately. Either give sometime to become active(from log messages) or do a core bundle deployment using below from core directory:

    mvn clean install -PautoInstallBundle
    
    
## Tasks completed as part this exercise.

*aem-image* project will contain the below as part this exercise. 

* ImageServiceImpl - OSGI service that checks for the OSGI configuration about the assets to be returned to the pages. 
    * ImageServiceImpl gets enabled based on the *isEnabled* in the AEM Image Service osgi configuration.
    * An additional field, Asset Count, in the same osgi confi is provided to limit the asset count returned to the front-end to avoid passing root DAM path.
    * An additional method to provide the JSON representation of the string is provided to be used by servlets in future. (if the request is not from the Sightly pages/components)     

* ImageServiceUtil - Utility class to query and process the requests from the service layer.

* Page Template and Components
    * As instructed, the page component from *aem-image* project inherits from We-Retail's page component.
    * The clientlibs are inherited from the We-Retail Project.
    * Added customization to Asset List component in aem-image project to invoke the OSGI service to render the assets in responsive grid. Added OSGI to process the request as per the given task. But it would be ideal for writing a model class to attach to a component.
    * List component is added to the template so when the user is validating this exercise, creating the **Content Page** will load the images in the responsive grid.   

## Maven command used to generate this project

Below is the mvn command used to generate this project:

    mvn -B archetype:generate \
	 -D archetypeGroupId=com.adobe.granite.archetypes \
	 -D archetypeArtifactId=aem-project-archetype \
	 -D archetypeVersion=23 \
	 -D aemVersion=6.5.0 \
	 -D appTitle="AEM Image" \
	 -D appId="aem-image" \
	 -D groupId="com.aem.image" \
	 -D frontendModule=general

	 