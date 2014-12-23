package cn.hm.quickbo.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;


public class ExcelUtil {
  
  private ExcelUtil(){}
  
  /**
   * 获取单元格值.
   * 
   * @param sheet
   * @param row
   * @param col
   * @return
   */
  public static String getCellContent(Sheet sheet, int row, int col) {
    Cell cell = sheet.getRow(row).getCell(col);
    if (cell == null)
      return "";

    int cellType = cell.getCellType();
    switch (cellType) {
    case Cell.CELL_TYPE_BLANK:
      return "";
    case Cell.CELL_TYPE_BOOLEAN:
      return String.valueOf(cell.getBooleanCellValue()).trim();
    case Cell.CELL_TYPE_ERROR:
    case Cell.CELL_TYPE_FORMULA:
    case Cell.CELL_TYPE_NUMERIC:
      return String.valueOf(cell.getNumericCellValue()).replaceAll("\\.0", "").trim();
    case Cell.CELL_TYPE_STRING:
      return cell.getStringCellValue().trim();
    default:
      return "";
    }
  }

}
