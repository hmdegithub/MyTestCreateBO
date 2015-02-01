package cn.hm.psapp.qgform.writer;

import java.io.IOException;

import org.junit.Test;

import cn.hm.psapp.qgform.Form;
import cn.hm.psapp.qgform.FormReader;
import cn.hm.psapp.qgform.config.ConfigConstant;
import cn.hm.psapp.qgform.reader.AWSFormReader;
import cn.hm.psapp.qgform.sender.AWSHttpFormWrtier;
import cn.hm.psapp.qgform.util.AWSHttpUtils;

public class AWSHttpFormWrtierTest {

  @Test
  public void testWrite() {
    try {
      String sid = AWSHttpUtils.getSid("http://10.10.10.201:8089/workflow/login.wf", "admin", "aklhello123,.");
      ConfigConstant.sid = sid;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    FormReader reader = new AWSFormReader();
    Form form = reader.read("706b095c579273f0657fdf79d6549880");
    AWSHttpFormWrtier writer = new AWSHttpFormWrtier();
    writer.send(form);
  }

}
