package cn.hm.psapp.qgform.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cn.hm.psapp.qgform.config.ConfigurationFactory;

public class DBUtil {

  private DBUtil() {
  }

  static {
    try {
      Class.forName(ConfigurationFactory.loadJson().getJdbcDriver());
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public static Connection open() throws SQLException {
    return DriverManager.getConnection(ConfigurationFactory.loadJson().getJdbcUrl(), ConfigurationFactory.loadJson().getJdbcUsername(), ConfigurationFactory
            .loadJson().getJdbcPassword());
  }

  public static ResultSet executeQuery(PreparedStatement ps, Object... objs) throws SQLException {
    for (int i = 0; i < objs.length; i++) {
      ps.setObject(i + 1, objs[i]);
    }
    return ps.executeQuery();
  }

  public static void executeAddBatch(PreparedStatement ps, Object... objs) throws SQLException {
    for (int i = 0; i < objs.length; i++) {
      ps.setObject(i + 1, objs[i]);
    }
    ps.addBatch();
  }

  public static int executeUpdate(PreparedStatement ps, Object... objs) throws SQLException {
    for (int i = 0; i < objs.length; i++) {
      ps.setObject(i + 1, objs[i]);
    }
    return ps.executeUpdate();
  }

  public static void rollback(Connection conn) {
    try {
      if (conn != null) {
        conn.rollback();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static void close(Connection conn) {
    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public static void close(Statement stat) {
    if (stat != null) {
      try {
        stat.close();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public static void close(Statement stat, ResultSet result) {
    if (result != null) {
      try {
        result.close();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public static void close(Connection conn, Statement stat, ResultSet result) {
    if (result != null) {
      try {
        result.close();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
    if (stat != null) {
      try {
        stat.close();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
  }

}
