Plugin-Registry
===============

RestExpress-specific plugins.  Plus a registry of third-party (externally written) plugins for enhancing RestExpress functionality.

Maven Usage
===========
Stable:
```xml
		<dependency>
			<groupId>com.strategicgains</groupId>
			<artifactId>PluginExpress</artifactId>
			<version>0.1.0</version>
		</dependency>
```
Development:
```xml
		<dependency>
			<groupId>com.strategicgains</groupId>
			<artifactId>PluginExpress</artifactId>
			<version>0.1.1-SNAPSHOT</version>
		</dependency>
```
Or download the jar directly from: 
http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22PluginExpress%22

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