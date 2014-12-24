package cn.hm.quickbo.dbtable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.hm.quickbo.conf.AWSConfigure;
import cn.hm.quickbo.dbtable.service.impl.AWSQuickTableGeneratorImpl;
import cn.hm.quickbo.dbtable.service.impl.AWSTableGeneratorImpl;
import cn.hm.quickbo.dbtable.util.HttpLogin;

public class AWSQuickTableGeneratorTest {

  public long startTime;
  private String filename = "C:/Users/huangming/Desktop/文档/shouhoubo.xlsx";

  public AWSQuickTableGeneratorTest() {
    AWSConfigure config = AWSConfigure.getInstance();
    config.setAwsurl("localhost:8088/portal");
    config.setUsername("admin");
    config.setPassword("123456");
    HttpLogin.testLogin();
  }

  @Before
  public void before() {
    startTime = System.currentTimeMillis();
  }

  @After
  public void after() {
    long nowTime = System.currentTimeMillis();
    long freeMemory = Runtime.getRuntime().freeMemory();
    long maxMemory = Runtime.getRuntime().maxMemory();
    long totalMemory = Runtime.getRuntime().totalMemory();

    System.out.println("消耗时间:" + (nowTime - startTime));
    System.out.println("空闲内存: " + freeMemory);
    System.out.println("最大内存: " + maxMemory);
    System.out.println("总内存: " + totalMemory);
  }

  @Test
  public void test1() {
    AWSQuickTableGeneratorImpl generator = new AWSQuickTableGeneratorImpl();
    generator.startCreate(filename);
  }
  
  @Test
  public void test2() {
    AWSTableGeneratorImpl generator = new AWSTableGeneratorImpl();
    generator.startCreate(filename);
  }
  
}
