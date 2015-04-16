package cn.hm.psapp.qgform.writer;

import java.io.IOException;

import org.junit.Test;

import cn.hm.psapp.qgform.Form;
import cn.hm.psapp.qgform.FormReader;
import cn.hm.psapp.qgform.config.Configuration;
import cn.hm.psapp.qgform.config.ConfigurationFactory;
import cn.hm.psapp.qgform.reader.AWSFormReader;
import cn.hm.psapp.qgform.sender.AWSHttpFormWrtier;
import cn.hm.psapp.qgform.util.AWSHttpUtils;

public class AWSHttpFormWrtierTest {

  @Test
  public void testWrite() {
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
    
    FormReader reader = new AWSFormReader();
    Form form = reader.read("42e5d26e59123a6b74b9ee8e89fe1ff9");
    AWSHttpFormWrtier writer = new AWSHttpFormWrtier();
    writer.send(form);
  }

}
