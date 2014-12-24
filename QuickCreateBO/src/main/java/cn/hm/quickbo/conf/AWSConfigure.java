package cn.hm.quickbo.conf;


public class AWSConfigure {

  AWSConfigure() {
  }

  private static AWSConfigure config;

  public synchronized static AWSConfigure getInstance() {
    if (config == null) {
      config = new AWSConfigure();
    }
    return config;
  }

  /**
   * sid.
   */
  private String sid;
  /**
   * aws平台ip地址.
   */
  private String awsurl;
  /**
   * aws平台用户名.
   */
  private String username;
  /**
   * aws平台密码.
   */
  private String password;

  public String getSid() {
    return sid;
  }
  public void setSid(String sid) {
    this.sid = sid;
  }
  public String getAwsurl() {
    return awsurl;
  }
  public void setAwsurl(String awsurl) {
    String[] prefix = { "http://" };
    String[] subfix = { "/console/", "/console/index_blue.jsp", "/console", "/portal/workflow/login.wf", "/" };
    String temp = awsurl.trim();

    // 去除前缀.
    for (String string : prefix) {
      if (awsurl.startsWith(string)) {
        temp = temp.substring(string.length());
      }
    }
    // 去除后缀.
    for (String string : subfix) {
      if (awsurl.endsWith(string)) {
        temp = temp.substring(0, temp.length() - string.length());
      }
    }

    this.awsurl = temp;
  }
  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }

}
