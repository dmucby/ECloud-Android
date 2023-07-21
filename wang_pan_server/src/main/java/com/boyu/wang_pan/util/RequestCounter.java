package com.boyu.wang_pan.util;

import java.util.concurrent.atomic.AtomicLong;


public class RequestCounter {

    private static final RequestCounter REQUEST_COUNTER = new RequestCounter();

    private RequestCounter() {}

    public static RequestCounter getInstance() {
        return REQUEST_COUNTER;
    }

    private final AtomicLong requestCount = new AtomicLong(0);
    private final AtomicLong successCount = new AtomicLong(0);
    private final AtomicLong failureCount = new AtomicLong(0);

    public void newRequestReceive() {
        requestCount.incrementAndGet();
    }

    public void requestSuccess() {
        successCount.incrementAndGet();
    }

    public void requestFailure() {
        failureCount.incrementAndGet();
    }

    public Long getRequestCount() {
        return requestCount.get();
    }

    public Long getRequestSuccessCount() {
        return successCount.get();
    }

    public Long getRequestFailureCount() {
        return failureCount.get();
    }
}
