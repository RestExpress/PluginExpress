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
			<version>0.2.2</version>
		</dependency>
```
Development:
```xml
		<dependency>
			<groupId>com.strategicgains.plugin-express</groupId>
			<artifactId>CORSPlugin</artifactId>
			<version>0.2.3-SNAPSHOT</version>
		</dependency>
```
Or download the jar directly from: 
http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22CORSPlugin%22

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

Usage of the CORS Plugin is basically the same as the other plugins in this registry.
Simply create a new plugin and register it with the RestExpress server, setting options
as necessary, using method chaining if desired.

*NOTE* It's recommended to make this the LAST plugin registered as it iterates all routes defined in your service suite and 
adds an OPTIONS route, if configured to support the CORS OPTIONS pre-flight request.

To support the CORS OPTIONS Preflight request:
```java
RestExpress server = new RestExpress()...

new CorsHeaderPlugin("*")							// Array of domain strings.
	.exposeHeaders("Location")						// Array of header names (Optional).
	.allowHeaders("Content-Type", "Accept", "Authorization")	// Array of header names (Optional).
	.maxAge(2592000)								// Seconds to cache (Optional).
	.flag("flag value")								// Just like flag() on Routes (Optional).
	.parameter("string", object)					// Just like parameter() on Routes (Optional).
	.register(server);
```

Or to NOT support the CORS OPTIONS Preflight request:
```java
RestExpress server = new RestExpress()...

new CorsHeaderPlugin("*")							// Array of domain strings.
	.exposeHeaders("Location")						// Array of header names (Optional).
	.noPreflightSupport()							// Turn off OPTIONS request support.
	.register(server);
```

To be more specific, my common CORS configuration is:
```java
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.*;
import static org.restexpress.Flags.Auth.PUBLIC_ROUTE;

new CorsHeaderPlugin("*")
	.flag(PUBLIC_ROUTE)
	.allowHeaders(CONTENT_TYPE, ACCEPT, AUTHORIZATION)
	.exposeHeaders(LOCATION)
	.register(server);
```