package cn.hm.quickbo.dbtable.domain;

import java.util.List;

/**
 * 表对象.
 * @author huangming
 *
 */
public class Table {

  /**
   * 模型名称.
   */
  private String groupName;
  /**
   * 表名称.
   */
  private String tableName;
  /**
   * 表标题.
   */
  private String tableTitle;
  /**
   * 字段列表.
   */
  private List<TableField> fieldList;

  public String getGroupName() {
    return groupName;
  }
  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }
  public String getTableTitle() {
    return tableTitle;
  }
  
  public void setTableTitle(String tableTitle) {
    this.tableTitle = tableTitle;
  }
  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName.toUpperCase();
  }

  public List<TableField> getFieldList() {
    return fieldList;
  }

  public void setFieldList(List<TableField> fieldList) {
    this.fieldList = fieldList;
  }
  @Override
  public String toString() {
    return "Table [groupName=" + groupName + ", tableName=" + tableName + ", tableTitle=" + tableTitle + ", fieldList=" + fieldList + "]";
  }

}
