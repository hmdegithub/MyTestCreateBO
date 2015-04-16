package cn.hm.psapp.qgform.paser;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import cn.hm.psapp.qgform.Form;
import cn.hm.psapp.qgform.FormReader;
import cn.hm.psapp.qgform.config.Configuration;
import cn.hm.psapp.qgform.config.ConfigurationFactory;
import cn.hm.psapp.qgform.reader.AWSFormReader;
import cn.hm.psapp.qgform.util.AWSHttpUtils;

public class OgnlRuleHtmlFormatterTest {

  private String templateFile = "";

  @Before
  public void init() {
    templateFile = ConfigurationFactory.loadJson().getTemplateFile();

    Configuration config = ConfigurationFactory.loadJson();
    String url = config.getAws().get("url");
    String username = config.getAws().get("username");
    String password = config.getAws().get("password");

    try {
      String sid = AWSHttpUtils.getSid(url, username, password);
      config.setSid(sid);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testBuild() {
    FormReader reader = new AWSFormReader();
    Form form = reader.read("42e5d26e590d1fdb7e7e1a66bc95ba14");
    OgnlMatchFormPaser paser = new OgnlMatchFormPaser(templateFile);
    System.out.println(paser.parse(form));
  }

}
