package org.ms.testapi.soap.repository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.ms.testapi.springsoap.gen.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CountryRepository {
  private static final Logger LOGGER = LoggerFactory.getLogger(CountryRepository.class);
  private Map<String, Country> map = new HashMap<>();
  
  public Country findCountry(String iso) {
    if(map.isEmpty()) {
      loadFromFile();
    }
    
    return map.get(iso);
  }
  
  public List<Country> getAllCountries() {
    if(map.isEmpty()) {
      loadFromFile();
    }
    
    List<Country> result = new ArrayList<>();
    map.values().forEach(result::add);
    return result;
  }
  
  private void loadFromFile() {
    LOGGER.info("Loading from JSON");
    try {
      Path path = Paths.get(this.getClass().getResource("/get/country.200.json").toURI());
      byte[] contents = Files.readAllBytes(path);
      String strContent = new String(contents, StandardCharsets.UTF_8).trim();
      JSONArray arr = new JSONArray(strContent);
      ObjectMapper mapper = new ObjectMapper();
      for(int i=0 ;i<arr.length();i++) {
        Country c = mapper.readValue(arr.getJSONObject(i).toString(), Country.class);
        map.put(c.getIso2(), c);
      }
    } catch (URISyntaxException | IOException | JSONException e) {
      LOGGER.error(e.getMessage(), e);
    }
    LOGGER.info("Loading from JSON Completed. Final Size: {}" + map.size());
  }
  
  public static void main(String[] args) {
    CountryRepository repo = new CountryRepository();
    repo.loadFromFile();
  }
}
