Routes Metadata Plugin
======================

Adds several routes within your service suite to facilitate auto-discovery of what's available. 

Routes added are:
* /routes/metadata.{format} - to retrieve all metadata for all routes.
* /routes/{routeName}/metadata.{format} - to retrieve metadata for a named route.
* /routes/console.html - placeholder for later. Not recommended.

The plugin allows flags and parameters, just like the regular Route DSL to set flags and parameters on the routes created
by the plugin so appropriate values are available in preprocessors, etc.  For instance, if you want to turn off 
authentication or authorization for the metadata routes.

Maven Usage
===========
Stable:
```xml
		<dependency>
			<groupId>com.strategicgains.plugin-express</groupId>
			<artifactId>RoutesMetadataPlugin</artifactId>
			<version>0.2.6</version>
		</dependency>
```
Development:
```xml
		<dependency>
			<groupId>com.strategicgains.plugin-express</groupId>
			<artifactId>RoutesMetadataPlugin</artifactId>
			<version>0.3.0-SNAPSHOT</version>
		</dependency>
```
Or download the jar directly from: 
http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22RoutesMetadataPlugin%22

Note that to use the SNAPSHOT version, you must enable snapshots and a repository in your pom (or settings.xml) file as follows:
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

Usage
=====

Simply create a new plugin and register it with the RestExpress server, setting options
as necessary, using method chaining if desired.

For example:
```java
RestExpress server = new RestExpress()...

new RoutesMetadataPlugin()
	.flag("public-route")					// optional. Set a flag on the request for this route.
	.parameter("name", "value")				// optional. Set a parameter on the request for this route.
	.register(server);
```
