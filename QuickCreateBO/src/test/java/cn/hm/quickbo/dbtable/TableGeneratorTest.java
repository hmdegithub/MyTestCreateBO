package cn.hm.quickbo.dbtable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.junit.Test;

import cn.hm.quickbo.conf.AWSConfigure;
import cn.hm.quickbo.dbtable.domain.Table;
import cn.hm.quickbo.dbtable.domain.TableField;
import cn.hm.quickbo.dbtable.reader.impl.ExcelTableReader;
import cn.hm.quickbo.dbtable.util.HttpLogin;
import cn.hm.quickbo.dbtable.util.HttpTablePaser;
import cn.hm.quickbo.util.HttpUtil;

public class TableGeneratorTest {

  private AWSConfigure conf;
  private ExcelTableReader reader;

  /**
   * 创建BO表的功能测试
   */
  @Test
  public void createTable() {

    try {
      // Http连接
      URL url = new URL("http://" + conf.getAwsurl() + "/ajax");
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();

      Table table = new Table();
      table.setGroupName("AIE练习");
      table.setTableName("贝瑞GOGO");
      table.setTableTitle("BERRY_GOGO");

      // 先获取SID
      String sid = HttpLogin.getSid(conf.getAwsurl(), conf.getUsername(), conf.getPassword());

      // 参数分析
      String param = HttpTablePaser.createTableAndTableRequestParam(sid, table);

      // 发送请求
      HttpUtil.sendPostRequest(conn, param);

      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String readLine = bufferedReader.readLine();

      // 显示消息
      System.out.println(readLine);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void createTableAndField() {
    try {
      // 先获取SID
      // Http连接
      URL url = new URL("http://localhost:8080/portal/ajax");
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();

      Table table = new Table();
      table.setGroupName("AIE练习");
      table.setTableName("贝瑞GOGO");
      table.setTableTitle("BERRY_GOGO");

      TableField boField = new TableField("FIELDA", "字段A", "32");
      ArrayList<TableField> arrayList = new ArrayList<TableField>();
      arrayList.add(boField);
      table.setFieldList(arrayList);

      String sid = HttpLogin.getSid(conf.getAwsurl(), conf.getUsername(), conf.getPassword());

      // 参数分析
      String param = HttpTablePaser.createTableAndTableRequestParam(sid, table);
      // 发送请求
      HttpUtil.sendPostRequest(conn, param);
      String message = HttpUtil.readResponse(conn);
      System.out.println(message);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
