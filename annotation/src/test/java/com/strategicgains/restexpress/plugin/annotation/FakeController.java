package com.strategicgains.restexpress.plugin.annotation;

import java.util.Collections;
import java.util.List;

import org.restexpress.Request;
import org.restexpress.Response;

public class FakeController implements AnnotationController {

    @Route(httpMethod="POST", name="user.create", uri="/user/create.{format}")
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
