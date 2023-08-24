package onl.andres.jsimplew.base.mdl;

import io.netty.handler.codec.http.HttpHeaders;

public class Response {

  private final HttpHeaders headers;
  private final byte[] body;

  public Response(HttpHeaders headers, byte[] body) {
    this.headers = headers;
    this.body = body;
  }

  public HttpHeaders getHeaders() {
    return headers;
  }

  public byte[] getBody() {
    return body;
  }
}
