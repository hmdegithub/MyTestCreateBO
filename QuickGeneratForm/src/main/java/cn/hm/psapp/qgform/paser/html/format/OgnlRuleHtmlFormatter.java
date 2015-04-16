package cn.hm.psapp.qgform.paser.html.format;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;
import cn.hm.psapp.qgform.Field;
import cn.hm.psapp.qgform.Form;
import cn.hm.psapp.qgform.Table;
import cn.hm.psapp.qgform.config.ConfigurationFactory;
import cn.hm.psapp.qgform.paser.html.FieldHtmlFormatter;

public class OgnlRuleHtmlFormatter implements FieldHtmlFormatter {

  private static Map<String, Object> form = null;
  private static final int INIT_COL_NUM;
  private static List<Map<String, Object>> tdRuleList = null;
  private static List<Map<String, Object>> trRuleList = null;
  private static final Pattern MATCH = Pattern.compile("\\$\\{TEXT\\}");

  static {
    form = ConfigurationFactory.loadJson().getForm();
    tdRuleList = (List<Map<String, Object>>) form.get("tdmatch");
    trRuleList = (List<Map<String, Object>>) form.get("trmatch");
    INIT_COL_NUM = (int) form.get("col");
  }

  @Override
  public String build(Form form) {
    Table mTable = form.getMainTable();
    List<Field> fieldList = mTable.getFieldList();
    return builderHtml(fieldList);
  }

  /**
   * 生成HTML.
   * 
   * @param fieldList
   * @return
   */
  private String builderHtml(List<Field> fieldList) {
    int curCol = 0;
    int curRow = 0;

    boolean nextRow = false;

    StringBuilder html = new StringBuilder();
    StringBuilder rowHtml = new StringBuilder();

    OgnlContext context = new OgnlContext();

    // 遍历所有单元格，拼凑表格.
    for (int i = 0; fieldList.size() > 0; i++) {
      context.put("col", curCol);

      Field field = fieldList.get(i % fieldList.size());
      putFieldToContext(context, field);

      Map<String, Object> tdMap = tdMatch(context);
      String titleHtml = (String) tdMap.get("titleHtml");
      String fieldHtml = (String) tdMap.get("fieldHtml");
      int colspan = (int) tdMap.get("colspan");

      if (curCol + colspan + 1 <= INIT_COL_NUM) {
        rowHtml.append(replaceMatch(titleHtml, field.getFieldTitle()));
        rowHtml.append(replaceMatch(fieldHtml, "[@" + field.getFieldName() + "]"));

        // 计算与换行.
        curCol = curCol + colspan + 1;
        fieldList.remove(i % fieldList.size());
        if (curCol == INIT_COL_NUM) {
          nextRow = true;
        }
      } else {
        nextRow = true;
      }

      // 换行或结束时，进行此操作.
      if (nextRow || fieldList.size() == 0) {
        // 补充空格.
        putFieldToContext(context, null);
        while (INIT_COL_NUM - curCol > 0) {
          context.put("col", curCol);
          tdMap = tdMatch(context);
          titleHtml = (String) tdMap.get("titleHtml");
          fieldHtml = (String) tdMap.get("fieldHtml");
          colspan = (int) tdMap.get("colspan");

          rowHtml.append(replaceMatch(titleHtml, "&nbsp"));
          rowHtml.append(replaceMatch(fieldHtml, "&nbsp;"));
          curCol = curCol + colspan + 1;
        }
        html.append("\n");
        html.append(replaceMatch(trMatch(context), rowHtml.toString()));
        rowHtml.delete(0, rowHtml.length());

        // 换行处理.
        curCol = 0;
        curRow++;
        nextRow = false;
        context.put("row", curRow);
      }
    }

    return html.toString();
  }

  public void putFieldToContext(OgnlContext context, Field field) {
    if (field == null) {
      context.put("name", null);
      context.put("title", null);
      context.put("width", null);
      context.put("type", null);
      context.put("control", null);
    } else {
      context.put("name", field.getFieldName());
      context.put("title", field.getFieldTitle());
      context.put("width", field.getInputWidth());
      context.put("type", field.getFieldType());
      context.put("control", field.getDisplayType());
    }
  }

  /**
   * 匹配行标签.
   * 
   * @param express
   * @param context
   * @return
   */
  public String trMatch(OgnlContext context) {
    for (Map<String, Object> map : trRuleList) {
      String expression = (String) map.get("expression");
      try {
        Object value = Ognl.getValue(expression, context);
        boolean b = (boolean) value;
        if (b) {
          return (String) map.get("html");
        }
      } catch (OgnlException e) {
        throw new RuntimeException(e);
      }
    }
    return "";
  }

  /**
   * 匹配列标签.
   * 
   * @param express
   * @param context
   * @return
   */
  public Map<String, Object> tdMatch(OgnlContext context) {
    for (Map<String, Object> map : tdRuleList) {
      String expression = (String) map.get("expression");
      try {
        Object value = Ognl.getValue(expression, context);
        boolean b = (boolean) value;
        if (b) {
          return map;
        }
      } catch (OgnlException e) {
        throw new RuntimeException(e);
      }
    }
    return null;
  }

  public String replaceMatch(String str, String replace) {
    return MATCH.matcher(str).replaceFirst(replace);
  }

  /**
   * 过滤字段.
   * 
   * @param field
   * @return
   */
  public boolean isShow(Field field) {
    if (field.getIsDisplay() == Field.YES) {
      return !(field.getFieldName().equals("制单人") || field.getFieldName().equals("制单日期"));
    }
    return false;
  }

}
