package cn.hm.psapp.qgform.reader;

import org.junit.Test;

import cn.hm.psapp.qgform.Form;
import cn.hm.psapp.qgform.FormReader;
import cn.hm.psapp.qgform.reader.AWSFormReader;

public class AWSFormReaderTest {

  private FormReader reader = new AWSFormReader();

  @Test
  public void testRead() {
    Form form = reader.read("42e5d26e590e6feb1b39ffd79126539e");
    System.out.println(form);
  }
  
}
