package org.ms.testapi.soap.repository;

import org.ms.testapi.springsoap.gen.GetAllCountryResponse;
import org.ms.testapi.springsoap.gen.GetCountryRequest;
import org.ms.testapi.springsoap.gen.GetCountryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class CountryEndpoint {
  private static final String NAMESPACE_URI = "http://www.testserver.com/springsoap/gen";

  private CountryRepository countryRepository;

  @Autowired
  public CountryEndpoint(CountryRepository countryRepository) {
    this.countryRepository = countryRepository;
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCountryRequest")
  @ResponsePayload
  public GetCountryResponse getCountry(@RequestPayload GetCountryRequest request) {
    GetCountryResponse response = new GetCountryResponse();
    response.setCountry(countryRepository.findCountry(request.getName()));

    return response;
  }
  
  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllCountryRequest")
  @ResponsePayload
  public GetAllCountryResponse getCountry() {
    GetAllCountryResponse response = new GetAllCountryResponse();
    countryRepository.getAllCountries().forEach(response.getCountry()::add);
    return response;
  }
}
