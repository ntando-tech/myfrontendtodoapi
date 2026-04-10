package todo.backend.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestClientConfig {

    @Bean
            public RestTemplate restTemplate(){
    // Use Apache HttpClient 5 to enable PATCH support
    HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();



        return new RestTemplate(factory);
}
}
