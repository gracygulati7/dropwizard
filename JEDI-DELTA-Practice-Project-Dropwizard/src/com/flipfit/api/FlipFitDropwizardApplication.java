package com.flipfit.api;

import com.flipfit.api.health.FlipFitHealthCheck;
import com.flipfit.rest.LoginController;
import com.flipfit.rest.AdminController;
import com.flipfit.rest.CustomerController;
import com.flipfit.rest.GymOwnerController;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class FlipFitDropwizardApplication extends Application<FlipFitConfiguration> {

    public static void main(String[] args) throws Exception {
        new FlipFitDropwizardApplication().run(args);
    }

    @Override
    public void run(FlipFitConfiguration configuration, Environment environment) {
        environment.healthChecks().register("flipfit", new FlipFitHealthCheck());
        
        // Register REST controllers from rest package
        environment.jersey().register(new LoginController());
        environment.jersey().register(new AdminController());
        environment.jersey().register(new CustomerController());
        environment.jersey().register(new GymOwnerController());
    }
}
