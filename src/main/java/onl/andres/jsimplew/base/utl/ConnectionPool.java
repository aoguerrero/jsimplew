package onl.andres.jsimplew.base.utl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.h2.jdbcx.JdbcConnectionPool;

public class ConnectionPool {

  private final JdbcConnectionPool pool;

  public ConnectionPool(String sqlConnection) {
    this.pool = JdbcConnectionPool.create(sqlConnection, "", "");
  }

  public JdbcConnectionPool getPool() {
    return pool;
  }

  public void close(PreparedStatement ps, Connection conn) throws SQLException {
    if (ps != null)
      ps.close();
    if (conn != null)
      conn.close();
  }

}
