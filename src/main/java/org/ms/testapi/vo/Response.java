package org.ms.testapi.vo;

/**
 * Value object for REST response.
 * 
 * @author Mayank
 *
 */

public class Response {
  private final String response;
  private final Payload requestPayload;

  public Response(String response, Payload requestPayload) {
    super();
    this.response = response;
    this.requestPayload = requestPayload;
  }

  public String getResponse() {
    return response;
  }

  public Payload getRequestPayload() {
    return requestPayload;
  }

}
