Cache-Control Plugin
====================

Adds caching-related headers to GET responses.

Maven Usage
===========
Stable:
```xml
		<dependency>
			<groupId>com.strategicgains.plugin-express</groupId>
			<artifactId>CacheControlPlugin</artifactId>
			<version>0.1.3</version>
		</dependency>
```
Development:
```xml
		<dependency>
			<groupId>com.strategicgains.plugin-express</groupId>
			<artifactId>CacheControlPlugin</artifactId>
			<version>0.1.4-SNAPSHOT</version>
		</dependency>
```
Or download the jar directly from: 
http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22CacheControlPlugin%22

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
For GET requests, adds a Date: \<timestamp\> header, if not already present. This enables clients to determine age of a representation for caching purposes.  Where \<timestamp\> is in RFC1123 full date format.

Note that HEAD requests are not provided with a Date header via this postprocessor. This is due to the fact that most external caches forward HEAD requests to the origin server as a GET request and cache the result.

If the route has a Parameters.Cache.MAX_AGE parameter, whose value is the max-age in seconds then the following are added:
* Cache-Control: max-age=\<seconds\>
* Expires: now + max-age

If the route has a Flags.Cache.NO_CACHE flag, then the following headers are set on the response:
* Cache-Control: no-cache
* Pragma: no-cache

The MAX_AGE parameter takes precidence, in that, if present, the NO_CACHE flag is ignored.

If the response body is non-null, adds an ETag header.  ETag is computed from the body object's hash code combined with the hash code of the resulting format.

**NOTE:** To fully support basic caching capability, also implement a LastModifiedHeaderPostprocessor() that inspects the date on your domain or presentation model and sets the 'Last-Modified' header.

Example Usage:
```Java
    RestExpress server = new RestExpress();
    ...
    new CacheControlPlugin()
        .register(server);
    server.addPostprocessor(new LastModifiedHeaderProcessor());
```

An example LastModifiedHeaderPostprocessor:
```Java
    public class LastModifiedHeaderPostprocessor
    implements Postprocessor
    {
		DateAdapter fmt = new HttpHeaderTimestampAdapter();

		@Override
		public void process(Request request, Response response)
		{
			if (!request.isMethodGet()) return;
			if (!response.hasBody()) return;

			Object body = response.getBody();

			if (!response.hasHeader(LAST_MODIFIED) && body instanceof Timestamped)
			{
				response.addHeader(LAST_MODIFIED, fmt.format(((Timestamped) body).getUpdatedAt()));
			}
		}
    }
```
