Getting Started: Consuming XML from a REST Service with Spring for Android
==========================================================================

What you'll build
-----------------

This Getting Started guide walks you through the process of building an application that uses Spring for Android's `RestTemplate` to consume XML from a REST service.

What you'll need
----------------

 - About 15 minutes
 - {!include#prereq-editor-android-buildtools}

## {!include#how-to-complete-this-guide}

<a name="scratch"></a>
Set up the project
------------------

{!include#android-build-system-intro}

{!include#create-directory-structure-org-hello}

### Create a Maven POM

    {!include:complete/pom.xml}

{!include#create-android-manifest}

    {!include:complete/AndroidManifest.xml}

### Create a string resource
Add a text string. Text strings can be referenced from the application or from other resource files.

    {!include:complete/res/values/strings.xml}

### Create a layout
Here you define the visual structure for the user interface of your application.

    {!include:complete/res/layout/hello_layout.xml}

<a name="initial"></a>
Fetch a REST resource
-----------------------------

Before you create a REST request, consider the data that you want your application to consume.

For example, when you query the service at the /hello-world endpoint, you receive an XML response. This response represents a greeting and resembles the following:

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

    {!include:complete/src/main/java/org/hello/Greeting.java}


Invoke a REST service with the RestTemplate
-------------------------------------------

Spring provides a convenient template class called `RestTemplate`. `RestTemplate` makes interacting with most RESTful services a simple process. In the example below, you establish a few variables and then make a request of the simple REST service. As mentioned earlier, you use the [Simple XML] library to marshal the XML response data into your representation classes.

    {!include:complete/src/main/java/org/hello/HelloActivity.java}

Thus far, you've only used the HTTP verb `GET` to make calls, but you could just as easily have used `POST`, `PUT`, and so on.


## Start the REST service

In order to consume a REST service, you must first have a REST service to consume. This project includes a simple self-contained application for use with testing the REST request. You can start the server by running the following shell script from the `service` folder:

```sh
$ ./start-service.sh
```


## {!include#build-and-run-android}


Summary
-------

Congratulations! You have just developed a simple REST client using Spring.


[zip]: https://github.com/springframework-meta/gs-consuming-rest-xml-android/archive/master.zip
