package cn.hm.quickbo.dbtable.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.hm.quickbo.conf.AWSConfigure;
import cn.hm.quickbo.util.HttpUtil;
import cn.hm.quickbo.util.ValidateUtil;

public class HttpLogin {

  /**
   * 测试连接，并设置Sid.
   * 
   * @return
   */
  public static boolean testLogin() {
    AWSConfigure awsConfig = AWSConfigure.getInstance();
    try {
      String sid = HttpLogin.getSid(awsConfig.getAwsurl(), awsConfig.getUsername(), awsConfig.getPassword());
      if (sid == null) {
        awsConfig.setSid(null);
        return false;
      } else {
        awsConfig.setSid(sid);
        return true;
      }
    } catch (IOException e) {
      awsConfig.setSid(null);
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 从登陆网页抓取sid
   * 
   * @return
   * @throws MalformedURLException
   * @throws IOException
   */
  public static String getSid(String ip, String username, String password) throws MalformedURLException, IOException {
    String sid = null;

    if (ValidateUtil.validateNullOrEmtpy(ip) || ValidateUtil.validateNullOrEmtpy(username) || ValidateUtil.validateNullOrEmtpy(password)) {
      return sid;
    }

    // 访问URL
    StringBuilder url = new StringBuilder(40);
    // POST参数
    StringBuilder param = new StringBuilder(100);

    // 拼接访问URL和POST参数
    url.append("http://").append(ip).append("/workflow/login.wf");
    param.append("PORTAL_LANG=cn&_CACHE_LOGIN_TIME_=1404357464784&cmd=Login&pwd=").append(password).append("&userid=").append(username);

    // 获取HTTP链接
    HttpURLConnection openConnection = (HttpURLConnection) new URL(url.toString()).openConnection();
    try {
      // 发送POST请求
      try {
        HttpUtil.sendPostRequest(openConnection, param.toString());
      } finally {
        openConnection.getOutputStream().close();
      }

      try {
        // 获取服务器回传的信息并分析出SID的值
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(openConnection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String readLine = bufferedReader.readLine();
        while (readLine != null) {
          sb.append(readLine);
          readLine = bufferedReader.readLine();
        }

        int index;
        if ((index = sb.indexOf("sid=\"")) != -1) {
          String substring = sb.substring(index + "sid=\"".length());
          sid = substring.substring(0, substring.indexOf("&"));
        } else if ((index = sb.indexOf("sid=")) != -1) {
          String substring = sb.substring(index + "sid=".length());
          sid = substring.substring(0, substring.indexOf("&"));
        } else if ((index = sb.indexOf("sid='")) != -1) {
          String substring = sb.substring(index + "sid='".length());
          sid = substring.substring(0, substring.indexOf("&"));
        }

        // 未找到SID的值
        if (index == -1) {
          return null;
        } else {
          return sid;
        }
      } finally {
        openConnection.getInputStream().close();
      }
    } finally {
      openConnection.disconnect();
    }
  }
}
