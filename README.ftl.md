<#assign project_id="gs-consuming-rest-xml-android">

What you'll build
-----------------

This Getting Started guide walks you through the process of building an application that uses Spring for Android's `RestTemplate` to consume XML from a REST service.


What you'll need
----------------

 - About 15 minutes
 - <@prereq_editor_android_buildtools/>

## <@how_to_complete_this_guide jump_ahead='Fetch a REST resource'/>


<a name="scratch"></a>
Set up the project
------------------

<@android_build_system_intro/>

<@create_directory_structure_org_hello/>

### Create a Maven POM

    <@snippet path="pom.xml" prefix="initial"/>
    
<@create_android_manifest/>

    <@snippet path="AndroidManifest.xml" prefix="initial"/>

### Create a string resource
Add a text string. Text strings can be referenced from the application or from other resource files.

    <@snippet path="res/values/strings.xml" prefix="initial"/>

### Create a layout
Here you define the visual structure for the user interface of your application.

    <@snippet path="res/layout/hello_layout.xml" prefix="initial"/>


<a name="initial"></a>
Fetch a REST resource
---------------------

Before you create a REST request, consider the data that you want your application to consume.

This project includes a simple, self-contained application for use with testing the REST request.
When you query this service at the /hello-world endpoint, you receive an XML response. This response represents a greeting and resembles the following:

```xml
<greeting>
    <id>1</id>
    <content>"Hello, World!"</content>
</greeting>
```

The `id` field is a unique identifier for the greeting, and `content` is the textual representation of the greeting.


Create a representation class
-----------------------------
To model the greeting representation, you create a representation class:

    <@snippet path="src/main/java/org/hello/Greeting.java" prefix="complete"/>


Invoke a REST service with the RestTemplate
-------------------------------------------

Spring provides a convenient template class called `RestTemplate`. `RestTemplate` makes interacting with most RESTful services a simple process. In the example below, you establish a few variables and then make a request of the simple REST service. You use the Simple XML library to marshal the XML response data into your representation classes.

    <@snippet path="src/main/java/org/hello/HelloActivity.java" prefix="complete"/>

Thus far, you've only used the HTTP verb `GET` to make calls, but you could just as easily have used `POST`, `PUT`, and so on.


Start the REST service
----------------------

In order to consume a REST service, you must first have a REST service to consume. You can start the server, included in this guide, by running the following shell script from the `service` folder:

```sh
$ ./start-service.sh
```


## <@build_and_run_android/>


Summary
-------

Congratulations! You have just developed a simple REST client using Spring.
