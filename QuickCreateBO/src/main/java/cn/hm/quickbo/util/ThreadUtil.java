package cn.hm.quickbo.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtil {

  public static final int POOL_SIZE = 3;

  private ThreadUtil() {
  }

  public static ExecutorService getThreadPool() {
    return Executors.newFixedThreadPool(POOL_SIZE);
  }

}