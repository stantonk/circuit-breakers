package com.stantonk.testing;

import com.stantonk.testing.resources.TestFrontResource;
import io.dropwizard.Application;
import io.dropwizard.client.HttpClientBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.apache.http.client.HttpClient;

public class testserviceApplication extends Application<testserviceConfiguration> {

    public static void main(final String[] args) throws Exception {
        new testserviceApplication().run(args);
    }

    @Override
    public String getName() {
        return "testservice";
    }

    @Override
    public void initialize(final Bootstrap<testserviceConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final testserviceConfiguration configuration,
                    final Environment environment) {
        // https://www.dropwizard.io/en/latest/manual/client.html
        final HttpClient httpClient = new HttpClientBuilder(environment).using(configuration.getHttpClientConfiguration())
                .build(getName());
        environment.jersey().register(new TestFrontResource(httpClient));
    }

}
