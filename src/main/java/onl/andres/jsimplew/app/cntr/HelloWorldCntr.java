package onl.andres.jsimplew.app.cntr;

import org.apache.velocity.VelocityContext;

import onl.andres.jsimplew.base.cntr.ContentController;

public class HelloWorldCntr extends ContentController {

  public HelloWorldCntr(String path) {
    super(path, null);
  }

  @Override
  public VelocityContext getContext() {
    VelocityContext context = new VelocityContext();
    context.put("message", "hello world");
    return context;
  }

}
