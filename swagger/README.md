Swagger Plugin
==============

*Help Us Out!* This plugin is not quite ready for prime time. While it honors many of the Swagger-provided annotations, when they are absent,
it provides defaults by simply introspects your object models, exposing every non-static, non-transient property.  This may not be desired.  Additionally, it doesn't
yet support annotations for the 'authorizations' and 'info' Swagger models.  Help us get it to prime time by sending a pull request!

The Swagger plugin gathers metadata about the routes in your RestExpress service suite
to render live documentation, so it's never out of date.

Adds routes within your service suite to facilitate Swagger documentation and usage.
By default, it add a route, /api-docs, but is configurable when instantiating the plugin.

It is possible to set flags and parameters on the plugin, so that preprocessors and postprocessors
handle them correctly.  For example, making the /api-docs route public so that it doesn't
require authentication or authorization.

Maven Usage
===========
Stable:
```xml
		<dependency>
			<groupId>com.strategicgains.plugin-express</groupId>
			<artifactId>SwaggerPlugin</artifactId>
			<version>0.2.6</version>
		</dependency>
```
Development:
```xml
		<dependency>
			<groupId>com.strategicgains.plugin-express</groupId>
			<artifactId>SwaggerPlugin</artifactId>
			<version>0.3.0-SNAPSHOT</version>
		</dependency>
```
Or download the jar directly from: 
http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22SwaggerPlugin%22

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

Simply create a new plugin and register it with the RestExpress server, setting options
as necessary, using method chaining if desired.

For example:
```java
RestExpress server = new RestExpress()...

new SwaggerPlugin("/api-docs")				// URL path is optional. Defaults to '/api-docs'
	.setDefaultToHidden(true)				// if this is set to true then only annotated apis will be shown
	.flag("public-route")					// optional. Set a flag on the request for this route.
	.register(server);
```

Swagger Annotations
===================
The Swagger Plugin now supports several swagger annotations.

