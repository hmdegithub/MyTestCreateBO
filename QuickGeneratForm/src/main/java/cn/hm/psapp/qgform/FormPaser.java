package cn.hm.psapp.qgform;


/**
 * 表单解析接口，将表单对象转换成HTML。
 * @author huangming
 *
 */
public interface FormPaser {

  /**
   * 转换Form对象成HTML.
   * @param form
   * @return
   */
  public String parse(Form form);

}
