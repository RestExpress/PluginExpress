Swagger Plugin
==============

Based on [OpenAPI Specification 3.0.0](https://swagger.io/specification/#oasDocument)

fixed files for swagger-annotations are in io.swagger.oas.annotations (Hidden + Operation)

How to use in RestExpress

Example of OpenApiConfig.java for your Project :
```
import com.strategicgains.restexpress.plugin.swagger.wrapper.Components;
import com.strategicgains.restexpress.plugin.swagger.wrapper.Contact;
import com.strategicgains.restexpress.plugin.swagger.wrapper.Info;
import com.strategicgains.restexpress.plugin.swagger.wrapper.License;
import com.strategicgains.restexpress.plugin.swagger.wrapper.OpenApi;
import com.strategicgains.restexpress.plugin.swagger.wrapper.SecurityScheme;
import com.strategicgains.restexpress.plugin.swagger.wrapper.Server;
import com.strategicgains.restexpress.plugin.swagger.wrapper.ServerVariable;

public class OpenApiConfig {
	public static OpenApi get() {
		Info info = new Info("cellypso REST API", "This is a your app server.", "1.0.0");
        info.setLicense(new License("Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0.html"));
        info.setTermsOfService("https://path.to.your/terms/");
        info.setContact(new Contact("API Support", "https://path.to.your/support", "apisupport@your.domain"));
        
        OpenApi api = new OpenApi(info);
        
        {
        	Server s = new Server("http://staging.your.test:{port}/{basePath}","Staging server");
			ServerVariable sv1 = new ServerVariable("80");
			sv1.addEnum("80");
//			sv1.addEnum("443");
			s.addVariable("port", sv1);
			s.getVariables().put("port", sv1);
			ServerVariable sv2 = new ServerVariable("v1");
			sv2.addEnum("v1");
			s.getVariables().put("basePath", sv2);
			api.getServers().add(s);
		}
        {
	        	Server s = new Server("https://your.server.com:{port}/{basePath}","Production server");
	        	ServerVariable sv1 = new ServerVariable("443");
	        	sv1.addEnum("443");
	        	s.addVariable("port", sv1);
	        	s.getVariables().put("port", sv1);
	        	ServerVariable sv2 = new ServerVariable("v1");
	        	sv2.addEnum("v1");
	        	s.getVariables().put("basePath", sv2);
	        	api.getServers().add(s);
        }
        api.setComponents(new Components());
        
        SecurityScheme securityScheme = new SecurityScheme();
        securityScheme.setDescription("Authorization header using the Bearer schema. Example: Authorization: Bearer {token}");
        securityScheme.setName("Authorization");
        securityScheme.setIn("header");
        securityScheme.setType("http");
        securityScheme.setScheme("bearer");
        securityScheme.setBearerFormat("JWT");

        api.getComponents().getSecuritySchemes().put("Authorization", securityScheme);
        
        
        return api;
	}
}
```
in your restexpress config 
```
private static void configurePlugins(Configuration config, RestExpress server) {
//	....       
        
        new SwaggerPlugin("/api-docs", OpenApiConfig.get())
//        		.setDefaultToHidden(true)
        		.setBasePath("/v1")
                .flag(Flags.Auth.PUBLIC_ROUTE)
                .register(server);

//	....    
    }
    ```
    
 add OpenApi Annotation to your controllers
```
	@Operation(
		tags = {"HR :: Employees"},
		summary = "Search employee by firstname || lastname",
        description = "",
        security = @SecurityRequirement(name = "Authorization"),
        parameters = {
        		@Parameter(name="filter", in="query", description="filter", required=true),
        		@Parameter(name="table", in="query", description="as table"),
        		@Parameter(name="countOnly", in="query", description="count only")
        },
        responses = {
        		@ApiResponse(responseCode="200", content=@Content(mediaType = "application/json", schema=@Schema(implementation=TestRS.class)))
        }
	)
	public void searchEmployee(Request request, Response response) {
	.... 
```

that's all