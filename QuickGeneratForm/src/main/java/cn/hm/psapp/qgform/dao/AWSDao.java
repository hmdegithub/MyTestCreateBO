package cn.hm.psapp.qgform.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.hm.psapp.qgform.Field;
import cn.hm.psapp.qgform.Form;
import cn.hm.psapp.qgform.Table;
import cn.hm.psapp.qgform.db.DBUtil;

public class AWSDao {

  private static final String UPDATE_FIELD = "UPDATE SYS_BUSINESS_METADATA_MAP SET DISPLAY_WIDTH=?, INPUTWIDTH=?, INPUTHEIGHT=?, HTML_INNER=?, DISPLAY_SQL=? WHERE ID=?";

  public void updateFields(Connection conn, List<Field> fieldList) throws SQLException {
    PreparedStatement ps = null;
    ps = conn.prepareStatement(UPDATE_FIELD);
    try {
      for (Field field : fieldList) {
        int updateCount = DBUtil.executeUpdate(ps, field.getDisplayWidth(), field.getInputWidth(), field.getInputHeight(), field.getHtmlInner(),
                field.getDisplaySql(), field.getId());
        if (updateCount != 1) {
          throw new RuntimeException("字段信息更新失败! " + field);
        }
      }
    } finally {
      DBUtil.close(ps);
    }
  }

  public void updateFields(Connection conn, Field field) throws SQLException {
    PreparedStatement ps = null;
    try {
      ps = conn.prepareStatement(UPDATE_FIELD);
      int updateCount = DBUtil.executeUpdate(ps, field.getDisplayWidth(), field.getInputWidth(), field.getInputHeight(), field.getHtmlInner(),
              field.getDisplaySql(), field.getId());
      if (updateCount != 1) {
        throw new RuntimeException("字段信息更新失败! " + field);
      }
    } finally {
      DBUtil.close(ps);
    }

  }

  /**
   * 获取Form对象.
   * 
   * @param conn
   * @param formid
   * @return
   * @throws SQLException
   */
  public Form queryForm(Connection conn, String formid) throws SQLException {

    if (formid == null || formid.equals("")) {
      return null;
    }

    Form form = new Form();
    PreparedStatement ps = null;
    ResultSet reset = null;
    try {
      ps = conn.prepareStatement("SELECT ID, REPORT_TITLE, GROUP_NAME FROM SYS_WF_REPORT WHERE UUID=?");
      reset = DBUtil.executeQuery(ps, formid);

      while (reset.next()) {
        form.setName(reset.getString("REPORT_TITLE"));
        form.setId(reset.getInt("ID"));
        form.setUuid(formid);
      }
    } finally {
      DBUtil.close(ps, reset);
    }

    queryTables(conn, form);
    return form;
  }

  /**
   * 查询表单附属的Table对象.
   * 
   * @param conn
   * @param form
   * @return
   * @throws SQLException
   */
  public List<Table> queryTables(Connection conn, Form form) throws SQLException {

    List<Table> tables = new ArrayList<Table>(5);
    if (form == null) {
      return tables;
    }

    PreparedStatement ps = null;
    ResultSet reset = null;
    try {
      ps = conn.prepareStatement("SELECT SHEET_TITLE, METADATA_ID, MODEL_NAME, REPORT_ID, METADATA_UUID, ISSUBSHEET FROM SYS_WF_SHEET WHERE REPORT_UUID=?");
      reset = DBUtil.executeQuery(ps, form.getUuid());
      while (reset.next()) {
        Table table = new Table();
        table.setId(reset.getInt("METADATA_ID"));
        table.setTableName(reset.getString("METADATA_UUID"));
        table.setTableTitle(reset.getString("SHEET_TITLE"));
        queryFields(conn, table);
        if (reset.getInt("ISSUBSHEET") == Field.YES) {
          tables.add(table);
        } else {
          form.setMainTable(table);
          form.setModelName(reset.getString("MODEL_NAME"));
        }
      }
    } finally {
      DBUtil.close(ps, reset);
    }

    form.setTables(tables);
    return tables;
  }

  /**
   * 查询Table中的字段信息.
   * 
   * @param conn
   * @param table
   * @return
   * @throws SQLException
   */
  public List<Field> queryFields(Connection conn, Table table) throws SQLException {

    List<Field> tables = new ArrayList<Field>(150);

    if (table == null) {
      return tables;
    }

    PreparedStatement ps = null;
    ResultSet reset = null;
    try {
      ps = conn
              .prepareStatement("SELECT ID,FIELD_NAME,FIELD_TITLE, FIELD_TYPE, FIELD_NOTNULL, FIELD_DEFAULT, DISPLAY_WIDTH, DISPLAY_TYPE, DISPLAY_SQL, ISDISPLAY, ISDELETE, INPUTWIDTH, ISMODIFY, INPUTHEIGHT, HTML_INNER, FIELD_LENGTH, UUID FROM SYS_BUSINESS_METADATA_MAP WHERE METADATA_ID=?");
      reset = DBUtil.executeQuery(ps, table.getId());
      while (reset.next()) {
        Field field = new Field();
        field.setId(reset.getInt("ID"));
        field.setDisplaySql(reset.getString("DISPLAY_SQL"));
        field.setDisplayType(reset.getString("DISPLAY_TYPE"));
        field.setDisplayWidth(reset.getInt("DISPLAY_WIDTH"));
        field.setFieldDefault(reset.getString("FIELD_DEFAULT"));
        field.setFieldLength(reset.getString("FIELD_LENGTH"));
        field.setFieldName(reset.getString("FIELD_NAME"));
        field.setFieldNotNull(reset.getString("FIELD_NOTNULL"));
        field.setFieldTitle(reset.getString("FIELD_TITLE"));
        field.setFieldType(reset.getString("FIELD_TYPE"));
        field.setHtmlInner(reset.getString("HTML_INNER"));
        field.setInputHeight(reset.getInt("INPUTHEIGHT"));
        field.setInputWidth(reset.getInt("INPUTWIDTH"));
        field.setIsModify(reset.getInt("ISMODIFY"));
        field.setIsDisplay(reset.getInt("ISDISPLAY"));
        field.setUuid(reset.getString("UUID"));
        field.setTable(table);
        tables.add(field);
      }
    } finally {
      DBUtil.close(ps, reset);
    }

    table.setFieldList(tables);
    return tables;
  }
}
