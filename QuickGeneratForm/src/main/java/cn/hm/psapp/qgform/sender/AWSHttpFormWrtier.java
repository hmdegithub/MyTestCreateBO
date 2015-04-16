package cn.hm.psapp.qgform.sender;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import cn.hm.psapp.qgform.Form;
import cn.hm.psapp.qgform.FormPaser;
import cn.hm.psapp.qgform.FormSender;
import cn.hm.psapp.qgform.config.ConfigurationFactory;
import cn.hm.psapp.qgform.paser.OgnlMatchFormPaser;
import cn.hm.psapp.qgform.util.AWSHttpUtils;

public class AWSHttpFormWrtier implements FormSender {

  private FormPaser formPaser = new OgnlMatchFormPaser(ConfigurationFactory.loadJson().getTemplateFile());

  private static final String url;
  private static final String sid;

  static {
    url = ConfigurationFactory.loadJson().getUrl();
    sid = ConfigurationFactory.loadJson().getSid();
  }

  @Override
  public void send(Form form) {
    List<NameValuePair> requestParams = convertFormToParams(form);
    AWSHttpUtils.sendSaveFormRequest(url, requestParams);
  }

  /**
   * 将Form对象转为请求参数.
   * 
   * @param form
   * @return
   */
  public List<NameValuePair> convertFormToParams(Form form) {
    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
    nvps.add(new BasicNameValuePair("modelName", form.getModelName()));
    nvps.add(new BasicNameValuePair("sheetId", "0"));
    nvps.add(new BasicNameValuePair("sid", sid));
    nvps.add(new BasicNameValuePair("cmd", "WorkFlow_Design_Form_HtmlEditSave"));
    nvps.add(new BasicNameValuePair("reportId", String.valueOf(form.getId())));
    nvps.add(new BasicNameValuePair("htmlContext", formPaser.parse(form)));
    nvps.add(new BasicNameValuePair("htmlName", form.getModelName()));
    nvps.add(new BasicNameValuePair("subModelName", form.getModelName()));
    nvps.add(new BasicNameValuePair("attachment", ""));
    nvps.add(new BasicNameValuePair("isRepeat", ""));
    return nvps;
  }
}
