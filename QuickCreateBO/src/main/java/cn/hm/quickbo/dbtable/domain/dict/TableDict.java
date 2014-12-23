package cn.hm.quickbo.dbtable.domain.dict;

public class TableDict {

  public static final String FIELDTYPE_TEXT = "文本";
  public static final String FIELDTYPE_NUMERIC = "数值";
  public static final String FIELDTYPE_DATE = "日期";

  public static String parseType(String str) {
    if(str == null)
      return null;
    
    String upperCase = str.trim().toUpperCase();
    switch (upperCase) {
    case "VARCHAR":
      return TableDict.FIELDTYPE_TEXT;
    case "VARHCAR":
      return TableDict.FIELDTYPE_TEXT;
    case "NUMERIC":
      return TableDict.FIELDTYPE_NUMERIC;
    case "NUMBER":
      return TableDict.FIELDTYPE_NUMERIC;
    case "NUMBERIC":
      return TableDict.FIELDTYPE_NUMERIC;
    case "DATE":
      return TableDict.FIELDTYPE_DATE;
    case "DATA":
      return TableDict.FIELDTYPE_DATE;
    case "DATETIME":
      return TableDict.FIELDTYPE_DATE;
    case TableDict.FIELDTYPE_TEXT:
      return upperCase;
    case TableDict.FIELDTYPE_DATE:
      return upperCase;
    case TableDict.FIELDTYPE_NUMERIC:
      return upperCase;
    default:
      return null;
    }
  }
}
