package sg.com.wego.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sg.com.wego.service.searchable.SearchEngine;
import sg.com.wego.service.searchable.SearchEngineFactory;
import sg.com.wego.service.searchable.SearchEngineType;

@Configuration
public class BeanConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public SearchEngine flightSearchEngineBean() {
        return SearchEngineFactory.getSearchEngine(SearchEngineType.FLIGHT);
    }

}
