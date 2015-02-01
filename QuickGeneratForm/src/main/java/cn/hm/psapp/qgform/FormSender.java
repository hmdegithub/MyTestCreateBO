package cn.hm.psapp.qgform;


/**
 * 表单写入接口。
 * 
 * @author huangming
 *
 */
public interface FormSender {

  /**
   * 写入Form对象.
   * 
   * @param form
   */
  public void send(Form form);

}
