package cn.hm.psapp.qgform.format;

import java.util.List;
import java.util.Map;

import cn.hm.psapp.qgform.Field;
import cn.hm.psapp.qgform.Form;
import cn.hm.psapp.qgform.Table;
import cn.hm.psapp.qgform.config.ConfigurationFactory;

public class FieldControlFormat {

  private static Map<String, Object> form = ConfigurationFactory.loadJson().getForm();
  /** 默认INPUTWIDTH. */
  private static final int DEFAULT_INPUTWIDTH;
  /** 是否强制更改宽度. */
  private static final boolean ISCHANGE;

  static {
    Map<String, Object> subtableMap = (Map<String, Object>) form.get("subtable");
    ISCHANGE = (boolean) subtableMap.get("inputWidthForOneWordMustChange");
    DEFAULT_INPUTWIDTH = (int) subtableMap.get("inputWidthForOneWord");
  }

  public Form format(Form form) {
    Table mainTable = form.getMainTable();

    List<Field> fieldList = mainTable.getFieldList();
    for (Field field : fieldList) {
      field.setInputWidth(DEFAULT_INPUTWIDTH);
      matchDataXml(field);
      matchInputWidth(field);
    }

    List<Table> tables = form.getTables();
    for (Table table : tables) {
      List<Field> fieldList2 = table.getFieldList();
      for (Field field : fieldList2) {
        matchDisplayWidth(field);
      }
    }

    return form;
  }
  
  /**
   * 匹配显示宽度.
   * 
   * @param field
   * @return
   */
  public Field matchDisplayWidth(Field field) {
    String fieldTitle = field.getFieldTitle();
    int length = fieldTitle.length();
    field.setDisplayWidth(30 + length * 15);
    return field;
  }

  /**
   * 显示数据字典宽度.
   * 
   * @param field
   * @return
   */
  public Field matchDataXml(Field field) {
    if (Field.DISPLAYTYPE_数据字典.equals(field.getDisplayType()) && Field.FIELD_TYPE_文本.equals(field.getFieldType())) {
      field.setInputWidth(DEFAULT_INPUTWIDTH - 5);
      field.setHtmlInner("readonly");
    }
    return field;
  }

  /**
   * 匹配输入宽度.
   * 
   * @param field
   * @return
   */
  public Field matchInputWidth(Field field) {
    if ((Field.DISPLAYTYPE_单行.equals(field.getDisplayType()) || Field.DISPLAYTYPE_多行.equals(field.getDisplayType()))
            && Field.FIELD_TYPE_文本.equals(field.getFieldType())) {
      String fieldLength = field.getFieldLength();
      int fieldSize = Integer.parseInt(fieldLength);
      if (fieldSize >= 200) {
        field.setDisplayType(Field.DISPLAYTYPE_多行);
        field.setInputHeight(3);
        field.setInputWidth(DEFAULT_INPUTWIDTH * 3);
      } else if (fieldSize >= 140) {
        field.setDisplayType(Field.DISPLAYTYPE_多行);
        field.setInputHeight(2);
        field.setInputWidth(DEFAULT_INPUTWIDTH * 3);
      } else if (fieldSize > 100) {
        field.setDisplayType(Field.DISPLAYTYPE_单行);
        field.setInputWidth(DEFAULT_INPUTWIDTH * 2);
      } else if (fieldSize > -1) {
        field.setDisplayType(Field.DISPLAYTYPE_单行);
        field.setInputWidth(DEFAULT_INPUTWIDTH);
      }
    }
    return field;
  }
}
