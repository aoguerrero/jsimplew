package onl.andres.jsimplew.app;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import onl.andres.jsimplew.app.cntr.HelloWorldCntr;
import onl.andres.jsimplew.app.cntr.JsonCntr;
import onl.andres.jsimplew.base.Application;
import onl.andres.jsimplew.base.cntr.BaseController;
import onl.andres.jsimplew.base.utl.ConnectionPool;

public class Main {

  public static void main(String[] args) throws Exception {
    Properties props = new Properties();
    InputStream stream = Main.class.getResourceAsStream("/config.properties");
    props.load(stream);
//    ConnectionPool pool = new ConnectionPool(props.getProperty("sqlConnection"));
    Map<String, BaseController> controllers = new HashMap<>();
    controllers.put("/hello", new HelloWorldCntr("/helloworld.html"));
    controllers.put("/json", new JsonCntr());
    int port = Integer.valueOf(props.getProperty("port"));
    new Application(controllers, port);
  }
}
