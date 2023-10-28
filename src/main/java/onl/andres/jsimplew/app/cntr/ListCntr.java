package onl.andres.jsimplew.app.cntr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonSyntaxException;

import onl.andres.jsimplew.app.mdl.Log;
import onl.andres.jsimplew.base.cntr.ContentController;
import onl.andres.jsimplew.base.excp.BadRequestException;
import onl.andres.jsimplew.base.excp.InternalServerException;
import onl.andres.jsimplew.base.excp.ServiceException;
import onl.andres.jsimplew.base.utl.ConnectionPool;

public class ListCntr extends ContentController {

	private static Logger logger = LoggerFactory.getLogger(ListCntr.class);

	public ListCntr(String path, ConnectionPool pool) {
		super(path, pool);
	}

	@Override
	public VelocityContext getContext() throws ServiceException, Exception {
		VelocityContext context = new VelocityContext();
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = getPool().getConnection();
			st = conn.prepareStatement("SELECT title, ts, body FROM log ORDER BY ts DESC LIMIT 25");
			rs = st.executeQuery();
			List<Log> logs = new ArrayList<Log>();
			while(rs.next()) {
				Log log = new Log();
				log.setTitle(rs.getString(1));
				log.setTimestamp(rs.getTimestamp(2).toString());
				log.setBody(rs.getString(3));
				logs.add(log);
			}
			context.put("logs", logs);
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
		return context;
	}

}
