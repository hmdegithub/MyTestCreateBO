package cn.hm.psapp.qgform.paser;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cn.hm.psapp.qgform.Field;
import cn.hm.psapp.qgform.Form;
import cn.hm.psapp.qgform.FormPaser;
import cn.hm.psapp.qgform.find.BytesFindProcesser;
import cn.hm.psapp.qgform.find.impl.BMBytesFindProcesser;
import cn.hm.psapp.qgform.paser.html.OgnlRuleHtmlFormatter;
import cn.hm.psapp.qgform.util.IOUtils;

public class OgnlMatchFormPaser implements FormPaser {

  private String templateFilePath;
  private BytesFindProcesser replaceProcesser = new BMBytesFindProcesser();
  private OgnlRuleHtmlFormatter parser = new OgnlRuleHtmlFormatter();

  public OgnlMatchFormPaser(String templateFilePath) {
    this.templateFilePath = templateFilePath;
  }

  @Override
  public String parse(Form form) {
    Map<String, String> paramMap = new HashMap<String, String>();
    paramMap.put("[@FORMNAME]", form.getName());
    paramMap.put("[@HIDDEN]", parseHidden(form));
    paramMap.put("[@HEAD]", parseHead(form));
    paramMap.put("[@BODY]", parseBody(form));
    try {
      return convertTemplateFile(paramMap);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 获取隐藏字段.
   * 
   * @param form
   * @return
   */
  public String parseHidden(Form form) {
    List<Field> fieldList = form.getMainTable().getFieldList();
    StringBuilder sb = new StringBuilder();
    for (Field field : fieldList) {
      if (field.getIsDisplay() == Field.NO) {
        sb.append("[@").append(field.getFieldName()).append("]");
      }
    }
    return sb.toString();
  }

  /**
   * 转换单头.
   * 
   * @param form
   * @return
   */
  public String parseHead(Form form) {
    return parser.build(form);
  }

  /**
   * 转换子表.
   * 
   * @param form
   * @return
   */
  public String parseBody(Form form) {
    return parser.buildSub(form);
  }

  /**
   * 转换模板文件.`
   * 
   * @param paramMap
   * @return
   * @throws IOException
   */
  public String convertTemplateFile(Map<String, String> paramMap) throws IOException {
    FileInputStream fileInput = null;
    byte[] array = null;
    try {
      fileInput = new FileInputStream(templateFilePath);
      array = IOUtils.read(fileInput);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      if (fileInput != null) {
        try {
          fileInput.close();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    }

    Set<Entry<String, String>> entrySet = paramMap.entrySet();
    for (Entry<String, String> entry : entrySet) {
      String key = entry.getKey();
      String value = entry.getValue();
      array = replaceProcesser.replace(array, key.getBytes(), value.getBytes());
    }

    if (array == null) {
      throw new RuntimeException("模板文件读取失败!");
    }

    return new String(array);
  }
}
