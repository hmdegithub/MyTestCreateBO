package cn.hm.quickbo.dbtable.domain;

import cn.hm.quickbo.dbtable.domain.dict.TableDict;

/**
 * BO表字段的属性信息.
 * 
 * @author huangming
 *
 */
public class TableField {

  /**
   * 字段名称对应数据表名称.
   */
  private String fieldName = null;
  /**
   * 字段标题.
   */
  private String fieldTitle = null;
  /**
   * 字段类型.
   */
  private String fieldType = "文本";
  /**
   * 字段长度.
   */
  private String fieldLenght = "0";
  /**
   * 字段非空校验.
   */
  private String fieldNotNull = "true";
  /**
   * 字段默认值.
   */
  private String fieldDefault = "";
  /**
   * 设置校验.
   */
  private String setValidate = "undefined";
  private String displayType = "undefined";
  /**
   * 是否可显示.
   */
  private String isdisplay = "true";
  /**
   * 是否可修改.
   */
  private String ismodify = "true";
  /**
   * 是否可复制.
   */
  private String iscopy = "false";
  /**
   * 实体字段还是虚拟字段.
   */
  private String mapType = "实体字段";
  /**
   * 字段uuid.
   */
  private String uuid = "";
  /**
   * bizGroup.
   */
  private String bizGroup = "";
  /**
   * 映射索引.
   */
  private String mapIndex = "45";
  /**
   * 行号.
   */
  private String rowid = "0";

  public TableField(String fieldName, String fieldTitle, String fieldType, String fieldLenght, String fieldNotNull, String filedDefualt, String setValidate,
          String displayType, String isdisplay, String ismodify, String iscopy, String mapType, String uuid, String bizGroup, String mapIndex, String rowid) {
    super();
    this.fieldName = fieldName;
    this.fieldTitle = fieldTitle;
    this.fieldType = fieldType;
    this.fieldLenght = fieldLenght;
    this.fieldNotNull = fieldNotNull;
    this.fieldDefault = filedDefualt;
    this.setValidate = setValidate;
    this.displayType = displayType;
    this.isdisplay = isdisplay;
    this.ismodify = ismodify;
    this.iscopy = iscopy;
    this.mapType = mapType;
    this.uuid = uuid;
    this.bizGroup = bizGroup;
    this.mapIndex = mapIndex;
    this.rowid = rowid;
  }
  public TableField() {
    super();
  }

  public TableField(String fieldName, String fieldTitle, String fieldLenght) {
    this.fieldName = fieldName;
    this.fieldTitle = fieldTitle;
    this.fieldLenght = fieldLenght;
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
    this.fieldType = TableDict.parseType(fieldType);
  }
  public String getFieldLenght() {
    return fieldLenght;
  }
  public void setFieldLenght(String fieldLenght) {
    this.fieldLenght = fieldLenght;
  }
  public String getSetValidate() {
    return setValidate;
  }
  public void setSetValidate(String setValidate) {
    this.setValidate = setValidate;
  }
  public String getDisplayType() {
    return displayType;
  }
  public void setDisplayType(String displayType) {
    this.displayType = displayType;
  }
  public String getIsdisplay() {
    return isdisplay;
  }
  public void setIsdisplay(String isdisplay) {
    this.isdisplay = isdisplay;
  }
  public String getIsmodify() {
    return ismodify;
  }
  public void setIsmodify(String ismodify) {
    this.ismodify = ismodify;
  }
  public String getIscopy() {
    return iscopy;
  }
  public void setIscopy(String iscopy) {
    this.iscopy = iscopy;
  }
  public String getMapType() {
    return mapType;
  }
  public void setMapType(String mapType) {
    this.mapType = mapType;
  }
  public String getUuid() {
    return uuid;
  }
  public void setUuid(String uuid) {
    this.uuid = uuid;
  }
  public String getBizGroup() {
    return bizGroup;
  }
  public void setBizGroup(String bizGroup) {
    this.bizGroup = bizGroup;
  }
  public String getMapIndex() {
    return mapIndex;
  }
  public void setMapIndex(String mapIndex) {
    this.mapIndex = mapIndex;
  }
  public String getRowid() {
    return rowid;
  }
  public void setRowid(String rowid) {
    this.rowid = rowid;
  }
  public String getFieldNotNull() {
    return fieldNotNull;
  }
  public void setFieldNotNull(String fieldNotNull) {
    this.fieldNotNull = fieldNotNull;
  }
  public String getFieldDefualt() {
    return fieldDefault;
  }
  public void setFieldDefault(String fieldDefualt) {
    this.fieldDefault = fieldDefualt;
  }
  @Override
  public String toString() {
    return "BOField [fieldName=" + fieldName + ", fieldTitle=" + fieldTitle + ", fieldType=" + fieldType + ", fieldLenght=" + fieldLenght + ", fieldNotNull="
            + fieldNotNull + ", fieldDefualt=" + fieldDefault + ", setValidate=" + setValidate + ", displayType=" + displayType + ", isdisplay=" + isdisplay
            + ", ismodify=" + ismodify + ", iscopy=" + iscopy + ", mapType=" + mapType + ", uuid=" + uuid + ", bizGroup=" + bizGroup + ", mapIndex=" + mapIndex
            + ", rowid=" + rowid + "]";
  }

}
