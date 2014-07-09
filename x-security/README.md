X-Security Plugin
====================

Adds owasp recomended x-security headers to responses. Based on the 
recommendations at https://www.owasp.org/index.php/List_of_useful_HTTP_headers

Maven Usage
===========
Stable:
```xml
		<dependency>
			<groupId>com.strategicgains.plugin-express</groupId>
			<artifactId>XSecurityPlugin</artifactId>
			<version>0.2.2</version>
		</dependency>
```
Development:
```xml
		<dependency>
			<groupId>com.strategicgains.plugin-express</groupId>
			<artifactId>XSecurityPlugin</artifactId>
			<version>0.2.3-SNAPSHOT</version>
		</dependency>
```
Or download the jar directly from: 
http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22XSecurityPlugin%22

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
========
This plugin adds the X-Content-Type-Options header to all responses leaving 
the RestExpress server. The only currently defined option is "nosniff",
therefore each header leaving the system will have a header that looks like
```X-Content-Type-Options: nosniff```


Example Usage:
```Java
    RestExpress server = new RestExpress();
    ...
    new XSecurityPlugin()
        .register(server);
```

