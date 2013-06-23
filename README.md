Getting Started: Consuming XML from a REST Service on Android
=============================================================

What you'll build
-----------------

This Getting Started guide will walk you through the process of consuming XML from a REST service using Spring for Android's `RestTemplate`.

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
Installing the Android Development Environment
----------------------------------------------

Building Android applications requires the installation of the [Android SDK][sdk].

### Install the Android SDK

1. Download the correct version of the [Android SDK][sdk] for your operating system from the Android web site.

2. Unzip the archive and place it in a location of your choosing. For example on Linux or Mac, you may want to place it in the root of your user directory. See the [Android Developers] web site for additional installation details.

3. Configure the `ANDROID_HOME` environment variable based on the location where you installed the Android SDK. Additionally, you should consider adding `ANDROID_HOME/tools`, and  `ANDROID_HOME/platform-tools` to your PATH.

    Mac OS X:

    ```sh
    $ export ANDROID_HOME=/<installation location>/android-sdk-macosx
    $ export PATH=${PATH}:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
    ```

    Linux:

    ```sh
    $ export ANDROID_HOME=/<installation location>/android-sdk-linux
    $ export PATH=${PATH}:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
    ```

    Windows:

    ```sh
    set ANDROID_HOME=C:\<installation location>\android-sdk-windows
    set PATH=%PATH%;%ANDROID_HOME%\tools;%ANDROID_HOME%\platform-tools
    ```

4. Once the SDK is installed, we need to add the relevant [Platforms and Packages]. We are using Android 4.2.2 (API Level 17) in this guide.

### Install Android SDK Platforms and Packages

The [Android SDK][sdk] download does not include any specific Android platforms. In order to run the code in this guide, you need to download and install the latest SDK Platform. You accomplish this by using the *Android SDK and AVD Manager* that was installed from the previous step.

1. Open the *Android SDK Manager* window:

    ```sh
    $ android
    ```

    > Note: if this command does not open the *Android SDK Manager*, then your path is not configured correctly.

2. Select the checkbox for *Tools*

3. Select the checkbox for the latest Android SDK, "Android 4.2.2 (API Level 17)" as of this writing

4. Select the checkbox for the *Android Support Library* from the *Extras* folder

5. Click the **Install packages...** button to complete the download and installation

    > Note: you may want to simply install all the available updates, but be aware it will take longer, as each API level is a sizable download.

[sdk]: http://developer.android.com/sdk/index.html
[Android Developers]: http://developer.android.com/sdk/installing/index.html
[Platforms and Packages]: http://developer.android.com/sdk/installing/adding-packages.html


Set up the project
------------------

First you set up a basic build script. You can use any build system you like when building apps with Spring, but the code you need to work with [Maven](https://maven.apache.org) and [Gradle](http://gradle.org) is included here. If you're not familiar with either, refer to [Getting Started with Maven](../gs-maven-android/README.md) or [Getting Started with Gradle](../gs-gradle-android/README.md).

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

### Create an Android Manifest

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

### Create a String Resource

`res/values/strings.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">REST XML Complete</string>
</resources>
```

### Create a Layout

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


Invoke a REST service with RestTemplate
---------------------------------------

Spring provides a convenient template class called `RestTemplate`. `RestTemplate` makes interacting with most RESTful services a simple process. In the example below, we establish a few variables and then make a request of our simple REST service. As mentioned earlier, we will use the [Simple XML] library to marshal the XML response data into our representation classes.

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

Thus far, we've only used the HTTP verb `GET` to make calls, but we could just as easily have used `POST`, `PUT`, etc.


## Start the REST service

The code is now complete, so we can run the application to see the results. In order to consume a REST service, you must first have a REST service in which to consume. This project includes a simple self contained application for use with testing our REST request. You can start the server by running the following shell script from the `service` folder:

```sh
$ ./start-service.sh
```


Start an Android Virtual Device
----------------------------------

If you do not have an Android device for testing, you can use an [Android Virtual Device (AVD)][avd]. To do this, you must first have the [Android SDK][sdk] installed and also have installed the corresponding SDK [Platforms and Packages].

### Create an AVD

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

[sdk]: http://developer.android.com/sdk/index.html
[avd]: http://developer.android.com/tools/devices/index.html
[Platforms and Packages]: http://developer.android.com/sdk/installing/adding-packages.html


Build and Run the Client
------------------------

Once the emulator has finished starting up, run the following command to invoke the code and see the results of the REST request:

```sh
$ mvn clean package android:deploy android:run
```

This will build the Android app and then run it in the emulator.


Summary
-------

Congratulations! You have just developed a simple REST client using Spring.

There's more to building and working with REST APIs than is covered here.

[zip]: https://github.com/springframework-meta/gs-consuming-rest-xml-android/archive/master.zip
