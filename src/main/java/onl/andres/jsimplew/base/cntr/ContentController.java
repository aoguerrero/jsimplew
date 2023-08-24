package onl.andres.jsimplew.base.cntr;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import onl.andres.jsimplew.base.excp.ServiceException;
import onl.andres.jsimplew.base.mdl.Response;
import onl.andres.jsimplew.base.utl.ConnectionPool;
import onl.andres.jsimplew.base.utl.HttpUtils;

public abstract class ContentController implements BaseController {

  private final String path;
  private ConnectionPool pool;

  private HttpRequest request;

  public ContentController(String path) {
    this.path = path;
  }

  public ContentController(String path, ConnectionPool pool) {
    this.path = path;
    this.pool = pool;
  }

  public Response execute(HttpRequest request, byte[] body) throws ServiceException, Exception {
    this.request = request;
    HttpHeaders headers = new DefaultHttpHeaders();
    headers.add(HttpUtils.CONTENT_TYPE, HttpUtils.HTML_CONTENT_TYPE);
    byte[] template = HttpUtils.getCpContent("/tmpl" + path);
    StringWriter writer = new StringWriter();
    Velocity.evaluate(getContext(), writer, "", new String(template, StandardCharsets.UTF_8));
    return new Response(headers, writer.toString().getBytes(StandardCharsets.UTF_8));
  }

  public abstract VelocityContext getContext();

  public HttpRequest getRequest() {
    return request;
  }

  protected ConnectionPool getPool() {
    return pool;
  }
}
