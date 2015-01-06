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
public class UserController implements AbstractController {
    
}
```

Release Notes
=============
Release 0.0.1 - 07 Jan 2015
---------------------------
* First commit.
