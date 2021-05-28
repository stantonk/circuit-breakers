package com.stantonk.testing.resources;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.server.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

@Path("/service")
@Produces(MediaType.APPLICATION_JSON)
public class TestFrontResource {

    private static Logger logger = LoggerFactory.getLogger(TestFrontResource.class);
    private final HttpClient httpClient;

    public TestFrontResource(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @GET
    @Path("/echo")
    public String echo(@QueryParam("input") String input) {
        return input + "\n";
    }

    @GET
    @Path("/proxy")
    public String callServiceB() throws Exception {
        //TODO
        logger.info("start of /proxy");
        String host = System.getenv("BACK_SERVICE_HOSTNAME");
        int port = Integer.parseInt(System.getenv("BACK_SERVICE_PORT"));
        // https://mkyong.com/java/apache-httpclient-examples/
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(100)
                .setConnectTimeout(100)
                .setSocketTimeout(15000) // 15 seconds, we know this service can be slow but we wont wait longer than 15 seconds...
                .build();

        // TODO: when the circuit breaker is open, do requests time out immediately?
        try {
            HttpGet req = new HttpGet(String.format("http://%s:%s/service/slow", host, port));
            req.setConfig(requestConfig);
            long start = System.currentTimeMillis();
            HttpResponse r = httpClient.execute(req);
            long end = System.currentTimeMillis();
            logger.info(String.format("/proxy request took %s ms", (end-start)));
            int statusCode = r.getStatusLine().getStatusCode();
            logger.info(String.valueOf(statusCode));
            Arrays.stream(r.getAllHeaders()).forEach(i -> logger.info(i.toString()));
            String response = EntityUtils.toString(r.getEntity());
            return response;
        } catch (Exception e) {
            logger.error("error: ", e.toString());
            throw e;
        }
    }

        @GET
        @Path("/slow")
        public String route1 () throws InterruptedException {
            Thread.sleep(10000);
            return "finally returning";
        }

    }
