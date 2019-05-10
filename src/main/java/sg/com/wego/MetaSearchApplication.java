package sg.com.wego;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.WebApplicationInitializer;


@SpringBootApplication
public class MetaSearchApplication  {
    public static void main(String[] args) {
        SpringApplication.run(MetaSearchApplication.class, args);
    }
}
