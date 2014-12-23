package cn.hm.quickbo.dbtable.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import cn.hm.quickbo.app.mess.PutMessage;
import cn.hm.quickbo.app.mess.SetMessage;
import cn.hm.quickbo.conf.AWSConfigure;
import cn.hm.quickbo.dbtable.domain.Table;
import cn.hm.quickbo.dbtable.reader.TableReader;
import cn.hm.quickbo.dbtable.reader.impl.ExcelSAXTableReader;
import cn.hm.quickbo.dbtable.service.TableGenerator;
import cn.hm.quickbo.dbtable.util.HttpTablePaser;
import cn.hm.quickbo.util.HttpUtil;
import cn.hm.quickbo.util.ThreadUtil;

public class AWSQuickTableGeneratorImpl implements TableGenerator, SetMessage {

  private PutMessage putMessage = null;
  private AWSConfigure conf = AWSConfigure.getInstance();
  private TableReader reader;
  private URL url;

  private Iterator<Table> iterator;

  private CountDownLatch threadSingal;

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

  public void sendMessage(String str) {
    if (putMessage != null)
      putMessage.putMessage(str);
  }

  /**
   * 设置消息传输接口.
   * 
   * @param putMessage
   */
  public void setPutMessage(PutMessage putMessage) {
    this.putMessage = putMessage;
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

    iterator = list.iterator();

    ExecutorService threadPool = ThreadUtil.getThreadPool();
    threadSingal = new CountDownLatch(list.size());
    for (int i = 0; i < ThreadUtil.POOL_SIZE; i++) {
      threadPool.execute(new Runnable() {

        public void run() {
          while (hasNext()) {
            try {
              createTable(next());
            } finally {
              threadSingal.countDown();
            }
          }
        }
      });
    }
    try {
      threadSingal.await();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    threadPool.shutdown();
  }

  public Table next() {
    synchronized (this) {
      return iterator.next();
    }
  }
  public boolean hasNext() {
    synchronized (this) {
      return iterator.hasNext();
    }
  }

  private void createTable(Table table) {
    HttpURLConnection conn = null;
    try {
      conn = (HttpURLConnection) url.openConnection();
      // 参数分析
      String param = HttpTablePaser.createTableAndTableRequestParam(conf.getSid(), table);

      // 发送请求
      HttpUtil.sendPostRequest(conn, param);

      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      synchronized (putMessage) {
        String readLine = bufferedReader.readLine();
        while (readLine != null) {
          // 显示消息
          if (putMessage != null) {
            sendMessage(table.getGroupName() + " -- " + table.getTableName() + " -- " + table.getTableTitle() + " -- " + readLine);
          }
          readLine = bufferedReader.readLine();
        }
      }
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      conn.disconnect();
    }
  }

}
