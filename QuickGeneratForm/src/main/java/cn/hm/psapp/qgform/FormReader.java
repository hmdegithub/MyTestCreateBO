package cn.hm.psapp.qgform;


/**
 * 表单读取接口，读取表单。
 * 
 * @author huangming
 *
 */
public interface FormReader {

  /**
   * 读取Form对象.
   * 
   * @param formid
   * @return
   */
  public Form read(String formid);

}
