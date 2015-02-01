package cn.hm.psapp.qgform.format;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cn.hm.psapp.qgform.Form;
import cn.hm.psapp.qgform.FormReader;
import cn.hm.psapp.qgform.FormSender;
import cn.hm.psapp.qgform.Table;
import cn.hm.psapp.qgform.config.ConfigConstant;
import cn.hm.psapp.qgform.dao.AWSDao;
import cn.hm.psapp.qgform.db.DBUtil;
import cn.hm.psapp.qgform.reader.AWSFormReader;
import cn.hm.psapp.qgform.sender.AWSHttpFormWrtier;
import cn.hm.psapp.qgform.util.AWSHttpUtils;

public class FieldControlFormatTest {

  private FieldControlFormat format = new FieldControlFormat();
  private FormReader reader = new AWSFormReader();
  private AWSDao dao = new AWSDao();
  private FormSender sender = new AWSHttpFormWrtier();

  @Before
  public void init() {
    try {
      String sid = AWSHttpUtils.getSid("http://10.10.10.201:8089/workflow/login.wf", "admin", "aklhello123,.");
      ConfigConstant.sid = sid;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 自动调节表单字段长度.
   */
  @Test
  public void testFormat() {
    Form form = reader.read("42e5d26e59006ba60c22c7420f82ae20");
    format.format(form);
    Connection conn = null;

    try {
      conn = DBUtil.open();
      conn.setAutoCommit(false);

      dao.updateFields(conn, form.getMainTable().getFieldList());

      List<Table> tables = form.getTables();
      for (Table table : tables) {
        dao.updateFields(conn, table.getFieldList());
      }

      conn.commit();
    } catch (SQLException e) {
      DBUtil.rollback(conn);
      throw new RuntimeException(e);
    } finally {
      DBUtil.close(conn);
    }
  }

  /**
   * 自动调节表单字段长度.
   */
  @Test
  public void testBuilderHtmlAndFormat() {
    Form form = reader.read("42e5d26e591d1d1c4628dd0ccb68c0f4");
    format.format(form);

    sender.send(form);

    Connection conn = null;

    try {
      conn = DBUtil.open();
      conn.setAutoCommit(false);

      dao.updateFields(conn, form.getMainTable().getFieldList());

      List<Table> tables = form.getTables();
      for (Table table : tables) {
        dao.updateFields(conn, table.getFieldList());
      }

      conn.commit();
    } catch (SQLException e) {
      DBUtil.rollback(conn);
      throw new RuntimeException(e);
    } finally {
      DBUtil.close(conn);
    }
  }
}
