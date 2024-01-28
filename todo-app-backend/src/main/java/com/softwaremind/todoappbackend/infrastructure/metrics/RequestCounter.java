package com.softwaremind.todoappbackend.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RequestCounter {

    private final Map<String, Counter> counters = new HashMap<>();

    RequestCounter(MeterRegistry meterRegistry) {
        for (var counterType : CounterType.values()) {
            initializeCounter(meterRegistry, counterType);
        }
    }

    public void recordRequest(CounterType counterType) {
        counters.get(counterType.getCounterName()).increment();
    }

    public Map<String, BigInteger> metrics() {
        return counters.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> BigInteger.valueOf((long) entry.getValue().count())));
    }

    private void initializeCounter(MeterRegistry meterRegistry, CounterType counterType) {
        counters.put(counterType.getCounterName(), meterRegistry.counter(counterType.getCounterName()));
    }

    public enum CounterType {
        CREATE_TODO("createTodoCounter"),
        UPDATE_TODO("updateTodoCounter"),
        DELETE_TODO("deleteTodoCounter"),
        FIND_TODO("findTodoCounter"),
        FIND_TODOS("findTodosCounter");

        private final String counterName;

        CounterType(String counterName) {
            this.counterName = counterName;
        }

        public String getCounterName() {
            return counterName;
        }
    }
}
