package cn.hm.psapp.qgform;

public class Field {
  
  public static final int YES = 1;
  public static final int NO = 0;

  public static final String DISPLAYTYPE_部门字典 = "部门字典";
  public static final String DISPLAYTYPE_文件字 = "文件字";
  public static final String DISPLAYTYPE_用户扩展授权 = "用户扩展授权";
  public static final String DISPLAYTYPE_地址簿 = "地址簿";
  public static final String DISPLAYTYPE_电子印章 = "电子印章";
  public static final String DISPLAYTYPE_单选按纽组 = "单选按纽组";
  public static final String DISPLAYTYPE_iWebOffice = "iWebOffice";
  public static final String DISPLAYTYPE_数值 = "数值";
  public static final String DISPLAYTYPE_HTML排版 = "HTML排版";
  public static final String DISPLAYTYPE_传阅列表 = "传阅列表";
  public static final String DISPLAYTYPE_增强地址簿 = "增强地址簿";
  public static final String DISPLAYTYPE_单行 = "单行";
  public static final String DISPLAYTYPE_附件 = "附件";
  public static final String DISPLAYTYPE_数据字典 = "数据字典";
  public static final String DISPLAYTYPE_货币 = "货币";
  public static final String DISPLAYTYPE_列表 = "列表";
  public static final String DISPLAYTYPE_URL = "URL";
  public static final String DISPLAYTYPE_多选列表 = "多选列表";
  public static final String DISPLAYTYPE_多行 = "多行";
  public static final String DISPLAYTYPE_日期时间 = "日期时间";
  public static final String DISPLAYTYPE_组合属性录入 = "组合属性录入";
  public static final String DISPLAYTYPE_复选框 = "复选框";
  public static final String DISPLAYTYPE_公文主题词 = "公文主题词";
  public static final String DISPLAYTYPE_选项卡 = "选项卡";
  public static final String DISPLAYTYPE_日期 = "日期";
  public static final String DISPLAYTYPE_协同日程 = "协同日程";
  
  public static final String FIELD_TYPE_备注="备注";
  public static final String FIELD_TYPE_文本="文本";
  public static final String FIELD_TYPE_数值="数值";
  public static final String FIELD_TYPE_日期="日期";

  private int id;
  private String fieldName;
  private String fieldTitle;
  private String fieldType;
  private String fieldNotNull;
  private String fieldDefault;
  private int displayWidth;
  private String displayType;
  private String displaySql;
  private int isDisplay;
  private int inputWidth;
  private int isModify;
  private int inputHeight;
  private String htmlInner;
  private String fieldLength;
  private String uuid;
  private Table table;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getFieldName() {
    return fieldName;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getFieldTitle() {
    return fieldTitle;
  }

  public void setFieldTitle(String fieldTitle) {
    this.fieldTitle = fieldTitle;
  }

  public String getFieldType() {
    return fieldType;
  }

  public void setFieldType(String fieldType) {
    this.fieldType = fieldType;
  }

  public String getFieldNotNull() {
    return fieldNotNull;
  }

  public void setFieldNotNull(String fieldNotNull) {
    this.fieldNotNull = fieldNotNull;
  }

  public String getFieldDefault() {
    return fieldDefault;
  }

  public void setFieldDefault(String fieldDefault) {
    this.fieldDefault = fieldDefault;
  }

  public int getDisplayWidth() {
    return displayWidth;
  }

  public void setDisplayWidth(int displayWidth) {
    this.displayWidth = displayWidth;
  }

  public String getDisplayType() {
    return displayType;
  }

  public void setDisplayType(String displayType) {
    this.displayType = displayType;
  }

  public String getDisplaySql() {
    return displaySql;
  }

  public void setDisplaySql(String displaySql) {
    this.displaySql = displaySql;
  }

  public int getIsDisplay() {
    return isDisplay;
  }

  public void setIsDisplay(int isDisplay) {
    this.isDisplay = isDisplay;
  }

  public int getInputWidth() {
    return inputWidth;
  }

  public void setInputWidth(int inputWidth) {
    this.inputWidth = inputWidth;
  }

  public int getIsModify() {
    return isModify;
  }

  public void setIsModify(int isModify) {
    this.isModify = isModify;
  }

  public int getInputHeight() {
    return inputHeight;
  }

  public void setInputHeight(int inputHeight) {
    this.inputHeight = inputHeight;
  }

  public String getHtmlInner() {
    return htmlInner;
  }

  public void setHtmlInner(String htmlInner) {
    this.htmlInner = htmlInner;
  }

  public String getFieldLength() {
    return fieldLength;
  }

  public void setFieldLength(String fieldLength) {
    this.fieldLength = fieldLength;
  }

  public Table getTable() {
    return table;
  }

  public void setTable(Table table) {
    this.table = table;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  @Override
  public String toString() {
    return "Field [id=" + id + ", fieldName=" + fieldName + ", fieldTitle=" + fieldTitle + ", fieldType=" + fieldType + ", fieldNotNull=" + fieldNotNull
            + ", fieldDefault=" + fieldDefault + ", displayWidth=" + displayWidth + ", displayType=" + displayType + ", displaySql=" + displaySql
            + ", isDisplay=" + isDisplay + ", inputWidth=" + inputWidth + ", isModify=" + isModify + ", inputHeight=" + inputHeight + ", htmlInner="
            + htmlInner + ", fieldLength=" + fieldLength + ", uuid=" + uuid + "]";
  }
  
}
