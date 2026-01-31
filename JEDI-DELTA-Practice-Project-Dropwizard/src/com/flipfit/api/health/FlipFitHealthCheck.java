package com.flipfit.api.health;

import com.codahale.metrics.health.HealthCheck;

public class FlipFitHealthCheck extends HealthCheck {
    @Override
    protected Result check() {
        return Result.healthy("FlipFit API is running");
    }
}
