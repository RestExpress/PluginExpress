package com.strategicgains.restexpress.plugin.annotation;

import io.netty.handler.codec.http.HttpMethod;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import net.sf.extcos.ComponentQuery;
import net.sf.extcos.ComponentScanner;

import org.restexpress.RestExpress;
import org.restexpress.plugin.AbstractPlugin;

public class AnnotationPlugin extends AbstractPlugin {
    
    private RestExpress server;
    private Map<String, AnnotationController> controllers = new TreeMap<String, AnnotationController>();
    private String packageName = "br.com";

    
    public AnnotationPlugin scanPackage(final String pkg) {
        this.packageName = pkg;
        return this;
    }
    
    public AnnotationPlugin register(RestExpress server) {
        this.server = server;
        ComponentScanner scanner = new ComponentScanner();
        Set<Class<?>> classes = scanner.getClasses(new ComponentQuery() {
            protected void query() {
                select().from(packageName).returning(allImplementing(AnnotationController.class));
            }
        });
        for (Class clazz: classes) {
            createFor(clazz);
        }
        return this;
    }
    
    private void createFor(Class clazz) {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            Route route = method.getAnnotation(Route.class);
            if (route != null) {
                addRoute(clazz, method, route);
            }
        }
    }

    private void addRoute(Class clazz, Method method, Route route) {
        try {
            String className = clazz.getName();
            if (! controllers.containsKey(className)) {
                AnnotationController obj = (AnnotationController) Class.forName(className).newInstance();
                controllers.put(className, obj);
            }
            server.uri(route.uri(), controllers.get(className))
                .action(method.getName(), HttpMethod.valueOf(route.httpMethod()))
                .name(route.name());
        }
        catch (Exception e) {
        }
    }
}
