package cn.hm.psapp.qgform.paser.html;

import cn.hm.psapp.qgform.Field;
import cn.hm.psapp.qgform.Form;

public interface FieldHtmlFormatter {

  /**
   * 组装对象.
   * 
   * @param cellList
   * @return
   */
  public String build(Form form);

  /**
   * 匹配HTML.
   * 
   * @param field
   * @return
   */
  public TableCell matchTableCell(Field field);
}
