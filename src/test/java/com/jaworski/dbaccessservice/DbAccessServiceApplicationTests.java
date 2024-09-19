package com.jaworski.dbaccessservice;

import com.jaworski.dbaccessservice.resources.AppResources;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class DbAccessServiceApplicationTests {

    @Autowired
    private AppResources resources;

    @Test
    void contextLoads() {
        assertNotNull(resources);
    }

}
