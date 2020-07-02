package org.ms.testapi.rest.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ms.testapi.vo.Payload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Scope(scopeName=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RestService {

  private static final Logger LOGGER = LoggerFactory.getLogger(RestService.class);
  
  /*
   * RESPONSE_FILE_FORMAT format.
   * 
   * Format: /<HTTP_METHOD/<entity>.<REQUESTED_REPONSE_CODE.json>
   * if REQUESTED_REPONSE_CODE is not available then 200 will be used as default.
   *  
   * eg: /get/employee.200.json
   */
  private static final String RESPONSE_FILE_FORMAT = "/%s/%s.%d.json";
  
  /**
   * Read pre-configured response file.
   * 
   * RESPONSE_FILE_FORMAT format.
   * 
   * Format: /<HTTP_METHOD/<entity>.<REQUESTED_REPONSE_CODE.json>
   * if REQUESTED_REPONSE_CODE is not available then 200 will be used as default.
   *  
   * Example: /get/employee.200.json
   *
   * @param method
   * @param entity
   * @param status
   * @param idExpr
   * @return String
   */
  public String readFile(final String method, final String entity, final HttpStatus status, final String idExpr) {
    byte[] contents = null;
    JSONObject jsonResult = null;
    JSONObject jsonObj = null;
    JSONArray jsonArr = null;
    boolean isArray = false;
    
    try {
      Path path = Paths.get(this.getClass().getResource(String.format(RESPONSE_FILE_FORMAT, method.toLowerCase(), entity, status.value())).toURI());
      contents = Files.readAllBytes(path);
      String strContent = new String(contents, StandardCharsets.UTF_8).trim();
      
      if(strContent.charAt(0) == '[') {
        jsonArr = new JSONArray(strContent);
        isArray = true;
      } else {
        jsonObj = new JSONObject(strContent);
      }
      
      jsonResult = getPartJsonFromPayload(jsonObj, jsonArr, isArray, idExpr);
      
      
    } catch (IOException | URISyntaxException | JSONException e) {
      LOGGER.error(e.getMessage(), e);
      jsonResult = new JSONObject();
      try {
        jsonResult.put("error", e.getMessage());
      } catch (JSONException je) {
        LOGGER.error(je.getMessage(), je);
      }
    }
    
    return jsonResult.toString();
  }
  
  private JSONObject getPartJsonFromPayload(final JSONObject jsonObj, final JSONArray jsonArray, boolean isArray,
      final String idExpr) {
    JSONObject result = null;

    if (idExpr == null || (jsonObj == null && jsonArray == null)) {
      return null; // fail fast
    }

    try {
      if (idExpr.contains("=")) {
        String keyVal[] = idExpr.split("=");
        boolean isValNumber = isInteger(keyVal[1]);

        // int idx = Integer.parseInt();

        for (int i = 0; i < jsonArray.length(); i++) {
          JSONObject o = jsonArray.getJSONObject(i);
          String val = isValNumber ? (o.getInt(keyVal[0]) + "") : o.getString(keyVal[0]);

          if (val.equals(keyVal[1])) {
            result = o;
            break;
          }
        }
      } else {
        int idx = Integer.parseInt(idExpr);
        result = isArray ? jsonArray.getJSONObject(idx) : jsonObj;
      }
    } catch (JSONException ex) {
      LOGGER.error(ex.getMessage(), ex);
    }

    return result;
  }
  /**
   * Create incoming payload object.
   * 
   * @param httpRequest
   * @param headers
   * @param payload
   * @return
   */
  public Payload preparePayload(final HttpServletRequest httpRequest, final Map<String, String> headers, final String payload) {
    return new Payload(httpRequest.getRequestURI(), httpRequest.getMethod(), headers, payload);
  }
  
  private static boolean isInteger(final String str) {
    boolean isChar = true;
    isChar = str.chars().filter(c -> !(c>=48 && c<=57)).count()>0;
    LOGGER.info("Str/Result: {}/{}", str, isChar);
    return !isChar;
  }
}
