# Getting Started Consuming XML from a REST Service on Android


##Introduction

### What You will Build

This guide will take you through the process of consuming XML from a REST service on Android.

### What You will Need

 - About 15 minutes
 - A favorite text editor or IDE
 - [Android SDK](http://developer.android.com/sdk/index.html)
 - Your choice of Maven (3.0+) or Gradle (1.4)
 - An Android device or Emulator

### How to Complete this Guide

Like all of Spring's [Getting Started Guides](/getting-started), you can choose to start from scratch and complete each step, or you can jump past basic setup steps that may already be familiar to you. Either way, you will end up with working code.

To **start from scratch**, just move on to the next section and start [setting up the project](#scratch).

If you would like to **skip the basics**, then do the following:

 - [download][zip] and unzip the source repository for this guide—or clone it using [git](/understanding/git):
`git clone https://github.com/springframework-meta/gs-consuming-rest-xml-android.git`
 - cd into `gs-consuming-rest-xml-android/initial`
 
 - jump ahead to [creating a representation class](#initial).

And **when you are finished**, you can check your results against the the code in `gs-consuming-rest-xml-android/complete`.


<a name="scratch"></a>
## Setting Up the Project

First you will need to set up a basic build script. You can use any build system you like when building apps with Spring, but we have included what you will need to work with [Maven](https://maven.apache.org) and [Gradle](http://gradle.org) here. If you are not familiar with either of these, you can refer to our [Getting Started with Maven on Android](../gs-maven-android/README.md) or [Getting Started with Gradle on Android](../gs-gradle-android/README.md) guides.

### Maven

Create a Maven POM that looks like this:

`pom.xml`
```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<groupId>org.hello</groupId>
	<artifactId>rest-xml-android-complete</artifactId>
	<version>1.0.0</version>
	<packaging>apk</packaging>

	<dependencies>
		<dependency>
			<groupId>com.google.android</groupId>
			<artifactId>android</artifactId>
			<version>4.1.1.4</version>
			<scope>provided</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.android</groupId>
			<artifactId>spring-android-rest-template</artifactId>
			<version>1.0.1.RELEASE</version>
		</dependency>
		<dependency>
    		<groupId>org.simpleframework</groupId>
    		<artifactId>simple-xml</artifactId>
    		<version>2.7</version>
    		<exclusions>
				<exclusion>
					<artifactId>stax</artifactId>
					<groupId>stax</groupId>
				</exclusion>
				<exclusion>
					<artifactId>stax-api</artifactId>
					<groupId>stax</groupId>
				</exclusion>
				<exclusion>
					<artifactId>xpp3</artifactId>
					<groupId>xpp3</groupId>
				</exclusion>
			</exclusions>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>com.jayway.maven.plugins.android.generation2</groupId>
				<artifactId>android-maven-plugin</artifactId>
				<version>3.5.3</version>
				<configuration>
					<sdk>
						<platform>17</platform>
					</sdk>
					<deleteConflictingFiles>true</deleteConflictingFiles>
					<undeployBeforeDeploy>true</undeployBeforeDeploy>
				</configuration>
				<extensions>true</extensions>
			</plugin>
			<plugin>
				<artifactId>maven-compiler-plugin</artifactId>
				<version>3.1</version>
				<configuration>
					<source>1.6</source>
					<target>1.6</target>
				</configuration>
			</plugin>
		</plugins>
	</build>

</project>
```


### Gradle

TODO: paste complete build.gradle.

Add the following within the `dependencies { }` section of your build.gradle file:

`build.gradle`
```groovy
gradle compile ""
```


## Defining a Simple Android Manifest

Every Android application must have an AndroidManifest.xml file. The [Android Manifest] contains all the information required to run an Android application, and it cannot build without one.

Create an AndroidManifest.xml file at the root of the project and add the following contents:

`AndroidManifest.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="org.hello"
    android:versionCode="1"
    android:versionName="1.0" >

    <uses-sdk android:minSdkVersion="7" />
    <uses-permission android:name="android.permission.INTERNET" />

    <application android:label="@string/app_name" >
        <activity
            android:name=".HelloActivity"
            android:label="@string/app_name" >
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

This represents a very simple Android app, which only contains a single activity called `HelloActivity`. In this example, the `HelloActivity` is a simple view with a text field. The `<intent-filter>` settings tell Android to display this activity when the app launches.


## Defining a Simple Layout

Android apps consist of one or more activities. An activity utilizes a layout to display and organize information. Our `HelloActivity`, for which we will define shortly, will make use of the following layout.

`res/layout/main.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent"
    >
<TextView  
    android:id="@+id/text_view"
    android:layout_width="fill_parent" 
    android:layout_height="wrap_content" 
    />
</LinearLayout>

``` 

<a name="initial"></a>
## Creating a Representation Class

With the Android project configured, it is time to create our REST request. Before we can do that though, we need to consider the data we are wanting to consume.

When we query the service at the /hello-world endpoint, we will receive an XML response. This response represents a greeting and will resemble the following:

```xml
<greeting>
	<id>1</id>
	<content>"Hello, World!"</content>
</greeting>
```

The `id` field is a unique identifier for the greeting, and `content` is the textual representation of the greeting.

To model the greeting representation, we will create a representation class:

`src/main/java/org/hello/Greeting.java`
```java
package org.hello;

public class Greeting {

    private final long id;
    private final String content;

    public Greeting(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
```


## Consuming REST Services with RestTemplate

Spring provides a convenient template class called `RestTemplate`. `RestTemplate` makes interacting with most RESTful services a simple process. In the example below, we establish a few variables and then make a request of our simple REST service. As mentioned earlier, we will use the [Simple XML] library to marshal the XML response data into our representation classes.

`src/main/java/org/hello/HelloActivity.java`

```java
package org.hello;

import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.app.Activity;
import android.os.Bundle;

public class HelloActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	@Override
	public void onStart() {
		final String url = "http://10.0.2.2:8080/hello-world";
		
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());

		Greeting greeting = restTemplate.getForObject(url, Greeting.class);
	}

}
```

Thus far, we've only used the HTTP verb `GET` to make calls, but we could just as easily have used `POST`, `PUT`, etc.


## Starting the REST Service

The code is now complete, so we can run the application to see the results. In order to consume a REST service, you must first have a REST service in which to consume. This project includes a simple self contained application for use with testing our REST request. You can start the server by running the following shell script from the `service` folder:

```sh
$ ./start-service.sh
```

Now that the service is running we can start the Android emulator and deploy the application.


## Starting an Android Virtual Device

If you do not have an Android device for testing, you can use an Android Virtual Device (AVD). To do this, you must first have the [Android SDK] installed and also have installed the corresponding SDK Platforms and Packages.

### Creating an AVD

The following command creates a new AVD based on Android 4.2.2 (API Level 17).

```sh
$ android create avd --name Default --target 29 --abi armeabi-v7a
```

### Start the AVD

Use the following command to start the emulator using the Android Maven Plugin:

```sh
$ mvn android:emulator-start
```

This command will try to start an emulator named "Default". Please be patient as the emulator takes a few moments to finish startup.


## Building and Running the Client

Once the emulator has completed starting up, run the following command to invoke the code and see the results of the REST request:

```sh
$ mvn clean package android:deploy android:run
```
	
This will compile the Android app and then run it in the emulator.


## Conclusion

Congratulations! You have now created a simple Android app that connects to a RESTful service and retrieves XML data.


[zip]: https://github.com/springframework-meta/gs-consuming-rest-xml-android/archive/master.zip



