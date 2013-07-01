Getting Started: Consuming XML from a REST Service with Spring for Android
==========================================================================

What you'll build
-----------------

This Getting Started guide walks you through the process of building an application that uses Spring for Android's `RestTemplate` to consume XML from a REST service.

What you'll need
----------------

 - About 15 minutes
 - A favorite text editor or IDE
 - [Android SDK][sdk]
 - [Maven 3.0][mvn] or later
 - An Android device or Emulator

[sdk]: http://developer.android.com/sdk/index.html
[mvn]: http://maven.apache.org/download.cgi

How to complete this guide
--------------------------

Like all Spring's [Getting Started guides](/getting-started), you can start from scratch and complete each step, or you can bypass basic setup steps that are already familiar to you. Either way, you end up with working code.

To **start from scratch**, move on to [Set up the project](#scratch).

To **skip the basics**, do the following:

 - [Download][zip] and unzip the source repository for this guide, or clone it using [git](/understanding/git):
`git clone https://github.com/springframework-meta/{@project-name}.git`
 - cd into `{@project-name}/initial`
 - Jump ahead to [Create a resource representation class](#initial).

**When you're finished**, you can check your results against the code in `{@project-name}/complete`.

<a name="scratch"></a>
Set up the project
------------------

In this section you set up a basic build script and then create a simple application. 

> **Note:** If you are new to Android projects, before you proceed, refer to [Getting Started with Android](../gs-android/README.md) to help you configure your development environment. 

You can use any build system you like when building apps with Spring, but the code you need to work with [Maven](https://maven.apache.org) and [Gradle](http://gradle.org) is included here. If you're not familiar with either, refer to [Getting Started with Maven](../gs-maven-android/README.md) or [Getting Started with Gradle](../gs-gradle-android/README.md).
 

### Create the directory structure

In a project directory of your choosing, create the following subdirectory structure; for example, with the following command on Mac or Linux:

```sh
$ mkdir -p src/main/java/org/hello
```

    └── src
        └── main
            └── java
                └── org
                    └── hello

### Create a Maven POM

`pom.xml`
```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.hello</groupId>
    <artifactId>rest-xml-android-complete</artifactId>
    <version>0.1.0</version>
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
                <version>3.6.0</version>
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

### Create an Android manifest

The [Android Manifest] contains all the information required to run an Android application, and it cannot build without one.

[Android Manifest]: http://developer.android.com/guide/topics/manifest/manifest-intro.html

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

### Create a string resource
Add a text string. Text strings can be referenced from the application or from other resource files.

`res/values/strings.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">REST XML Complete</string>
</resources>
```

### Create a layout
Here you define the visual structure for the user interface of your application.

`res/layout/hello_layout.xml`
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

`src/main/java/org/hello/Greeting.java`
```java
package org.hello;

public class Greeting {

    private long id;
    private String content;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
```


Invoke a REST service with the RestTemplate
-------------------------------------------

Spring provides a convenient template class called `RestTemplate`. `RestTemplate` makes interacting with most RESTful services a simple process. In the example below, you establish a few variables and then make a request of the simple REST service. As mentioned earlier, you use the [Simple XML] library to marshal the XML response data into your representation classes.

`src/main/java/org/hello/HelloActivity.java`
```java
package org.hello;

import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class HelloActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hello_layout);
    }

    @Override
    public void onStart() {
        super.onStart();
        final String url = "http://10.0.2.2:8080/hello-world";

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());

        Greeting greeting = restTemplate.getForObject(url, Greeting.class);

        TextView textView = (TextView) this.findViewById(R.id.text_view);
        textView.setText(greeting.getContent());
    }

}
```

Thus far, you've only used the HTTP verb `GET` to make calls, but you could just as easily have used `POST`, `PUT`, and so on.


## Start the REST service

In order to consume a REST service, you must first have a REST service to consume. This project includes a simple self-contained application for use with testing the REST request. You can start the server by running the following shell script from the `service` folder:

```sh
$ ./start-service.sh
```


Build and run the client
------------------------

With an attached device or emulator running, invoke the code and see the results of the REST request:

```sh
$ mvn clean package android:deploy android:run
```

The command builds the Android app and runs it in the emulator or attached device.


Summary
-------

Congratulations! You have just developed a simple REST client using Spring.


[zip]: https://github.com/springframework-meta/gs-consuming-rest-xml-android/archive/master.zip
