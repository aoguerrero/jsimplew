package onl.andres.jsimplew.base.cntr;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.LastHttpContent;
import onl.andres.jsimplew.base.excp.BadRequestException;
import onl.andres.jsimplew.base.excp.NotFoundException;
import onl.andres.jsimplew.base.mdl.Response;
import onl.andres.jsimplew.base.utl.HttpUtils;

public class ControllerHandler extends SimpleChannelInboundHandler<Object> {

  private static Logger logger = LoggerFactory.getLogger(ControllerHandler.class);

  private Map<String, BaseController> controllers;

  public ControllerHandler(Map<String, BaseController> controllers) {
    this.controllers = controllers;
  }

  private HttpRequest request;
  private byte[] body = new byte[] {};

  @Override
  protected void channelRead0(ChannelHandlerContext context, Object message) {
    try {

      if (message instanceof HttpRequest) {
        request = (HttpRequest) message;

        if (HttpUtil.is100ContinueExpected(request)) {
          FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, CONTINUE, Unpooled.EMPTY_BUFFER);
          context.write(response);
        }
      }

      if (message instanceof HttpContent) {
        HttpContent httpContent = (HttpContent) message;
        try {
          BaseController controller = getController(request.uri());

          ByteBuf byteBuf = httpContent.content();
          byte[] buffer = new byte[byteBuf.readableBytes()];
          byteBuf.readBytes(buffer);
          byte[] joined = new byte[body.length + buffer.length];
          System.arraycopy(body, 0, joined, 0, body.length);
          System.arraycopy(buffer, 0, joined, body.length, buffer.length);
          body = joined;

          if (message instanceof LastHttpContent) {
            Response response = controller.execute(request, body);
            writeOk(context, response.getHeaders(), response.getBody());
          }
        } catch (BadRequestException e) {
          writeError(context, BAD_REQUEST);
        } catch (NotFoundException e) {
          writeError(context, NOT_FOUND);
        }
      }
    } catch (Exception e) {
      logger.error("Unexpected error", e);
      writeError(context, INTERNAL_SERVER_ERROR);
      return;
    }
  }

  private BaseController getController(String uri) throws NotFoundException {
    for (String key : controllers.keySet()) {
      if (uri.startsWith(key)) {
        return controllers.get(key);
      }
    }
    throw new NotFoundException();
  }

  private void writeOk(ChannelHandlerContext context, HttpHeaders headers, byte[] body) {
    FullHttpResponse httpResponse = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.copiedBuffer(body));
    httpResponse.headers().add(headers);
    context.write(httpResponse);
    context.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
  }

  private void writeError(ChannelHandlerContext context, HttpResponseStatus status) {
    try {
      FullHttpResponse httpResponse = new DefaultFullHttpResponse(HTTP_1_1, status,
          Unpooled.copiedBuffer(HttpUtils.getCpContent("/error/" + status.code() + ".html")));
      context.write(httpResponse);
      context.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    } catch (Exception e) {
      logger.error("Exception loading error page", e);
    }
  }
}