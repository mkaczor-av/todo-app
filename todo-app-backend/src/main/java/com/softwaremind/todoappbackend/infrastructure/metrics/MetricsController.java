package com.softwaremind.todoappbackend.infrastructure.metrics;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.Map;
import java.util.Objects;

@RestController
class MetricsController {

    private final RequestCounter requestCounter;

    public MetricsController(RequestCounter requestCounter) {
        this.requestCounter = Objects.requireNonNull(requestCounter);
    }

    @GetMapping("metrics")
    Map<String, BigInteger> metrics() {
        return requestCounter.metrics();
    }
}
