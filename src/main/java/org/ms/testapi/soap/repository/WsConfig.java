package org.ms.testapi.soap.repository;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WsConfig extends WsConfigurerAdapter {
  @Bean
  public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
    MessageDispatcherServlet servlet = new MessageDispatcherServlet();
    servlet.setApplicationContext(applicationContext);
    servlet.setTransformWsdlLocations(true);
    return new ServletRegistrationBean(servlet, "/ws/*");
  }
  
  @Bean(name = "country")
  public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema countriesSchema) {
      DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
      wsdl11Definition.setPortTypeName("CountriesPort");
      wsdl11Definition.setLocationUri("/ws");
      wsdl11Definition.setTargetNamespace("http://www.testserver.com/springsoap/gen");
      wsdl11Definition.setSchema(countriesSchema);
      return wsdl11Definition;
  }
   
  @Bean
  public XsdSchema countrySchema() {
      return new SimpleXsdSchema(new ClassPathResource("country.xsd"));
  }
}
