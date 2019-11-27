package demo.reactAdmin;

import demo.reactAdmin.crud.services.DataInitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import springboot.rest.providers.ObjectMapperProvider;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;

@SpringBootApplication(scanBasePackages = {"demo.reactAdmin", "springboot.rest"})
@EnableSwagger2
public class ReactAdminDemoApplication extends WebMvcAutoConfiguration {

    @Autowired
    private DataInitService dataInitService;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private ObjectMapperProvider objMapperProvider;

    public static void main(String[] args) {
        SpringApplication.run(ReactAdminDemoApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void afterReady() {
        dataInitService.init();
    }

    @Bean
    public ViewResolver internalResourceViewResolver() {
        InternalResourceViewResolver bean = new InternalResourceViewResolver();
        bean.setPrefix("/uploaded/");
        return bean;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return objMapperProvider.getObjectMapper();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .pathProvider(new RelativePathProvider(servletContext) {
                    @Override
                    public String getApplicationBasePath() {
                        return "/api/v1";
                    }
                })
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

}
