package cn.hm.quickbo.http.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Scanner;

import org.junit.Test;

public class HttpTest {

  @Test
  public void testURLConnection() throws IOException {
    URL url = new URL("http://www.baidu.com/");
    URLConnection connection = url.openConnection();
    // connection.setDoInput(true);
    connection.setDoOutput(true);

    PrintWriter out = new PrintWriter(connection.getOutputStream());
    String params = "a=1&b=2";
    out.print(URLEncoder.encode(params, "UTF-8"));
    out.close();

    Scanner in;
    StringBuilder response = new StringBuilder();
    in = new Scanner(connection.getInputStream());
    while (in.hasNextLine()) {
      response.append(in.nextLine());
      response.append("\n");
    }
    in.close();

    System.out.println(response.toString());
  }

  @Test
  public void testSocketConnection() throws IOException {
    InetAddress i4d = Inet4Address.getByName("192.168.56.1");
    Socket socket = new Socket();
    socket.connect(new InetSocketAddress(i4d, 8088));

    PrintWriter out = new PrintWriter(socket.getOutputStream());
    out.println("POST / HTTP1.1");
    out.println("HOST:127.0.0.1:8088/workflow/login.wf");
    out.println("User-Agent:Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; .NET CLR 3.0.04506.648; .NET CLR 3.5.21022)");
    out.println("Content-Type:application/x-www-form-urlencoded");
    out.println("Content-Length:40");
    out.println("Connection:Keep-Alive");
    out.println();
    out.println("name=Professional%20Ajax&publisher=Wiley");
    out.flush();
    socket.shutdownOutput();

    Scanner in = null;
    StringBuilder response = new StringBuilder();
    in = new Scanner(socket.getInputStream());
    while (in.hasNextLine()) {
      response.append(in.nextLine());
      response.append("\n");
    }
    socket.shutdownInput();
    socket.close();
  }

  @Test
  public void threadPoolURLConnection() throws IOException {
    ServerSocket server = new ServerSocket(8089);
    Socket acceptSocket = server.accept();

    PrintWriter out = new PrintWriter(acceptSocket.getOutputStream());
//    out.println("HTTP/1.1 200 OK");
//    out.println("Date: Fri, 22 May 2009 06:07:21 GMT");
//    out.println("Content-Type: text/html; charset=UTF-8");
//    out.println();
    out.println("<html><head></head><body>1</body></html>");
    out.flush();
    acceptSocket.shutdownOutput();

    Scanner in = null;
    StringBuilder response = new StringBuilder();
    in = new Scanner(acceptSocket.getInputStream());
    while (in.hasNextLine()) {
      response.append(in.nextLine());
      response.append("\n");
    }
    acceptSocket.shutdownInput();

    acceptSocket.close();
    server.close();
  }

  @Test
  public void threadPoolSocketConnection() {

  }

}
