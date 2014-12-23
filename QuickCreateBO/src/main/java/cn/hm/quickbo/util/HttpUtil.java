package cn.hm.quickbo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;

public class HttpUtil {

  /**
   * 读取响应内容.
   * @param conn
   * @return
   * @throws IOException
   */
  public static String readResponse(HttpURLConnection conn) throws IOException{
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    StringBuilder sb = new StringBuilder();
    String readLine = null;
    while((readLine=bufferedReader.readLine())!=null){
      sb.append(readLine);
    }
    return sb.toString();
  }
  
  /**
   * 发送POST请求.
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

  /**
   * 发送POS请求，并获取消息.
   * @param conn
   * @param param
   * @return
   * @throws IOException
   */
  public static String sendPostRequestAndGetMessage(HttpURLConnection conn, String param) throws IOException {
    conn.setRequestMethod("POST");
    conn.setDoInput(true);
    conn.setDoOutput(true);
    conn.setRequestProperty("content-type", "html/text;charset=utf-8");
    conn.setRequestProperty("accept", "*\\/*");
    conn.setRequestProperty("connection", "Keep-Alive");
    conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
    PrintWriter printWriter = new PrintWriter(conn.getOutputStream());
    printWriter.print(param.toString());
    printWriter.flush();

    StringBuilder sbuilder = new StringBuilder();
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
    try {
      String readLine = bufferedReader.readLine();
      while (readLine != null) {
        sbuilder.append(readLine);
        readLine = bufferedReader.readLine();
      }
      // 因返回的时候总会夹杂着 <script>
      // alert("重要提示:\n为保护您的数据安全，应该首先修改您的登录口令!");</script>
      // 这样的数据，所以需要把这些无关数据清除掉。
      int index = sbuilder.indexOf("<script> alert(\"");
      return index == -1 ? sbuilder.toString() : sbuilder.replace(index, sbuilder.indexOf("</script>") + "</script>".length(), "").toString();
    } finally {
    }
  }

}
