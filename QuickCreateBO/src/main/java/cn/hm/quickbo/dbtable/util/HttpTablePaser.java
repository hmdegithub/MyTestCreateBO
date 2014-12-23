package cn.hm.quickbo.dbtable.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import cn.hm.quickbo.conf.AWSConfigure;
import cn.hm.quickbo.dbtable.domain.Table;
import cn.hm.quickbo.dbtable.domain.TableField;

/**
 * 将Table对象转换成http请求参数.
 * 
 * @author huangming
 *
 */
public final class HttpTablePaser {

  private static AWSConfigure conf = AWSConfigure.getInstance();

  /**
   * 私有构造.
   */
  private HttpTablePaser() {
  }

  /**
   * 测试连接，并设置Sid.
   * 
   * @return
   */
  public static boolean testConnection() {
    try {
      String sid = HttpLogin.getSid(conf.getAwsurl(), conf.getUsername(), conf.getPassword());
      conf.setSid(sid);
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 获取创建BO表字段或修改BO表字段的POST请求参数.
   * 
   * @param sid
   * @param metaDataId
   * @param list
   * @throws UnsupportedEncodingException
   */
  public static String saveOrUpdateTableRequestParam(String sid, String metaDataId, List<TableField> list) throws UnsupportedEncodingException {
    StringBuilder sbuilder = new StringBuilder();
    sbuilder.append("cmd=MetaData_Design_Maps_Save").append("&");
    sbuilder.append("metaDataId=").append(metaDataId).append("&");
    sbuilder.append("sid=").append(sid).append("&");
    sbuilder.append("data=").append(parseFieldInfoListToStr(list));
    return sbuilder.toString();
  }

  /**
   * 创建新表并创建字段.
   * 
   * @param sid
   * @param entityName
   * @param entityTitle
   * @param groupName
   * @param list
   * @throws UnsupportedEncodingException
   */
  public static String createTableAndTableRequestParam(String sid, Table table) throws UnsupportedEncodingException {

    StringBuilder sbuilder = new StringBuilder();
    sbuilder.append("boType=TABLE").append("&");
    // 表名，BO_ XXX，代表的是XXX
    sbuilder.append("entityName=").append(table.getTableName()).append("&");
    // 表的中文注释名
    sbuilder.append("entityTitle=").append(table.getTableTitle()).append("&");
    // 模型库分类名
    sbuilder.append("groupName=").append(table.getGroupName()).append("&");
    sbuilder.append("cmd=MetaData_Design_Create_And_Maps_Save").append("&");
    sbuilder.append("sid=").append(sid).append("&");

    List<TableField> list = table.getFieldList();
    if (list != null) {
      sbuilder.append("data=").append(parseFieldInfoListToStr(list));
    }
    return sbuilder.toString();
  }

  /**
   * 将所有的字段信息转换成字符串.
   * 
   * @param list
   * @return
   * @throws UnsupportedEncodingException
   */
  private static String parseFieldInfoListToStr(List<TableField> list) throws UnsupportedEncodingException {
    StringBuilder sbuilder = new StringBuilder();
    sbuilder.append("_AWSSHEETMODIFYCOUNT{").append(list.size()).append("}AWSSHEETMODIFYCOUNT_ ");

    int count = 0;
    for (TableField boField : list) {
      boField.setMapIndex(String.valueOf(count));
      sbuilder.append("_AWSSHEETMODIFYRECORD").append(count).append("{").append(parseFieldInfoToStr(boField)).append("}AWSSHEETMODIFYRECORD").append(count)
              .append("_ ");
      count++;
    }

    return sbuilder.toString();
  }

  /**
   * 将字段信息转换成字符串.
   * 
   * @param bofield
   * @return
   */
  private static String parseFieldInfoToStr(TableField bofield) {

    if (bofield.getFieldName() == null || bofield.getFieldTitle() == null || bofield.getFieldLenght() == null) {
      throw new RuntimeException("字段信息不完整 " + bofield);
    }

    StringBuilder sbulider = new StringBuilder();
    sbulider.append("_FIELD_NAME{").append(bofield.getFieldName()).append("}FIELD_NAME_ ");
    sbulider.append("_FIELD_TITLE{").append(bofield.getFieldTitle()).append("}FIELD_TITLE_ ");
    sbulider.append("_FIELD_TYPE{").append(bofield.getFieldType()).append("}FIELD_TYPE_ ");
    sbulider.append("_FIELD_LENGTH{").append(bofield.getFieldLenght()).append("}FIELD_LENGTH_ ");
    sbulider.append("_FIELD_NOTNULL{").append(bofield.getFieldNotNull()).append("}FIELD_NOTNULL_ ");
    sbulider.append("_FIELD_DEFAULT{").append(bofield.getFieldDefualt()).append("}FIELD_DEFAULT_ ");
    sbulider.append("_SET_VALIDATE{").append(bofield.getSetValidate()).append("}SET_VALIDATE_ ");
    sbulider.append("_DISPLAY_TYPE{").append(bofield.getDisplayType()).append("}DISPLAY_TYPE_ ");
    sbulider.append("_ISDISPLAY{").append(bofield.getIsdisplay()).append("}ISDISPLAY_ ");
    sbulider.append("_ISMODIFY{").append(bofield.getIsmodify()).append("}ISMODIFY_ ");
    sbulider.append("_ISCOPY{").append(bofield.getIscopy()).append("}ISCOPY_ ");
    sbulider.append("_MAP_TYPE{").append(bofield.getMapType()).append("}MAP_TYPE_ ");
    sbulider.append("_UUID{").append(bofield.getUuid()).append("}UUID_ ");
    sbulider.append("_BIZ_GROUP{").append(bofield.getBizGroup()).append("}BIZ_GROUP_ ");
    sbulider.append("_MAP_INDEX{").append(bofield.getMapIndex()).append("}MAP_INDEX_ ");
    sbulider.append("_rowid{").append(bofield.getRowid()).append("}rowid_ ");
    return sbulider.toString();
  }

}
