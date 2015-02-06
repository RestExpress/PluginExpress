package com.strategicgains.restexpress.plugin.annotation;

import java.util.List;

import org.restexpress.Request;
import org.restexpress.Response;

public interface AnnotationController {

    public Object create(Request request, Response response);

    public Object read(Request request, Response response);

    public List<Object> readAll(Request request, Response response);

    public void update(Request request, Response response);

    public void delete(Request request, Response response);
}
