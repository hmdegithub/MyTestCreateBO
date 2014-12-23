package cn.hm.quickbo.dbtable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.hm.quickbo.conf.AWSConfigure;
import cn.hm.quickbo.dbtable.domain.Table;
import cn.hm.quickbo.dbtable.reader.impl.ExcelTableReader;
import cn.hm.quickbo.dbtable.util.HttpLogin;
import cn.hm.quickbo.dbtable.util.HttpTablePaser;
import cn.hm.quickbo.util.HttpUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class AWSTableGeneratorTest {

  private static Logger log = Logger.getLogger(AWSTableGeneratorTest.class);

  @Autowired
  @Qualifier("config")
  private AWSConfigure conf;

  @Autowired
  @Qualifier("excelTableReader")
  private ExcelTableReader reader;

  /**
   * 创建BO表的功能测试
   */
  @Test
  public void createTable() {
    // Http连接
    try {
      URL url = new URL("http://" + conf.getAwsurl() + "/ajax");
      List<Table> tables = reader.readTables();

      // 先获取SID
      String sid = HttpLogin.getSid(conf.getAwsurl(), conf.getUsername(), conf.getPassword());

      for (Table table : tables) {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try {
          // 参数分析
          String param = HttpTablePaser.createTableAndTableRequestParam(sid, table);

          // 发送请求
          HttpUtil.sendPostRequest(conn, param);

          BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
          String readLine = bufferedReader.readLine();
          while (readLine != null) {
            // 显示消息
            readLine = bufferedReader.readLine();
            log.info(table.getGroupName() + " -- " + table.getTableName() + " -- " + table.getTableTitle() + " -- " + readLine);
          }
        } finally {
          conn.disconnect();
        }
      }
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 创建BO表的功能测试
   */
  @Test
  public void createTableC() {
    // Http连接
    try {
      URL url = new URL("http://" + conf.getAwsurl() + "/ajax");
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      List<Table> table = reader.readTables();

      // 先获取SID
      String sid = HttpLogin.getSid(conf.getAwsurl(), conf.getUsername(), conf.getPassword());

      // 参数分析
      String param = HttpTablePaser.createTableAndTableRequestParam(sid, table.get(0));

      System.out.println(param);

      // 发送请求
      HttpUtil.sendPostRequest(conn, param);

      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String readLine = bufferedReader.readLine();
      while (readLine != null) {
        // 显示消息
        System.out.println(readLine);
        readLine = bufferedReader.readLine();
      }
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
