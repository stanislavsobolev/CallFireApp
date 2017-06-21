package com.sobolev.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class CallFireController {
    @RequestMapping(value = "/subscribe", method = RequestMethod.POST )
    public void greeting(@RequestBody String notification) throws IOException {
        System.out.println(notification);
    }
}
