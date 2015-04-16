package cn.hm.psapp.qgform.format;

import java.util.List;
import java.util.Map;

import cn.hm.psapp.qgform.Field;
import cn.hm.psapp.qgform.Form;
import cn.hm.psapp.qgform.Table;
import cn.hm.psapp.qgform.config.ConfigurationFactory;

public class ExpressMatchFieldWidthFormat {

  private static Map<String, Object> form = ConfigurationFactory.loadJson().getForm();
  /** 是否强制更改宽度. */
  private static final boolean ISCHANGE;

  static {
    Map<String, Object> subtableMap = (Map<String, Object>) form.get("subtable");
    ISCHANGE = (boolean) subtableMap.get("inputWidthForOneWordMustChange");
  }

  public Form format(Form form) {
    Table mainTable = form.getMainTable();

    // 主表字段调整.
    List<Field> fieldList = mainTable.getFieldList();
    for (Field field : fieldList) {
    }

    // 子表字段调整.
    List<Table> tables = form.getTables();
    for (Table table : tables) {
      List<Field> fieldList2 = table.getFieldList();
      for (Field field : fieldList2) {
      }
    }

    return form;
  }

}
