package com.strategicgains.restexpress.plugin.annotation;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restexpress.RestExpress;
import org.restexpress.pipeline.SimpleConsoleLogMessageObserver;

public class AnnotationPluginTest {
   
    private static final int SERVER_PORT = 8888;
    private static final String SERVER_HOST = "http://localhost:" + SERVER_PORT;
  
    private RestExpress server = new RestExpress();
    private HttpClient client = new DefaultHttpClient();
    

    @Before
    public void createServer() {
        RestExpress.getSerializationProvider();
        server = new RestExpress().setName("")
                .setBaseUrl(SERVER_HOST)
                .addMessageObserver(new SimpleConsoleLogMessageObserver());
    }

    @After
    public void shutdownServer() {
        server.shutdown();
    }

    @Test
    public void shouldReturnStatusSuccessOnGET() throws Exception {
        new AnnotationPlugin().scanPackage("com.strategicgains.restexpress.plugin").register(server);
        server.bind(SERVER_PORT);
        
        HttpGet request = new HttpGet(SERVER_HOST + "/users.json");
        HttpResponse response = (HttpResponse) client.execute(request);
        assertEquals(200, response.getStatusLine().getStatusCode());
        
        request.releaseConnection();
    }
    
    @Test
    public void shouldReturnStatusSuccessOnPOST() throws Exception {
        new AnnotationPlugin().scanPackage("com.strategicgains.restexpress.plugin").register(server);
        server.bind(SERVER_PORT);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", "gustavohenrique"));
        HttpPost request = new HttpPost(SERVER_HOST + "/user/create.json");
        request.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
        
        HttpResponse response = (HttpResponse) client.execute(request);
        assertEquals(200, response.getStatusLine().getStatusCode());
        
        request.releaseConnection();
    }

}
