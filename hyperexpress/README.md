HyperExpress Plugin
===================

The HyperExpress plugin gathers metadata about the routes in your RestExpress service suite
to render live documentation, so it's never out of date.

Adds routes within your service suite to facilitate Swagger documentation and usage.
By default, it add a route, /api-docs, but is configurable when instantiating the plugin.

It is possible to set flags and parameters on the plugin, so that preprocessors and postprocessors
handle them correctly.  For example, making the /api-docs route public so that it doesn't
require authentication or authorization.

Maven Usage
===========
Stable:
```xml
		<dependency>
			<groupId>com.strategicgains.plugin-express</groupId>
			<artifactId>HyperExpressPlugin</artifactId>
			<version>0.2.4</version>
		</dependency>
```
Development:
```xml
		<dependency>
			<groupId>com.strategicgains.plugin-express</groupId>
			<artifactId>HyperExpressPlugin</artifactId>
			<version>0.2.3-SNAPSHOT</version>
		</dependency>
```
Or download the jar directly from: 
http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22HyperExpressPlugin%22

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


Simply instantiate the plugin and register it with the RestExpress server, setting options
as necessary, using method chaining if desired.

For example:
```java
RestExpress server = new RestExpress()...

new SwaggerPlugin("/api-docs")				// URL path is optional. Defaults to '/api-docs'
	.flag("public-route")					// optional. Set a flag on the request for this route.
	.register(server);
```
