package cn.hm.quickbo.util;

/**
 * 校验工具类.
 * 
 * @author huangming
 *
 */
public class ValidateUtil {

  /**
   * 私有构造.
   */
  private ValidateUtil() {
  }

  /**
   * 校验字符串Null或者''.
   * 
   * @param value
   * @return true为空， false为非空.
   */
  public static boolean validateNullOrEmtpy(String value) {
    if (value == null || "".equals(value.trim())) {
      return true;
    } else {
      return false;
    }
  }

}
