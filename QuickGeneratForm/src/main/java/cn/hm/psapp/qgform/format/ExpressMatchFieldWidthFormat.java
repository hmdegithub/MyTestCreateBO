package cn.hm.psapp.qgform.format;

import java.util.List;
import java.util.Map;

import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;
import cn.hm.psapp.qgform.Field;
import cn.hm.psapp.qgform.Form;
import cn.hm.psapp.qgform.Table;
import cn.hm.psapp.qgform.config.ConfigurationFactory;

public class ExpressMatchFieldWidthFormat {

  private static Map<String, Object> form = ConfigurationFactory.loadJson().getForm();
  private static List<Map<String, Object>> tdRuleList = null;
  /** 子表宽度计算公式. */
  private static final String subWidthExpression;
  /** 是否强制更改宽度. */
  private static final boolean ISCHANGE;

  static {
    subWidthExpression = (String) form.get("inputWidth");
    ISCHANGE = (boolean) form.get("inputWidthForOneWordMustChange");
    tdRuleList = (List<Map<String, Object>>) form.get("tdmatch");
  }

  public Form format(Form form) {
    Table mainTable = form.getMainTable();
    OgnlContext context = new OgnlContext();

    // 主表字段调整.
    List<Field> fieldList = mainTable.getFieldList();
    for (Field field : fieldList) {
      putFieldToContext(context, field);
      field.setInputWidth(widthMatchForMain(context));
    }

    context.clear();

    // 子表字段调整.
    List<Table> tables = form.getTables();
    for (Table table : tables) {
      List<Field> fieldList2 = table.getFieldList();
      for (Field field : fieldList2) {
        context.put("length", field.getFieldTitle().length());
        int modifyWidth = widthMatchForSub(context);
        if (modifyWidth < field.getDisplayWidth() && ISCHANGE) {
          field.setDisplayWidth(modifyWidth);
        }
      }
    }

    return form;
  }

  /**
   * 匹配主表宽度标签.
   * 
   * @param context
   * @return
   */
  public int widthMatchForMain(OgnlContext context) {
    for (Map<String, Object> map : tdRuleList) {
      String expression = (String) map.get("expression");
      try {
        Object value = Ognl.getValue(expression, context);
        boolean b = (boolean) value;
        if (b) {
          return (int) map.get("width");
        }
      } catch (OgnlException e) {
        throw new RuntimeException(e);
      }
    }
    return 0;
  }

  public static void putFieldToContext(OgnlContext context, Field field) {
    if (field == null) {
      context.put("name", null);
      context.put("title", null);
      context.put("width", null);
      context.put("type", null);
      context.put("control", null);
      context.put("length", null);
    } else {
      String fieldLength = field.getFieldLength();
      if (fieldLength.indexOf(",") != -1) {
        String[] split = fieldLength.split(",");
        Integer length = Integer.parseInt(split[0]) + Integer.parseInt(split[1]);
        context.put("length", length.toString());
      } else {
        context.put("length", field.getFieldLength());
      }
      context.put("name", field.getFieldName());
      context.put("title", field.getFieldTitle());
      context.put("width", field.getInputWidth());
      context.put("type", field.getFieldType());
      context.put("control", field.getDisplayType());
    }
  }

  /**
   * 匹配子表宽度标签.
   * 
   * @param context
   * @return
   */
  public int widthMatchForSub(OgnlContext context) {
    try {
      return (int) Ognl.getValue(subWidthExpression, context);
    } catch (OgnlException e) {
      throw new RuntimeException(e);
    }
  }

}
