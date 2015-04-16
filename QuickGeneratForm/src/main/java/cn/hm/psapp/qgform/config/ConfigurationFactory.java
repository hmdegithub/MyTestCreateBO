package cn.hm.psapp.qgform.config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;

import cn.hm.psapp.qgform.db.DBUtil;
import cn.hm.psapp.qgform.util.AWSHttpUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConfigurationFactory {

  private static Configuration jsonConfig = null;

  public synchronized static Configuration loadJson() throws Error {
    if (jsonConfig == null) {
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
      FileReader fileReader;
      try {
        fileReader = new FileReader("D:\\developer\\workspace\\github-repostiory\\MyTestCreateBO\\QuickGeneratForm\\src\\main\\resources\\config.properties");
        Configuration config = objectMapper.readValue(fileReader, Configuration.class);
        jsonConfig = config;

        /** 数据校验. **/
        // 模板文件.
        boolean canRead = new File(config.getTemplateFile()).canRead();
        if (!canRead) {
          throw new RuntimeException(config.getTemplateFile() + " 文件不可读取！");
        }

        // 数据库.
        try {
          Class.forName(config.getJdbcDriver());
        } catch (ClassNotFoundException e) {
          throw new RuntimeException(e);
        }
        Connection conn = null;
        try {
          conn = DBUtil.open();
        } finally {
          DBUtil.close(conn);
        }

        // AWS链接.
        String url = config.getAws().get("url");
        String username = config.getAws().get("username");
        String password = config.getAws().get("password");

        try {
          String sid = AWSHttpUtils.getSid(url, username, password);
          config.setSid(sid);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }

      } catch (Exception e) {
        throw new Error("配置读取错误!\n" + e.getMessage());
      }
    }
    return jsonConfig;
  }
}
