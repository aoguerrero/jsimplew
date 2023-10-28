package onl.andres.jsimplew.app;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import onl.andres.jsimplew.app.cntr.ListCntr;
import onl.andres.jsimplew.app.cntr.LogCntr;
import onl.andres.jsimplew.base.Application;
import onl.andres.jsimplew.base.cntr.BaseController;
import onl.andres.jsimplew.base.utl.ConnectionPool;

public class Main {

  public static void main(String[] args) throws Exception {
    Properties props = new Properties();
    InputStream stream = Main.class.getResourceAsStream("/config.properties");
    props.load(stream);
    ConnectionPool pool = new ConnectionPool(props.getProperty("sqlConnection"));
    Map<String, BaseController> controllers = new HashMap<>();
    controllers.put("/list", new ListCntr("/list.html", pool));
    controllers.put("/log", new LogCntr(pool));
    int port = Integer.valueOf(props.getProperty("port"));
    new Application(controllers, port);
  }
}
