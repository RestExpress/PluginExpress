CORS Plugin
===========

General RestExpress plugin to support CORS pre-flight OPTIONS request, as well as CORS headers support.

Maven Usage
===========
Stable:
```xml
		<dependency>
			<groupId>com.strategicgains.plugin-express</groupId>
			<artifactId>CORSPlugin</artifactId>
			<version>0.1.2</version>
		</dependency>
```
Development:
```xml
		<dependency>
			<groupId>com.strategicgains.plugin-express</groupId>
			<artifactId>CORSPlugin</artifactId>
			<version>0.1.3-SNAPSHOT</version>
		</dependency>
```
Or download the jar directly from: 
http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22CORSPlugin%22

Note that to use the SNAPSHOT version, you must enable snapshots and a repository in your pom file as follows:
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

Usage of the CORS Plugin is basically the same as the other plugins in this registry.
Simply create a new plugin and register it with the RestExpress server, setting options
as necessary, using method chaining if desired.

'''java
RestExpress server = new RestExpress()...

new CorsHeaderPlugin("*")							// Array of domain strings.
	.exposeHeaders("Location")						// Array of header names (Optional).
	.allowHeaders("Content-Type", "Accept")			// Array of header names (Optional).
	.maxAge(2592000)								// Seconds to cache (Optional).
	.flag("flag value")								// Just like flag() on Routes.
	.parameter("string", object)					// Just like parameter() on Routes.
	.noPreflightSupport()							// 
	.register(server);

'''
