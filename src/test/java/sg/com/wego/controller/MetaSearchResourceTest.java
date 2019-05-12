package sg.com.wego.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import sg.com.wego.entity.Airport;
import sg.com.wego.service.MetaSearchService;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(MetaSearchResource.class)
public class MetaSearchResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MetaSearchService metaSearchService;

    @Test
    public void should_getSuccessful() throws Exception {
        List<Airport> airports = new ArrayList<>();

        //Mockito.when(metaSearchService.getAllAirports()).thenReturn(airports);

        //mockMvc.perform(MockMvcRequestBuilders.get("api/airports")).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}
