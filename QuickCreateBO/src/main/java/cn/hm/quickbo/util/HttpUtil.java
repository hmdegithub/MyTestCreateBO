package cn.hm.quickbo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;

public class HttpUtil {
  
  private HttpUtil(){}

  /**
   * 读取响应内容.
   * 
   * @param conn
   * @return
   * @throws IOException
   */
  public static String readResponse(HttpURLConnection conn) throws IOException {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    StringBuilder sb = new StringBuilder();
    String readLine = null;
    while ((readLine = bufferedReader.readLine()) != null) {
      sb.append(readLine);
    }
    return sb.toString();
  }

  /**
   * 发送POST请求.
   * 
   * @param conn
   * @param param
   * @throws IOException
   */
  public static void sendPostRequest(HttpURLConnection conn, String param) throws IOException {
    conn.setRequestMethod("POST");
    conn.setDoInput(true);
    conn.setDoOutput(true);
    conn.setRequestProperty("accept", "*\\/*");
    conn.setRequestProperty("connection", "Keep-Alive");
    conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
    PrintWriter printWriter = new PrintWriter(conn.getOutputStream());
    printWriter.print(param.toString());
    printWriter.flush();
  }

}
