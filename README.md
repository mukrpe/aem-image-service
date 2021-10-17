# AEM Image Service

This is project built from mvn archetype version 25. This project is intended to demonstrate the usage of OSGI config and services to render Asset information on the page component. Below are the steps to build this project on local machine to use. Since this is generated from aem project archetype, it creates 

## How to build

Get the latest code from the git repo:

    git clone <repo url> <TO-DO>
 
To build all the modules run in the project root directory the following command with Maven 3:

    mvn clean install

If you have a running AEM instance you can build and package the whole project and deploy into AEM with

    mvn clean install -PautoInstallPackage

## Testing

There are three levels of testing contained in the project:

* unit test in core: this show-cases classic unit testing of the code contained in the bundle. To test, execute:

    mvn clean test

* server-side integration tests: this allows to run unit-like tests in the AEM-environment, ie on the AEM server. To test, execute:

    mvn clean verify -PintegrationTests

* client-side Hobbes.js tests: JavaScript-based browser-side tests that verify browser-side behavior. To test:

    in the browser, open the page in 'Developer mode', open the left panel and switch to the 'Tests' tab and find the generated 'MyName Tests' and run them.


## Maven settings

The project comes with the auto-public repository configured. To setup the repository in your Maven settings, refer to:

    http://helpx.adobe.com/experience-manager/kb/SetUpTheAdobeMavenRepository.html

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
