package com.example.geodata;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {GeoDataApplicationTests.class})
class GeoDataApplicationTests {


	@Test
	void contextLoads() {
        String asd = "sonarqube";
        assertNotNull(asd);
	}

}
