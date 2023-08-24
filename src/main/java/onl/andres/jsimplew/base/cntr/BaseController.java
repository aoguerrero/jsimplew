package onl.andres.jsimplew.base.cntr;

import io.netty.handler.codec.http.HttpRequest;
import onl.andres.jsimplew.base.excp.ServiceException;
import onl.andres.jsimplew.base.mdl.Response;

public interface BaseController {

  public Response execute(HttpRequest request, byte[] body) throws ServiceException, Exception;

}
