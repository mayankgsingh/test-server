package org.ms.testapi.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("org.ms.testapi")
public class TestServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestServerApplication.class, args);
	}
	
	/*@Bean
  public RequestMappingHandlerAdapter getRequestMappingHandlerAdapter() {
	  MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
	  converter.getObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
    adapter.getMessageConverters().add(converter);
    return adapter;
  }*/
}
