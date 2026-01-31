package com.flipfit;

import com.flipfit.rest.HelloController;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import io.dropwizard.Configuration;

public class App extends Application<Configuration> {

    public static void main(String[] args) throws Exception {
        new App().run(args);
    }

    @Override
    public void run(Configuration configuration, Environment environment) {
        environment.jersey().register(new HelloController());
    }
}
