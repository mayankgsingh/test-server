package org.ms.testapi.vo;

import java.util.Collections;
import java.util.Map;

public class Payload {
  private final String url;
  private final String httpMethod;
  private final Map<String, String> headers;
  private final String body;

  public Payload(final String url, final String httpMethod, final Map<String, String> headers, final String body) {
    super();
    this.url = url;
    this.httpMethod = httpMethod;
    this.headers = Collections.unmodifiableMap(headers);
    this.body = body;
  }

  public String getUrl() {
    return url;
  }

  public String getHttpMethod() {
    return httpMethod;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public String getBody() {
    return body;
  }

}
