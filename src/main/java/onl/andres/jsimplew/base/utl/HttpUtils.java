package onl.andres.jsimplew.base.utl;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {

  public static final String CONTENT_TYPE = "Content-Type";
  public static final String SET_COOKIE = "Set-Cookie";
  public static final String COOKIE = "Cookie";
  public static final String JSON_CONTENT_TYPE = "application/json; charset=utf-8";
  public static final String HTML_CONTENT_TYPE = "text/html; charset=utf-8";

  public static byte[] getCpContent(String path) throws IOException {
    return HttpUtils.class.getResourceAsStream(path).readAllBytes();
  }

  public static Map<String, String> bodyToForm(byte[] body) {
    return bodyToForm(new String(body, StandardCharsets.UTF_8));
  }

  public static Map<String, String> bodyToForm(String body) {
    Map<String, String> result = new HashMap<>();
    if (body != null) {
      String[] rows = body.split("&");
      for (String row : rows) {
        String[] keyValue = row.split("=");
        result.put(URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8),
            URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8));
      }
    }
    return result;
  }

  public static Map<String, String> cookiesToMap(String cookieStr) {
    Map<String, String> result = new HashMap<>();
    if (cookieStr != null) {
      String[] rows = cookieStr.split(";");
      for (String row : rows) {
        String[] keyValue = row.split("=");
        result.put(keyValue[0].trim(), keyValue[1].trim());
      }
    }
    return result;
  }
}
