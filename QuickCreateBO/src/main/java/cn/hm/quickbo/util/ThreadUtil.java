package cn.hm.quickbo.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtil {

  private ThreadUtil() {
  }

  private static ExecutorService executorService = Executors.newFixedThreadPool(3);

  public static ExecutorService getThreadPool() {
    return executorService;
  }

}