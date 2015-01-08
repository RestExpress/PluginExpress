Metrics Plugin
==============

Enables metrics on all routes in the service suite via the Yammer (Coda Hale) Metrics library.  Metrics are available
via JMX (not recommended for production) by configuring the Coda Hale JMX Metrics publisher and can be published to
Graphite simply by configuring the Coda Hale Graphite publisher (see 'Usage' below).

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
			<version>0.2.6</version>
		</dependency>
```
Development:
```xml
		<dependency>
			<groupId>com.strategicgains.plugin-express</groupId>
			<artifactId>MetricsPlugin</artifactId>
			<version>0.3.0-SNAPSHOT</version>
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
Simply create a new plugin and register it with the RestExpress server, using method
chaining if desired.  However, to publish the gathered metrics to JMX or Graphite (or any
of the other Coda Hale Metrics library-supported endpoints), you must create a
MetricRegistry instance and pass it in to the plugin constructor.

For example, to make metrics available via JMX:
```java
RestExpress server = new RestExpress()...

MetricRegistry registry = new MetricRegistry();
new MetricsPlugin(registry)
	.register(server);

final JmxReporter reporter = JmxReporter.forRegistry(registry).build();
reporter.start();
```
Or to publish metrics to the Console:
```java
final ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
                                                .convertRatesTo(TimeUnit.SECONDS)
                                                .convertDurationsTo(TimeUnit.MILLISECONDS)
                                                .build();
reporter.start(1, TimeUnit.MINUTES);
```
Or to publish metrics to Graphite:
```java
RestExpress server = new RestExpress()...

MetricRegistry registry = new MetricRegistry();
new MetricsPlugin(registry).register(server);

// Publish to graphite.example.com every minute...
final Graphite graphite = new Graphite(new InetSocketAddress("graphite.example.com", 2003));
final GraphiteReporter reporter = GraphiteReporter.forRegistry(registry)
                                                  .prefixedWith("web1.example.com")
                                                  .convertRatesTo(TimeUnit.SECONDS)
                                                  .convertDurationsTo(TimeUnit.MILLISECONDS)
                                                  .filter(MetricFilter.ALL)
                                                  .build(graphite);
reporter.start(1, TimeUnit.MINUTES);
```

For more information on supported publishers see also: http://metrics.codahale.com/manual/
