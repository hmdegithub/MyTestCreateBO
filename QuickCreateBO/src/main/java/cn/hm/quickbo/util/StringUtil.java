package cn.hm.quickbo.util;

import java.util.regex.Pattern;

public class StringUtil {

  private StringUtil() {
  }

  /**
   * 替换不掉的一个空格!
   */
  private static String UNKONW_SPACE = new String(new byte[] { -62, -96 });
  private static String RE_STR = "\\[(.*?)\\]|\\((.*?)\\)|\\（(.*?)\\）|\\【(.*?)\\】| \\s | 　|[" + UNKONW_SPACE + "]";
  private static String RE_SPACE_STR = "\\s | 　|[" + UNKONW_SPACE + "]";
  private static Pattern _pattern_bz = Pattern.compile(RE_STR);
  private static Pattern _pattern_space = Pattern.compile(RE_SPACE_STR);

  /**
   * 将备注等文字去掉.
   * 
   * @param str
   * @return
   */
  public static String replaceOtherText(String str) {
    return _pattern_bz.matcher(str).replaceAll("");
  }
  /**
   * 替换掉空格文字.
   * 
   * @param str
   * @return
   */
  public static String replaceAllSpace(String str) {
    return _pattern_space.matcher(str).replaceAll("");
  }
}
