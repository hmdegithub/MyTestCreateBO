package cn.hm.psapp.qgform.run;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import cn.hm.psapp.qgform.Form;
import cn.hm.psapp.qgform.FormReader;
import cn.hm.psapp.qgform.FormSender;
import cn.hm.psapp.qgform.Table;
import cn.hm.psapp.qgform.config.ConfigurationFactory;
import cn.hm.psapp.qgform.dao.AWSDao;
import cn.hm.psapp.qgform.db.DBUtil;
import cn.hm.psapp.qgform.format.ExpressMatchFieldWidthFormat;
import cn.hm.psapp.qgform.reader.AWSFormReader;
import cn.hm.psapp.qgform.sender.AWSHttpFormWrtier;

public class StartUp {

  public static void main(String[] args) {
    System.out.println("开始读取配置...");
    ConfigurationFactory.loadJson();
    System.out.println("配置读取完毕...");
    Scanner scanner = null;
    try {
      scanner = new Scanner(System.in);
      System.out.println("请输入表单uuid:");
      String uuid = scanner.nextLine();

      while (!"QUIT".equals(uuid.toUpperCase())) {
        System.out.println("开始读取表单...");
        // 读取表单
        Form form = buildForm(uuid);
        System.out.println("上传表单中...");
        // 发送表单.
        FormSender sender = new AWSHttpFormWrtier();
        sender.send(form);
        System.out.println("上传完毕");
        // 改变宽度.
        modifyWidth(form);
        System.out.println("字段宽度修改完毕");
        System.out.println("输入QUIT退出 或 继续输入表单UUID");
        uuid = scanner.nextLine();
      }
    } finally {
      if (scanner != null) {
        scanner.close();
      }
    }
    System.out.println("谢谢使用!");
  }

  private static Form buildForm(String uuid) {
    ExpressMatchFieldWidthFormat format = new ExpressMatchFieldWidthFormat();
    FormReader reader = new AWSFormReader();

    Form form = reader.read(uuid);
    format.format(form);
    return form;
  }

  public static void modifyWidth(Form form) {
    AWSDao dao = new AWSDao();
    Connection conn = null;

    try {
      conn = DBUtil.open();
      conn.setAutoCommit(false);

      dao.updateFields(conn, form.getMainTable().getFieldList());

      List<Table> tables = form.getTables();
      for (Table table : tables) {
        dao.updateFields(conn, table.getFieldList());
      }

      conn.commit();
    } catch (SQLException e) {
      DBUtil.rollback(conn);
      throw new RuntimeException(e);
    } finally {
      DBUtil.close(conn);
    }
  }

}
