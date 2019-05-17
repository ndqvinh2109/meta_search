package sg.com.wego.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sg.com.wego.service.SearchEngine;
import sg.com.wego.service.SearchEngineFactory;
import sg.com.wego.service.SearchEngineType;

@Configuration
public class BeanConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public SearchEngine searchEngine() {
        return SearchEngineFactory.getSearchEngine(SearchEngineType.FLIGHT);
    }

}
