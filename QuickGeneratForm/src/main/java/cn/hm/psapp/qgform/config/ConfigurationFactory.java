package cn.hm.psapp.qgform.config;

import java.io.FileReader;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConfigurationFactory {

  private static Configuration jsonConfig = null;

  public static void main(String[] args) throws Error {
    loadJson();
  }

  public static Configuration loadJson() throws Error {
    if (jsonConfig == null) {
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
      FileReader fileReader;
      try {
        fileReader = new FileReader("D:\\developer\\workspace\\github-repostiory\\MyTestCreateBO\\QuickGeneratForm\\src\\main\\resources\\config.properties");
        Configuration config = objectMapper.readValue(fileReader, Configuration.class);
        jsonConfig = config;
      } catch (Exception e) {
        throw new Error("配置读取错误!\n" + e.getMessage());
      }
    }
    return jsonConfig;
  }
}
