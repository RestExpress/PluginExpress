Cross-Site Scripting (XSS) Prevention Plugin
============================================

For serialized routes (those NOT denoted with .noSerialization()), enables outbound XSS encoding
provided by the OWASP encoding library (see ).  Essentially, simply installs a postprocessor
to operate on the serialized response before sending it to the client.

* For application/xml responses, uses XML encoding.
* For application/json, using JSON encoding.
* For others, use the .encode(content-type, encoding) method to set the encoding for responses of that content type.

Maven Usage
===========
Development:
```xml
		<dependency>
			<groupId>com.strategicgains.plugin-express</groupId>
			<artifactId>XSS-Plugin</artifactId>
			<version>0.2.1-SNAPSHOT</version>
		</dependency>
```
Or download the jar directly from: 
http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22XSS-Plugin%22

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

Usage of the XSS Plugin is basically the same as the other plugins in this registry.
Simply create a new plugin and register it with the RestExpress server, using method
chaining if desired.

For example, to support output encoding for JSON and XML with the content types of
application/json and application/xml, respectively:

```java
RestExpress server = new RestExpress()...

new XssPlugin()
	.register(server);
```

Or to support output encoding for other conent-types, for example HAL (application/hal+json:

```java
RestExpress server = new RestExpress()...

new XssPlugin()
    .encode("application/hal+json", Encoding.JSON)
    .encode("application/hal+xml", Encoding.XML)
    .register(server);

```
