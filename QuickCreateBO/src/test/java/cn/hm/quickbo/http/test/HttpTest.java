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

  public static final String host = "10.10.10.236";
  public static final String url = "/index_blue.jsp";
  public static final String path = "/";
  public static final int port = 80;
  public static final byte[] req = ("GET " + url+ " HTTP/1.0\r\nHost:" + url + " \r\n\r\n").getBytes();

  @Test
  public void testSocketConnection() throws IOException {
    Socket socket = new Socket(host, port);
//    PrintWriter out = new PrintWriter(socket.getOutputStream());
    socket.getOutputStream().write(req);
//    out.println("HOST:" + host + "/workflow/login.wf");
//    out.println("User-Agent:Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; .NET CLR 3.0.04506.648; .NET CLR 3.5.21022)");
//    out.println("Content-Type:application/x-www-form-urlencoded");
//    out.println("Content-Length:40");
//    out.println("Connection:Keep-Alive");
//    out.println();
//    out.println("name=Professional%20Ajax&publisher=Wiley");
//    out.flush();
    socket.getOutputStream().flush();
    
    StringBuilder response = new StringBuilder();
    Scanner in = new Scanner(socket.getInputStream());
    while (in.hasNextLine()) {
      response.append(in.nextLine());
      response.append("\n");
    }

     System.out.println(response);
     
     socket.getOutputStream().write(req);
     socket.getOutputStream().flush();
     
     in = new Scanner(socket.getInputStream());
     while (in.hasNextLine()) {
       response.append(in.nextLine());
       response.append("\n");
     }

      System.out.println(response);
     
    socket.close();
  }

  @Test
  public void threadPoolURLConnection() throws IOException {
    ServerSocket server = new ServerSocket(8089);
    Socket acceptSocket = server.accept();

    PrintWriter out = new PrintWriter(acceptSocket.getOutputStream());
    out.println("<html><head></head><body>1</body></html>");
    out.flush();
    acceptSocket.shutdownOutput();

    StringBuilder response = new StringBuilder();
    Scanner in = null;
    try {
      in = new Scanner(acceptSocket.getInputStream());
      while (in.hasNextLine()) {
        response.append(in.nextLine());
        response.append("\n");
      }
      acceptSocket.shutdownInput();
    } finally {
      if (in != null) {
        in.close();
      }
    }
    acceptSocket.close();
    server.close();
  }

}
