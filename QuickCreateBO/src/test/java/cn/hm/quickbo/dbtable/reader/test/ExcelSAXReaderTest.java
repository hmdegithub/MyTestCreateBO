package cn.hm.quickbo.dbtable.reader.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheets;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.WorkbookDocument;

import cn.hm.quickbo.dbtable.domain.Table;
import cn.hm.quickbo.dbtable.reader.impl.ExcelSAXTableReader;
import cn.hm.quickbo.dbtable.reader.impl.ExcelTableReader;

public class ExcelSAXReaderTest {

  private String filename = "C:/Users/huangming/Desktop/文档/shouhoubo.xlsx";

  public long startTime;

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
  public void readWorkbookSheet() throws Exception {
    List<String> sheetsList = new ArrayList<String>();

    OPCPackage pkg = null;
    pkg = OPCPackage.open(filename);
    XSSFReader r = new XSSFReader(pkg);

    WorkbookDocument doc = WorkbookDocument.Factory.parse(r.getWorkbookData());
    CTSheets sheets = doc.getWorkbook().getSheets();
    List<CTSheet> sheetList = sheets.getSheetList();
    for (CTSheet ctSheet : sheetList) {
      sheetsList.add(ctSheet.getName());
    }
  }

  @Test
  public void readSheetContent() {
    ExcelSAXTableReader reader = new ExcelSAXTableReader(filename);
    List<Table> tables = reader.readTables();

    int totalField = 0;
     System.out.println("表格长度:" + tables.size());
    for (Table table : tables) {
       System.out.println(table);
       System.out.println(table.getTableName() + "\t字段长度:" +
       table.getFieldList().size());
       totalField+= table.getFieldList().size();
    }

     System.out.println("表格总字段长度:" + totalField);
  }

  @Test
  public void readSheetContentForDom() {
    ExcelTableReader reader = new ExcelTableReader(filename);
    List<Table> tables = reader.readTables();
    int totalField = 0;
    System.out.println("表格长度:" + tables.size());
    for (Table table : tables) {
      System.out.println(table.getTableName() + "\t字段长度:" + table.getFieldList().size());
      totalField += table.getFieldList().size();
    }
    System.out.println("表格总字段长度:" + totalField);
  }

}
