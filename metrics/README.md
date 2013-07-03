Metrics Plugin
==============

Enables metrics on all routes in the service suite via the Yammer (Coda Hale) Metrics library.  Metrics are available
via JMX, but can be published to Graphite simply by configuring the Yammer (Coda Hale) Metrics publisher.

This plugin maintains metrics for the following:
* currently active requests (counter)
* all exceptions occurred (counter)
* all times (timer, milliseconds/hours)
* times by route (timer, milliseconds/hours)
* exceptions by route (counter)
* counters by return status (counter)

In addition, the overall response time is set in the response header, X-Response-Time, in milliseconds.

*Note:* Named routes are reported using their name.  Unnamed routes are reported using the URL pattern.

Maven Usage
===========
Stable:
```xml
		<dependency>
			<groupId>com.strategicgains.plugin-express</groupId>
			<artifactId>MetricsPlugin</artifactId>
			<version>0.1.3</version>
		</dependency>
```
Development:
```xml
		<dependency>
			<groupId>com.strategicgains.plugin-express</groupId>
			<artifactId>MetricsPlugin</artifactId>
			<version>0.1.4-SNAPSHOT</version>
		</dependency>
```
Or download the jar directly from: 
http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22MetricsPlugin%22

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

Usage of the Metrics Plugin is basically the same as the other plugins in this registry.
Simply create a new plugin and register it with the RestExpress server, setting options
as necessary, using method chaining if desired.

For example, to make metrics available via JMX:
```java
RestExpress server = new RestExpress()...

new MetricsPlugin()
	.virtualMachineId("us-east-1a-beta1")	// optional. Unique name metrics are published under.
	.noLogging()							// optional. Turn off output logging.
	.logOutputFactory(logOutputFactory)		// optional. Set your own LogOutputFactory (SLF4J).
	.register(server);
```

Or to additionally publish metrics to Graphite:
```java
RestExpress server = new RestExpress()...

new MetricsPlugin().register(server);

// Publish to graphite.example.com every minute...
GraphiteReporter.enable(1, TimeUnit.MINUTES, "graphite.example.com", 2003);
```
