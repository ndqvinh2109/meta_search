package sg.com.wego.controller;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import sg.com.wego.MetaSearchApplication;
import sg.com.wego.model.Airport;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MetaSearchApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MetaSearchResourceHTTPTest {

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @Test
    public void should_ca() {

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<List<Airport>> response = restTemplate.exchange(
                createURLWithPort("/api/airports"),
                HttpMethod.GET, entity, new ParameterizedTypeReference<List<Airport>>(){});

        List<Airport> airports = response.getBody();

        Assert.assertThat(100, Matchers.is(airports.size()));

    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
