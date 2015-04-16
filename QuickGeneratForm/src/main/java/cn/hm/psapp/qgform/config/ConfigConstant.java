package cn.hm.psapp.qgform.config;

public class ConfigConstant {

  private static int CACHE_TABLE_FIELD_SIZE = 150;
  private static String url = "http://10.10.10.201:8089/workflow/login.wf";
  private static String sid = "admin_1422453770265_192.168.80.147$2415acba3ad002d01b6cb49834709dfbL{cn}LC{pc}C";
  private static String jdbcUrl = "jdbc:jtds:sqlserver://10.10.10.201:1433/AKL_BPM;characterEncoding=UTF-8";
  private static String jdbcClass = "net.sourceforge.jtds.jdbc.Driver";
  private static String jdbcUsername = "sa";
  private static String jdbcPassword = "hello123";
  private static String templateFile = "D:/developer/workspace/github-repostiory/MyTestCreateBO/QuickGeneratForm/src/main/resources/template2.html";

  /** 初始化列数 */
  private static final int INIT_COL_NUM = 6;
  /** 初始化录入宽度 */
  private static final int DEFAULT_INPUTWIDTH = 28;
}
