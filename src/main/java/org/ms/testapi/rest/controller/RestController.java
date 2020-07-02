package org.ms.testapi.rest.controller;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.ms.testapi.rest.service.RestService;
import org.ms.testapi.vo.Payload;
import org.ms.testapi.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Rest controller with limited but dynamic methods to simulate rest server calls.
 * Controller is designed to take in expected response type in headers and return static json data.
 * 
 * @author Mayank
 *
 */
@org.springframework.web.bind.annotation.RestController
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequestMapping(path = "/rest")
public class RestController {
  private static String REQUESTED_RESPONSE_HEADER_NAME = "x-requested-response";
  
  @Autowired
  private RestService service;
  
  @RequestMapping(path = "/{entity}/{idExpr}", method = {GET, POST, DELETE, PATCH, PUT}, produces = "application/json")
  public ResponseEntity<Response> serviceRequest(@RequestHeader Map<String, String> headers,
      @PathVariable String entity, @PathVariable String idExpr, @RequestBody(required = false) String payload,
      HttpServletRequest request) {
    
    final String expectedResponseCode = headers.get(REQUESTED_RESPONSE_HEADER_NAME);
    final HttpStatus httpResponseStatus = getHttpStatus(expectedResponseCode);
    final String method = request.getMethod();
    final String responseContent = service.readFile(method, entity, httpResponseStatus, idExpr);
    final Payload inputPayload = service.preparePayload(request, headers, payload);
    Response body = new Response(responseContent, inputPayload);
    
    return new ResponseEntity<Response>(body, httpResponseStatus);
  }
  
  private HttpStatus getHttpStatus(final String expectedResponseCode) {
    if(expectedResponseCode == null || "".equals(expectedResponseCode.trim())) {
      return HttpStatus.OK;
    }
    
    return HttpStatus.resolve(Integer.parseInt(expectedResponseCode));
  }
  
  
}
