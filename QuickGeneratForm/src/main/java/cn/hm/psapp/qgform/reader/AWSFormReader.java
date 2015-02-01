package cn.hm.psapp.qgform.reader;

import java.sql.Connection;
import java.sql.SQLException;

import cn.hm.psapp.qgform.Form;
import cn.hm.psapp.qgform.FormReader;
import cn.hm.psapp.qgform.dao.AWSDao;
import cn.hm.psapp.qgform.db.DBUtil;

/**
 * AWS FORM表单读取.
 * 
 * @author huangming
 *
 */
public class AWSFormReader implements FormReader {

  private AWSDao dao = new AWSDao();

  @Override
  public Form read(String formid) {
    Connection conn = null;
    try {
      conn = DBUtil.open();
      return dao.queryForm(conn, formid);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      DBUtil.close(conn);
    }
  }

}