### @ApiOperation
[Swagger doc for ApiOperation](https://github.com/wordnik/swagger-core/blob/master/modules/swagger-annotations/src/main/java/com/wordnik/swagger/annotations/ApiOperation.java)

The four parameters that are supported in ApiOperation are value, notes, response, and hidden.  Several of the other values are determined by inspecting the Route metadata.  These include response, httpMethod, and nickname.  If response is defined in the ApiOperation annotation, that will be used, if not, the response class will be set as the return object defined in the controller method.  if the parameter "hidden" is set to true then the API will not appear in the swagger documentation.  The default for "hidden" is set to false (unless specified differently, See Usage above).

Example Usage:
```java
@ApiOperation(value = "Get a specific course item",  // Brief description of the operation
      notes = "This operation will return a specific course item as defined in the route.", // Detailed description of the operation
      response = Item.class, hidden = false) // Response type returned by the method.
public Item getItem(Request request, Response response)
```

### @ApiParam
[Swagger doc for ApiParam](https://github.com/wordnik/swagger-core/blob/master/modules/swagger-annotations/src/main/java/com/wordnik/swagger/annotations/ApiParam.java)

This represents a single parameter for an api operation.

Example Usage:
```java
@ApiParam(name = "title", // Name of the parameter
required = true, // Whether the parameter is required or not
value = "(Required) Title of the item.", // Description of the parameter
defaultValue = "Title placeholder", // Default value if none is assigned.
allowableValues = "Any String") // Description of all allowable values for the parameter.
  public void createWithApiParam(Request request, Response response) 
```

### @ApiImplicitParam
[Swagger doc for ApiImplicitParam](https://github.com/wordnik/swagger-core/blob/master/modules/swagger-annotations/src/main/java/com/wordnik/swagger/annotations/ApiImplicitParam.java)

This represents a single parameter for an api operation.

Example Usage:
```java
@ApiImplicitParam(name = "expand", // Name of the parameter
required = false, // Whether the parameter is required or not.
value = "(Optional) Return item and all its children.", // Description of the parameter
paramType = "query", // Parameter type, i.e. body, query, path.
dataType = "String", // Type of the parameter.
allowableValues = "all, some, none") // Description of the allowable values.
public void createWithApiImplicitParams(Request request, Response response)
```

### @ApiImplicitParams
[Swagger doc for ApiImplicitParams](https://github.com/wordnik/swagger-core/blob/master/modules/swagger-annotations/src/main/java/com/wordnik/swagger/annotations/ApiImplicitParams.java)

Collection of ApiImplicitParam annotations.  This allows one method to define multiple ApiImplicitParam annotations.

Example Usage:
```java
@ApiImplicitParams({
    @ApiImplicitParam(name = "expand", required = false, value = "(Optional) Return item and all its children.", paramType = "query", dataType = "String", allowableValues = "items"),
    @ApiImplicitParam(name = "limit", required = false, value = "(Optional) Set the number of items returned from request.", paramType = "query", dataType = "Integer", allowableValues = "Any integer"),
    @ApiImplicitParam(name = "offset", required = false, value = "(Optional) Return the collection of items starting with the offset number.  The limit query param must also be set if offset is set.", paramType = "query", dataType = "Integer", allowableValues = "Any integer")
})
public Item getItem(Request request, Response response)
```

### @ApiResponse
[Swagger doc for ApiResponse](https://github.com/wordnik/swagger-core/blob/master/modules/swagger-annotations/src/main/java/com/wordnik/swagger/annotations/ApiResponse.java)

Defines a response code that can be returned from this api operation.

Example Usage:
```java
@ApiResponse(code = 200, // Http response code
message = "Successful retrieval of item") // Description of when the code is returned.
public Item getItem(Request request, Response response)
```

### @ApiResponses
[Swagger doc for ApiResponses](https://github.com/wordnik/swagger-core/blob/master/modules/swagger-annotations/src/main/java/com/wordnik/swagger/annotations/ApiResponses.java)

Collection of Api Response annotations allowing one method to define multiple ApiResponse annotations.

Example Usage:
```java
@ApiResponses({
  @ApiResponse(code = 200, message = "Successful retrieval of item"),
  @ApiResponse(code = 404, message = "Course or Item not found"),
  @ApiResponse(code = 403, message = "User not authorized"),
  @ApiResponse(code = 400, message = "Item id incorrect format")
})
public Item getItem(Request request, Response response)
```

### @ApiModel
[Swagger doc for ApiModel](https://github.com/wordnik/swagger-core/blob/master/modules/swagger-annotations/src/main/java/com/wordnik/swagger/annotations/ApiModel.java)

Provides information a model class, including description, as well as inheritance and subtype information.

Example Usage:
```java
@ApiModel(value = "Course Item", // Brief synopsis of the model.
description = "Model that defines a course item that will be returned back to the user.", // Detailed description of the class.
parent = ParentItem.class, // Superclass of the model.
subtypes = [CourseItem.class, UserItem.class]) // SubClasses that inherit this model.
public class Item {
```

### @ApiModelProperty
[Swagger doc for ApiModelProperty](https://github.com/wordnik/swagger-core/blob/master/modules/swagger-annotations/src/main/java/com/wordnik/swagger/annotations/ApiModelProperty.java)

Information on a particular property in the model.

Example Usage:
```java
  @ApiModelProperty(hidden = true) // This marks the property as hidden, and won't be visible in the documentation.
  private UUID id;
  @ApiModelProperty(value = "Item Title", // Brief description of the model property
  notes = "This defines the items title that will be visible in the course UI", // Detailed description of the property
  dataType = "String", // Data type of the property.
  required=true, // Whether the property is required to be set
  allowableValues = "Any non-empty string.  '\n' is not allowed") // Allowable values for the property
  private String title;
```
