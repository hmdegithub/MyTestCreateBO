package cn.hm.psapp.qgform;

import java.util.List;

public class Table {

  private int id;
  private String tableName;
  private String tableTitle;
  private List<Field> fieldList;

  public int getId() {
    return id;
  }
  
  public void setId(int id) {
    this.id = id;
  }
  
  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public String getTableTitle() {
    return tableTitle;
  }

  public void setTableTitle(String tableTitle) {
    this.tableTitle = tableTitle;
  }

  public List<Field> getFieldList() {
    return fieldList;
  }

  public void setFieldList(List<Field> fieldList) {
    this.fieldList = fieldList;
  }

  @Override
  public String toString() {
    return "Table [id=" + id + ", tableName=" + tableName + ", tableTitle=" + tableTitle + "]";
  }
}
