Annotation Plugin
===========

Plugin to support annotation in controllers.

Maven Usage
===========
Stable:
```xml
		<dependency>
			<groupId>com.strategicgains.plugin-express</groupId>
			<artifactId>AnnotationPlugin</artifactId>
			<version>0.0.1</version>
		</dependency>
```


Usage
=====

Usage of the Annotation Plugin is basically the same as the other plugins in this registry.
Simply create a new plugin and register it with the RestExpress server, setting options
as necessary, using method chaining if desired.

*NOTE* This plugin also create the routes.

```java
RestExpress server = new RestExpress()...

new AnnotationPlugin()
	.setControllersPackage("com.my-package.controllers")   // Package to scan for controllers.
	.register(server);
```

Configuring controllers:
```java
public class FakeController implements AnnotationController {

    public Object create(Request request, Response response) {
        return null;
    }

    public Object read(Request request, Response response) {
        return null;
    }

    @Route(httpMethod="GET", name="user.all", uri="/users.{format}")
    public List<Object> readAll(Request request, Response response) {
        return Collections.emptyList();
    }

    public void update(Request request, Response response) {
    }

    public void delete(Request request, Response response) {
    }
}
```

Release Notes
=============
Release 0.0.1 - 07 Jan 2015
---------------------------
* First commit.
