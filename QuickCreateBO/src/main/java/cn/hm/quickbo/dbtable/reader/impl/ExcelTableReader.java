package cn.hm.quickbo.dbtable.reader.impl;

import static cn.hm.quickbo.util.ExcelUtil.getCellContent;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.hm.quickbo.dbtable.domain.Table;
import cn.hm.quickbo.dbtable.domain.TableField;
import cn.hm.quickbo.dbtable.reader.TableReader;
import cn.hm.quickbo.util.StringUtil;
import cn.hm.quickbo.util.ValidateUtil;

/**
 * Excel读取Table.
 * 
 * @author huangming
 *
 */
public class ExcelTableReader implements TableReader {

  /**
   * 表别名所在的行.
   */
  private static final int TABLE_TITLE_ROW = 2;
  /**
   * 表格字段数据起始行.
   */
  private static final int TABLE_START_ROW = 5;
  /**
   * 字段名.
   */
  private static final int FIELD_NAME_COL = 0;
  /**
   * 字段显示名.
   */
  private static final int FIELD_TITLE_COL = 1;
  /**
   * 字段类型. TableDict中有相关获取的方法.
   */
  private static final int FIELD_TYPE_COL = 2;
  /**
   * 字段长度.
   */
  private static final int FIELD_LENGTH_COL = 3;
  /**
   * 字段默认值.
   */
  private static final int FIELD_DEFAULT_COL = 4;

  private String filename;

  public ExcelTableReader(String filename) {
    super();
    this.filename = filename;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  /**
   * 读取表格.
   */
  @Override
  public List<Table> readTables() {
    List<Table> tableList = new LinkedList<Table>();
    XSSFWorkbook wb;

    // 读取Excel文件
    try {
      wb = new XSSFWorkbook(OPCPackage.open(filename));
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("读取失败!");
    } catch (InvalidFormatException e) {
      e.printStackTrace();
      throw new RuntimeException("读取失败!");
    }

    int sheetSize = wb.getNumberOfSheets();

    // 遍历所有Sheet
    for (int sheetIndex = 0; sheetIndex < sheetSize; sheetIndex++) {
      XSSFSheet sheet = wb.getSheetAt(sheetIndex);

      int curRow = 0;
      int lastRow = sheet.getLastRowNum();

      Table table = null;
      List<TableField> fieldList = null;
      TableField tableField = null;

      while (curRow <= lastRow) {
        if (sheet.getRow(curRow) != null) {
          Table tempTable = getTableHead(sheet, curRow, 0);
          if (tempTable != null) {
            if (table != null) {
              tableList.add(table);
            }

            fieldList = new LinkedList<TableField>();
            tempTable.setFieldList(fieldList);

            table = tempTable;
            curRow += TABLE_START_ROW - TABLE_TITLE_ROW;
            continue;
          }
          if (table != null) {
            tableField = getTableField(sheet, curRow);
            if (validateTableField(tableField)) {
              fieldList.add(tableField);
            }
          }
        }
        curRow++;
      }
      if (table != null) {
        tableList.add(table);
      }
    }
    return tableList;
  }

  /**
   * 获取表格头.
   * 
   * @param sheet
   * @param row
   * @param col
   * @return
   */
  public Table getTableHead(Sheet sheet, int row, int col) {
    Table table = null;
    String splitStr = "--";
    String cellContent = getCellContent(sheet, row, col);
    int splitIndex = cellContent.indexOf(splitStr);
    if (splitIndex == -1) {
      return null;
    } else {
      String title = cellContent.substring(0, splitIndex);
      String name = cellContent.substring(splitIndex + splitStr.length());
      String groupName = sheet.getSheetName();
      table = new Table();
      if (ValidateUtil.validateNullOrEmtpy(name) || ValidateUtil.validateNullOrEmtpy(title)) {
        throw new RuntimeException((row + 1) + "行附近，无法获取表名和表标题名，请检查!");
      }

      // 清理格式
      name = StringUtil.replaceOtherText(name);
      title = StringUtil.replaceOtherText(title);
      groupName = StringUtil.replaceOtherText(groupName);

      if (name.startsWith("BO_")) {
        name = name.substring("BO_".length());
      }

      table.setGroupName(groupName);
      table.setTableName(name);
      table.setTableTitle(title);
    }
    return table;
  }

  /**
   * 获取表格字段.
   * 
   * @param sheet
   * @param row
   * @return
   */
  public TableField getTableField(Sheet sheet, int row) {
    TableField tableField = new TableField();
    tableField.setFieldName(StringUtil.replaceAllSpace(getCellContent(sheet, row, FIELD_NAME_COL)));
    tableField.setFieldTitle(StringUtil.replaceAllSpace(getCellContent(sheet, row, FIELD_TITLE_COL)));
    tableField.setFieldType(StringUtil.replaceAllSpace(getCellContent(sheet, row, FIELD_TYPE_COL)));
    tableField.setFieldLenght(StringUtil.replaceAllSpace(getCellContent(sheet, row, FIELD_LENGTH_COL)));
    tableField.setFieldDefault(getCellContent(sheet, row, FIELD_DEFAULT_COL));
    return tableField;
  }

  /**
   * 校验表格字段.
   * 
   * @param tableField
   * @return
   */
  public boolean validateTableField(TableField tableField) {
    if (tableField == null)
      return false;
    if (ValidateUtil.validateNullOrEmtpy(tableField.getFieldName()) || ValidateUtil.validateNullOrEmtpy(tableField.getFieldTitle())
            || ValidateUtil.validateNullOrEmtpy(tableField.getFieldType())) {
      return false;
    }
    if (ValidateUtil.validateNullOrEmtpy(tableField.getFieldLenght())) {
      tableField.setFieldLenght("0");
    }
    return true;
  }
}
