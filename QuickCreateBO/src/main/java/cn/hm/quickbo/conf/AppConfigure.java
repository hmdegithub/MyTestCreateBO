package cn.hm.quickbo.conf;

/**
 * 应用程序配置.
 * 
 * @author huangming
 *
 */
public class AppConfigure {

  private static AppConfigure config = new AppConfigure();

  /**
   * 应用程序名.
   */
  private String appName = "BO存储创建工具";
  /**
   * 版本.
   */
  private String version = "v0.1";
  /**
   * 描述.
   */
  private String desc = "";

  private AppConfigure() {
  }

  public static AppConfigure getInstance() {
    return config;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getAppName() {
    return appName;
  }
  
  public void setAppName(String appName) {
    this.appName = appName;
  }
  

}
