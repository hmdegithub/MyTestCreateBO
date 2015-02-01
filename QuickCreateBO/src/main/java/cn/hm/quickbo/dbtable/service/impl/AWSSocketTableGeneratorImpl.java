package cn.hm.quickbo.dbtable.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import cn.hm.quickbo.app.mess.PutMessage;
import cn.hm.quickbo.app.mess.SetMessage;
import cn.hm.quickbo.app.window.exp.MessageException;
import cn.hm.quickbo.conf.AWSConfigure;
import cn.hm.quickbo.dbtable.domain.Table;
import cn.hm.quickbo.dbtable.reader.FileTableReader;
import cn.hm.quickbo.dbtable.reader.impl.ExcelSAXTableReader;
import cn.hm.quickbo.dbtable.service.FileTableGenerator;
import cn.hm.quickbo.dbtable.util.HttpTablePaser;

public class AWSSocketTableGeneratorImpl implements FileTableGenerator, SetMessage {

  /**
   * 返回消息读取的最大大小.
   */
  private final static int READMESSAGE_MAXSIZE = 10 * 1024;

  private PutMessage putMessage = null;
  private AWSConfigure conf = AWSConfigure.getInstance();
  private FileTableReader reader = new ExcelSAXTableReader();
  private URL url;

  /**
   * 设置表格读取者.
   * 
   * @param reader
   */
  public void setReader(FileTableReader reader) {
    this.reader = reader;
  }

  /**
   * 设置消息传输接口.
   * 
   * @param putMessage
   */
  @Override
  public void setPutMessage(PutMessage putMessage) {
    this.putMessage = putMessage;
  }

  public void sendMessage(String str) {
    if (putMessage != null)
      putMessage.putMessage(str);
  }

  @Override
  public void startCreate(String filepath) {
    // Table
    sendMessage("读取文件中...");
    reader.setFilepath(filepath);
    List<Table> readTables = reader.readTables();
    sendMessage("文件读取完毕");
    sendMessage("开始建表...");
    saveTables(readTables);
    sendMessage("完成");
  }

  @Override
  public void saveTable(Table table) {
    saveTables(Arrays.asList(table));
  }

  @Override
  public void saveTables(List<Table> list) {
    try {
      url = new URL("http://" + conf.getAwsurl() + "/ajax");
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }

    Socket socket = null;
    try {
      socket = new Socket();
      socket.setKeepAlive(true);
      socket.setSendBufferSize(1024);
      socket.setReceiveBufferSize(1024);
      socket.connect(new InetSocketAddress(url.getHost(), url.getPort()));

      for (Table table : list) {
        createTable(socket, table);
      }
    } catch (UnknownHostException e) {
      throw new MessageException("URL地址填写错误！请检查！", e);
    } catch (IOException e) {
      throw new MessageException("网络错误!", e);
    } finally {
      if (socket != null) {
        try {
          socket.close();
        } catch (IOException e) {
          throw new MessageException("网络错误!", e);
        }
      }
    }
  }

  /**
   * 创建一个表格，并返回一个表格的创建信息.
   * 
   * @param socket
   * @param table
   * @throws IOException
   */
  private void createTable(Socket socket, Table table) throws IOException {
    InputStream inputStream = socket.getInputStream();
    OutputStream outputStream = socket.getOutputStream();

    // 参数分析
    String param = HttpTablePaser.createTableAndTableRequestParam(conf.getSid(), table);

    // 发送请求
    StringBuilder httpRequestStr = new StringBuilder();
    httpRequestStr.append("POST ").append(url.getPath()).append(" HTTP/1.1").append("\n");
    httpRequestStr.append("HOST:").append(url.getHost()).append(":").append(url.getPort()).append("\n");
    httpRequestStr.append("Content-Length: ").append(param.length()).append("\n");
    httpRequestStr.append("X-Requested-With: XMLHttpRequest").append("\n");
    httpRequestStr.append("User-Agent:Mozila/4.0(compatible:MSIE5.01:Windows NT5.0)").append("\n");
    httpRequestStr.append("Connection:Keep-Alive").append("\n");
    httpRequestStr.append("Content-Type: application/x-www-form-urlencoded").append("\n");
    httpRequestStr.append("Accept-Language:zh-cn").append("\n");
    httpRequestStr.append("Accept: */*").append("\n\n");
    httpRequestStr.append(param.toString());
    outputStream.write(httpRequestStr.toString().getBytes());
    outputStream.flush();
    System.out.println(httpRequestStr);

    // 判断是否需要输出消息
    if (putMessage == null)
      return;

    // 用于存储读取的byte数组
    byte[] buffer = new byte[READMESSAGE_MAXSIZE];
    int readLength = inputStream.read(buffer);
    if (readLength != -1) {
      // 显示消息
      sendMessage(table.getGroupName() + " -- " + table.getTableName() + " -- " + table.getTableTitle() + " -- " + new String(buffer, 0, readLength));
    }

    outputStream.flush();
  }

}
