package cn.hm.psapp.qgform.config;

import java.util.Map;

public class Configuration {

  public Map<String, String> app;
  public Map<String, String> aws;
  public Map<String, String> jdbc;
  public Map<String, Object> form;

  public void setAws(Map<String, String> aws) {
    this.aws = aws;
  }
  public void setApp(Map<String, String> app) {
    this.app = app;
  }
  public void setJdbc(Map<String, String> jdbc) {
    this.jdbc = jdbc;
  }
  public void setForm(Map<String, Object> form) {
    this.form = form;
  }
  
  public Map<String, String> getApp() {
    return app;
  }
  
  public Map<String, String> getAws() {
    return aws;
  }
  
  public Map<String, String> getJdbc() {
    return jdbc;
  }
  public String getUrl() {
    return aws.get("url");
  }
  public String getSid() {
    return aws.get("sid");
  }
  public void setSid(String sid) {
    aws.put("sid", sid);
  }
  public String getJdbcUrl() {
    return jdbc.get("url");
  }
  public String getJdbcDriver() {
    return jdbc.get("driver");
  }
  public String getJdbcUsername() {
    return jdbc.get("username");
  }
  public String getJdbcPassword() {
    return jdbc.get("password");
  }
  public String getTemplateFile() {
    return app.get("templateFile");
  }
  public Map<String, Object> getForm() {
    return form;
  }

  @Override
  public String toString() {
    return "Configuration [app=" + app + ", aws=" + aws + ", jdbc=" + jdbc + ", form=" + form + "]";
  }

}
