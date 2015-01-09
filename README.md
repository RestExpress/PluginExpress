**Build Status** [![Build Status](https://buildhive.cloudbees.com/job/RestExpress/job/PluginExpress/badge/icon)](https://buildhive.cloudbees.com/job/RestExpress/job/PluginExpress/)

**waffle.io** [![Stories in Ready](https://badge.waffle.io/RestExpress/PluginExpress.png?label=ready)](https://waffle.io/RestExpress/PluginExpress)

Plugin-Registry
===============

RestExpress-specific plugins.  Plus a registry of third-party (externally written) plugins for enhancing RestExpress functionality.
Please see the README for each individual plugin for additional details.  Or the PluginExpress wiki page.

General Maven Usage
===================
Stable:
```xml
		<dependency>
			<groupId>com.strategicgains.plugin-express</groupId>
			<artifactId>* plugin name here *</artifactId>
			<version>0.2.6</version>
		</dependency>
```
Development:
```xml
		<dependency>
			<groupId>com.strategicgains.plugin-express</groupId>
			<artifactId>PluginExpress</artifactId>
			<version>0.3.0-SNAPSHOT</version>
		</dependency>
```
Or download the jar directly from: 
http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22plugin-express%22

Note that to use the SNAPSHOT versions, you must enable snapshots and a repository in your pom file as follows:
```xml
  <profiles>
    <profile>
       <id>allow-snapshots</id>
          <activation><activeByDefault>true</activeByDefault></activation>
       <repositories>
         <repository>
           <id>snapshots-repo</id>
           <url>https://oss.sonatype.org/content/repositories/snapshots</url>
           <releases><enabled>false</enabled></releases>
           <snapshots><enabled>true</enabled></snapshots>
         </repository>
       </repositories>
     </profile>
  </profiles>
```
