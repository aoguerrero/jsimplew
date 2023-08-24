package onl.andres.jsimplew.app.cntr;

import com.google.gson.Gson;

import onl.andres.jsimplew.app.mdl.Person;
import onl.andres.jsimplew.base.cntr.RestController;
import onl.andres.jsimplew.base.excp.ServiceException;

/**
 * <pre>
 * curl -XPOST -H "Content-type: application/json" -d '{"name":"hello", "age":99}' 'http://localhost:8080/json' {"name":"hello","age":0}
 * </pre>
 */
public class JsonCntr extends RestController {

  public JsonCntr() {
    super(null);
  }

  @Override
  public String executeRest(String body) throws ServiceException, Exception {
    Gson gson = new Gson();
    Person person = gson.fromJson(body, Person.class);
    person.setAge(0);
    return gson.toJson(person);
  }
}
