package onl.andres.jsimplew.app.cntr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import onl.andres.jsimplew.app.mdl.Log;
import onl.andres.jsimplew.base.cntr.RestController;
import onl.andres.jsimplew.base.excp.BadRequestException;
import onl.andres.jsimplew.base.excp.InternalServerException;
import onl.andres.jsimplew.base.excp.ServiceException;
import onl.andres.jsimplew.base.utl.ConnectionPool;

public class LogCntr extends RestController {

	private static Logger logger = LoggerFactory.getLogger(RestController.class);

	public LogCntr(ConnectionPool pool) {
		super(pool);
	}

	//curl -XPOST -d '{ "title":"Hello", "timestamp":"2023-10-31 10:30:00", "body":"Hello world"}' 'http://localhost:8080/log'
	@Override
	public String executeRest(String body) throws ServiceException, Exception {
		Connection conn = null;
		PreparedStatement st = null;
		try {
			Gson gson = new Gson();
			Log log = gson.fromJson(body, Log.class);
			conn = getPool().getConnection();
			st = conn.prepareStatement("INSERT INTO log (title, ts, body) VALUES (?, ?, ?)");
			st.setString(1, log.getTitle());
			// yyyy-mm-dd hh:mm:ss
			Timestamp ts = Timestamp.valueOf(log.getTimestamp());
			st.setTimestamp(2, ts);
			st.setString(3, log.getBody());
			st.execute();
		} catch (JsonSyntaxException jse) {
			logger.error("Json format exception", jse);
			throw new BadRequestException();
		} catch (Exception e) {
			logger.error("Exception saving the log", e);
			throw new InternalServerException(e);
		} finally {
			if (conn != null)
				conn.close();
			if (st != null)
				st.close();
		}
		return "{\"response\": \"ok\"}";
	}
}
