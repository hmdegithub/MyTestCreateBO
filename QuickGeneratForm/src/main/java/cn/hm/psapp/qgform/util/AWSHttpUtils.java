package cn.hm.psapp.qgform.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import cn.hm.psapp.qgform.find.impl.BMBytesFindProcesser;

public class AWSHttpUtils {

  private AWSHttpUtils() {
  }

  /**
   * 发送保存表单请求.
   * 
   * @param url
   * @param nvps
   */
  public static void sendSaveRequest(String url, List<NameValuePair> nvps) {
    CloseableHttpClient httpClient = HttpClients.createMinimal();
    HttpPost httpPos = new HttpPost(url);
    httpPos.setHeader(new BasicHeader("Accept-Language", "zh-CN,zh;q=0.8"));
    httpPos.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded"));
    httpPos.setHeader(new BasicHeader("User-Agent",
            "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.99 Safari/537.36"));
    httpPos.setHeader(new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"));
    try {
      httpPos.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }

    try {
      httpClient.execute(httpPos);
    } catch (ClientProtocolException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 获取SID.
   * 
   * @param url
   * @param username
   * @param password
   * @return
   * @throws MalformedURLException
   * @throws IOException
   */
  public static String getSid(String url, String username, String password) throws MalformedURLException, IOException {
    if (ValidateUtil.validateNullOrEmtpy(url) || ValidateUtil.validateNullOrEmtpy(username) || ValidateUtil.validateNullOrEmtpy(password)) {
      throw new RuntimeException("IP、用户名、密码不能为空!");
    }

    List<NameValuePair> requestParams = new ArrayList<NameValuePair>(10);
    requestParams.add(new BasicNameValuePair("userid", username));
    requestParams.add(new BasicNameValuePair("pwd", password));
    requestParams.add(new BasicNameValuePair("cmd", "LoginBPM"));
    requestParams.add(new BasicNameValuePair("PORTAL_LANG", "cn"));

    CloseableHttpClient httpClient = HttpClients.createMinimal();
    HttpPost httpPost = new HttpPost(url);
    httpPost.setHeader(new BasicHeader("Accept-Language", "zh-CN,zh;q=0.8"));
    httpPost.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded"));
    httpPost.setHeader(new BasicHeader("User-Agent",
            "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.99 Safari/537.36"));
    httpPost.setHeader(new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"));
    httpPost.setEntity(new UrlEncodedFormEntity(requestParams, "utf-8"));

    CloseableHttpResponse response = httpClient.execute(httpPost);
    byte[] bytes = IOUtils.read(response.getEntity().getContent());

    BMBytesFindProcesser findPorcesser = new BMBytesFindProcesser();

    int indexS = findPorcesser.findBytes(bytes, "sid=".getBytes("UTF-8"));
    if (indexS == -1) {
      throw new RuntimeException("用户名或密码错误!");
    }
    indexS = indexS + "sid=".length();

    int indexE = findPorcesser.findBytes(bytes, indexS, "&".getBytes());
    if (indexE == -1) {
      throw new RuntimeException("sid获取失败!");
    }

    return new String(bytes, indexS, indexE - indexS);
  }
}
