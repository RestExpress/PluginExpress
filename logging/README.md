Logging Plugin
==============

Uses SLF4J to log using a MessageObserver implementation.

The plugin outputs INFO, WARN, ERROR level logging messages as follows:

* INFO - for all messages received and processed, with timing data.
* WARN - for all HTTP statuses from 400-499.
* ERROR - for HTTP statuses of 500 (or other internal errors).

Usage:

```Java
RestExpress server = new RestExpress()...

new LoggingPlugin()
    .register(server);
```

Maven Usage
===========
Stable:
```xml
		<dependency>
			<groupId>com.strategicgains.plugin-express</groupId>
			<artifactId>LoggingPlugin</artifactId>
			<version>0.2.6</version>
		</dependency>
```
Development:
```xml
		<dependency>
			<groupId>com.strategicgains.plugin-express</groupId>
			<artifactId>LoggingPlugin</artifactId>
			<version>0.3.0-SNAPSHOT</version>
		</dependency>
```
Or download the jar directly from: 
http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22LoggingPlugin%22

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

