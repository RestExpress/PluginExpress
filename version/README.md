Version Plugin
==============

Adds a /version route that returns the version number of your API.

Usage:

```Java
RestExpress server = new RestExpress()...

new VersionPlugin("1.0")	     // Supply the version string here.
    .path("/version")            // optional. Supply the version route URL here. Defaults to "/version"
    .flag("flag value")          // optional. Set a flag on the request for this route.
    .parameter("name", object)   // optional. Set a parameter on the request for this route.
    .register(server);
```

Maven Usage
===========
Stable:
```xml
		<dependency>
			<groupId>com.strategicgains.plugin-express</groupId>
			<artifactId>VersionPlugin</artifactId>
			<version>0.2.5</version>
		</dependency>
```
Development:
```xml
		<dependency>
			<groupId>com.strategicgains.plugin-express</groupId>
			<artifactId>VersionPlugin</artifactId>
			<version>0.2.6-SNAPSHOT</version>
		</dependency>
```
Or download the jar directly from: 
http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22VersionPlugin%22

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