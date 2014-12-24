package cn.hm.quickbo.dbtable.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import cn.hm.quickbo.app.mess.PutMessage;
import cn.hm.quickbo.app.mess.SetMessage;
import cn.hm.quickbo.conf.AWSConfigure;
import cn.hm.quickbo.dbtable.domain.Table;
import cn.hm.quickbo.dbtable.reader.TableReader;
import cn.hm.quickbo.dbtable.reader.impl.ExcelSAXTableReader;
import cn.hm.quickbo.dbtable.service.FileTableGenerator;
import cn.hm.quickbo.dbtable.util.HttpLogin;
import cn.hm.quickbo.dbtable.util.HttpTablePaser;
import cn.hm.quickbo.util.HttpUtil;

public class AWSTableGeneratorImpl implements FileTableGenerator, SetMessage {

  // private static Logger log = Logger.getLogger(AWSTableGeneratorImpl.class);

  private PutMessage putMessage = null;
  private AWSConfigure conf = AWSConfigure.getInstance();
  private TableReader reader;
  private URL url;

  public void startCreate(String filepath) {
    // Table
    sendMessage("读取文件中...");
    reader = new ExcelSAXTableReader(filepath);
    List<Table> readTables = reader.readTables();
    sendMessage("文件读取完毕");
    sendMessage("开始建表...");
    saveTables(readTables);
    sendMessage("完成");
  }

  /**
   * 设置消息传输接口.
   * 
   * @param putMessage
   */
  public void setPutMessage(PutMessage putMessage) {
    this.putMessage = putMessage;
  }

  public void sendMessage(String str) {
    if (putMessage != null)
      putMessage.putMessage(str);
  }

  @Override
  public void saveTable(Table table) {
    try {
      // 先获取SID
      String sid = HttpLogin.getSid(conf.getAwsurl(), conf.getUsername(), conf.getPassword());
      // Http连接
      URL url = new URL("http://" + conf.getAwsurl() + "/ajax");
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      String param = HttpTablePaser.createTableAndTableRequestParam(sid, table);
      // 发送请求
      HttpUtil.sendPostRequest(conn, param);
      // 读取响应参数.
      String response = HttpUtil.readResponse(conn);
      // 显示消息
      System.out.println(response);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void saveTables(List<Table> list) {
    try {
      url = new URL("http://" + conf.getAwsurl() + "/ajax");
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
    for (Table table : list) {
      try {
        createTable(table);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private void createTable(Table table) throws IOException, UnsupportedEncodingException {
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    try {
      // 参数分析
      String param = HttpTablePaser.createTableAndTableRequestParam(conf.getSid(), table);

      // 发送请求
      HttpUtil.sendPostRequest(conn, param);

      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

      synchronized (AWSTableGeneratorImpl.this) {
        String readLine = bufferedReader.readLine();
        while (readLine != null) {
          // 显示消息
          // log.info(table.getGroupName() + " -- " + table.getTableName() +
          // " -- " + table.getTableTitle() + " -- " + readLine);
          if (putMessage != null)
            sendMessage(table.getGroupName() + " -- " + table.getTableName() + " -- " + table.getTableTitle() + " -- " + readLine);

          readLine = bufferedReader.readLine();
        }
      }
    } finally {
      conn.disconnect();
    }
  }

}
