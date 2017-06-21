package com.sobolev.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * CallFireServicePublisher is a class to run REST service described in CallFireController
 * When running, it listens and prints payload coming from CallFire
 */
@SpringBootApplication
public class CallFireServicePublisher {
    public static void main(String[] args) {
        SpringApplication.run(CallFireServicePublisher.class, args);
    }
}
