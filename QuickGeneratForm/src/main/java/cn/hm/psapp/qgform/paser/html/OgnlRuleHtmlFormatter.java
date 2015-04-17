package cn.hm.psapp.qgform.paser.html;

import java.util.ArrayList;
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
import cn.hm.psapp.qgform.format.ExpressMatchFieldWidthFormat;

public class OgnlRuleHtmlFormatter {

  private static Map<String, Object> form = null;
  private static final int INIT_COL_NUM;
  private static List<Map<String, Object>> tdRuleList = null;
  private static List<Map<String, Object>> trRuleList = null;
  private static List<Map<String, Object>> subRuleList = null;
  private static final Pattern MATCH = Pattern.compile("\\$\\{TEXT\\}");

  static {
    form = ConfigurationFactory.loadJson().getForm();
    tdRuleList = (List<Map<String, Object>>) form.get("tdmatch");
    trRuleList = (List<Map<String, Object>>) form.get("trmatch");
    subRuleList = (List<Map<String, Object>>) form.get("submatch");
    INIT_COL_NUM = (int) form.get("col");
  }

  public String build(Form form) {
    Table mTable = form.getMainTable();
    List<Field> fieldList = mTable.getFieldList();
    return builderHtml(fieldList);
  }

  public String buildSub(Form form) {
    OgnlContext context = new OgnlContext();
    StringBuilder html = new StringBuilder(300);
    List<Table> tables = form.getTables();
    for (int i = 0; i < tables.size(); i++) {
      context.put("row", i);
      if (i == 0) {
        html.append(replaceMatch(subMatch(context), "[@SubReport]")).append("\n");
      } else {
        html.append(replaceMatch(subMatch(context), "[@SubReport" + (i + 1) + "]")).append("\n");
      }
    }
    return html.toString();
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

    StringBuilder html = new StringBuilder(500);
    StringBuilder rowHtml = new StringBuilder(200);

    OgnlContext context = new OgnlContext();

    // 将所有下表存入集合.
    List<Integer> fieldIndexArray = new ArrayList<Integer>(fieldList.size());
    for (int i = 0; i < fieldList.size(); i++) {
      fieldIndexArray.add(i);
    }

    // 遍历所有单元格，拼凑表格.
    for (int i = 0; fieldIndexArray.size() > 0; i++) {
      int index = i % fieldIndexArray.size();

      // 存入Context.
      Field field = fieldList.get(fieldIndexArray.get(index));
      ExpressMatchFieldWidthFormat.putFieldToContext(context, field);
      context.put("col", curCol);

      // 匹配HTML.
      Map<String, Object> tdMap = tdMatch(context);
      String titleHtml = (String) tdMap.get("titleHtml");
      String fieldHtml = (String) tdMap.get("fieldHtml");
      int colspan = (int) tdMap.get("colspan");

      // 若不超界则加入HTML，否则换行.
      if (curCol + colspan + 1 <= INIT_COL_NUM) {
        rowHtml.append(replaceMatch(titleHtml, field.getFieldTitle()));
        rowHtml.append(replaceMatch(fieldHtml, "[@" + field.getFieldName() + "]"));

        // 移除下标.
        fieldIndexArray.remove(index);

        // 计算与换行.
        curCol = curCol + colspan + 1;
        if (curCol == INIT_COL_NUM) {
          nextRow = true;
        }
      } else {
        nextRow = true;
      }

      // 换行或结束时，进行此操作.
      if (nextRow || fieldIndexArray.size() == 0) {
        // 补充空格.
        ExpressMatchFieldWidthFormat.putFieldToContext(context, null);
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

  /**
   * 匹配子表标签.
   * 
   * @param express
   * @param context
   * @return
   */
  public String subMatch(OgnlContext context) {
    for (Map<String, Object> map : subRuleList) {
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
