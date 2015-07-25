Correlation ID Plugin
=====================

This RestExpress plugin retrieves the Correlation-Id header from the request
and adds it to the global, thread-safe RequestContext to be available in deeper
levels of the application.

If the Correlation-Id header does not exist on the request, one is assigned
using a UUID string.

The Correlation-Id header is assigned to the response on its way out, also.

Maven Usage
===========

Development:
```xml
		<dependency>
			<groupId>com.strategicgains.plugin-express</groupId>
			<artifactId>CORSPlugin</artifactId>
			<version>0.3.2-SNAPSHOT</version>
		</dependency>
```
Or download the jar directly from: 
http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22CorrelationIdPlugin%22

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

Usage of the Correlation ID Plugin is basically the same as the other plugins in this registry.
Simply create a new plugin and register it with the RestExpress server, using method chaining as desired.

To support Correlation-Id in all requests:

```java
RestExpress server = new RestExpress()...

new CorrelationIdPlugin()
	.register(server);
```

Then in controller methods, the correlation id is available as a header on the request. It is accessed
as follows:

```java
String correlationId = request.getHeader(CorrelationIdPlugin.CORRELATION_ID);
```

Because the plugin also sets the correlation id on the RequestContext object, a thread-safe, global
singleton, the value can be accessed in parts of your application that don't have access to the request.
It can be accessed as follows:

```java
String correlationId = (String) RequestContext.get(CorrelationIdPlugin.CORRELATION_ID);
```
