package cn.hm.psapp.qgform;

import java.util.List;

public class Form {

  private List<Table> tables;
  private Table mainTable;
  private String uuid;
  private String name;
  private String modelName;
  private String title;
  private int id;

  public Table getMainTable() {
    return mainTable;
  }

  public void setMainTable(Table mainTable) {
    this.mainTable = mainTable;
  }
  public List<Table> getTables() {
    return tables;
  }
  public void setTables(List<Table> tables) {
    this.tables = tables;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getModelName() {
    return modelName;
  }

  public void setModelName(String modelName) {
    this.modelName = modelName;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public String toString() {
    return "Form [tables=" + tables + ", mainTable=" + mainTable + ", uuid=" + uuid + ", name=" + name + ", modelName=" + modelName + ", title=" + title
            + ", id=" + id + "]";
  }

}
