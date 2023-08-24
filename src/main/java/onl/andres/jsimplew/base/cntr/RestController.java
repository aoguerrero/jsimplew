package onl.andres.jsimplew.base.cntr;

import java.nio.charset.StandardCharsets;

import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import onl.andres.jsimplew.base.excp.ServiceException;
import onl.andres.jsimplew.base.mdl.Response;
import onl.andres.jsimplew.base.utl.ConnectionPool;
import onl.andres.jsimplew.base.utl.HttpUtils;

public abstract class RestController implements BaseController {

  private ConnectionPool pool;
  private HttpRequest request;

  public RestController() {
  }

  public RestController(ConnectionPool pool) {
    this.pool = pool;
  }

  public Response execute(HttpRequest request, byte[] body) throws ServiceException, Exception {
    this.request = request;
    HttpHeaders headers = new DefaultHttpHeaders();
    headers.add(HttpUtils.CONTENT_TYPE, HttpUtils.JSON_CONTENT_TYPE);
    return new Response(headers,
        executeRest(new String(body, StandardCharsets.UTF_8)).getBytes(StandardCharsets.UTF_8));
  }

  public abstract String executeRest(String body) throws ServiceException, Exception;

  public HttpRequest getRequest() {
    return request;
  }

  protected ConnectionPool getPool() {
    return pool;
  }
}
