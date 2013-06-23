Getting Started: Consuming XML from a REST Service on Android
=============================================================

What you'll build
-----------------

This Getting Started guide will walk you through the process of consuming XML from a REST service using Spring for Android's `RestTemplate`.

What you'll need
----------------

 - About 15 minutes
 - {!include#prereq-editor-android-buildtools}

## {!include#how-to-complete-this-guide}

<a name="scratch"></a>
## {!include#android-dev-env}


Set up the project
------------------

{!include#android-build-system-intro}

{!include#create-directory-structure-org-hello}

### Create a Maven POM

    {!include:complete/pom.xml}

{!include#create-android-manifest}

    {!include:complete/AndroidManifest.xml}

### Create a String Resource

    {!include:complete/res/values/strings.xml}

### Create a Layout

    {!include:complete/res/layout/hello_layout.xml}

<a name="initial"></a>
Create a representation class
-----------------------------

With the Android project configured, it is time to create our REST request. Before we can do that though, we need to consider the data we are wanting to consume.

### XML Data

When we query the service at the /hello-world endpoint, we will receive an XML response. This response represents a greeting and will resemble the following:

```xml
<greeting>
    <id>1</id>
    <content>"Hello, World!"</content>
</greeting>
```

The `id` field is a unique identifier for the greeting, and `content` is the textual representation of the greeting.

### Greeting

To model the greeting representation, we will create a representation class:

    {!include:complete/src/main/java/org/hello/Greeting.java}


Invoke a REST service with RestTemplate
---------------------------------------

Spring provides a convenient template class called `RestTemplate`. `RestTemplate` makes interacting with most RESTful services a simple process. In the example below, we establish a few variables and then make a request of our simple REST service. As mentioned earlier, we will use the [Simple XML] library to marshal the XML response data into our representation classes.

    {!include:complete/src/main/java/org/hello/HelloActivity.java}

Thus far, we've only used the HTTP verb `GET` to make calls, but we could just as easily have used `POST`, `PUT`, etc.


## Start the REST service

The code is now complete, so we can run the application to see the results. In order to consume a REST service, you must first have a REST service in which to consume. This project includes a simple self contained application for use with testing our REST request. You can start the server by running the following shell script from the `service` folder:

```sh
$ ./start-service.sh
```


## {!include#start-android-virtual-device}


## {!include#build-and-run-android}


Summary
-------

Congratulations! You have just developed a simple REST client using Spring.

There's more to building and working with REST APIs than is covered here.

[zip]: https://github.com/springframework-meta/gs-consuming-rest-xml-android/archive/master.zip
