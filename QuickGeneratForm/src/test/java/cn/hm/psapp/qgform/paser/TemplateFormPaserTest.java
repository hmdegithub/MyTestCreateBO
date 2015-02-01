package cn.hm.psapp.qgform.paser;

import java.io.IOException;

import org.junit.Test;

import cn.hm.psapp.qgform.Form;
import cn.hm.psapp.qgform.FormPaser;
import cn.hm.psapp.qgform.FormReader;
import cn.hm.psapp.qgform.config.ConfigConstant;
import cn.hm.psapp.qgform.reader.AWSFormReader;
import cn.hm.psapp.qgform.util.AWSHttpUtils;

public class TemplateFormPaserTest {

  @Test
  public void testParse() {
    FormReader reader = new AWSFormReader();
    Form form = reader.read("42e5d26e590d1fdb7e7e1a66bc95ba14");
    FormPaser paser = new TemplateFormPaser(ConfigConstant.templateFile);
    System.out.println(paser.parse(form));
  }
  
  @Test
  public void testLogin(){
    try {
      String sid = AWSHttpUtils.getSid("http://10.10.10.201:8089/workflow/login.wf", "admin", "aklhello123,.");
      System.out.println(sid);
      System.out.println(sid.length());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
