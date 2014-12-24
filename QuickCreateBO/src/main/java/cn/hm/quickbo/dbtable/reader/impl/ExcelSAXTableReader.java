package cn.hm.quickbo.dbtable.reader.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheets;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.WorkbookDocument;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import cn.hm.quickbo.dbtable.domain.Table;
import cn.hm.quickbo.dbtable.domain.TableField;
import cn.hm.quickbo.dbtable.reader.TableReader;
import cn.hm.quickbo.util.StringUtil;
import cn.hm.quickbo.util.ValidateUtil;

public class ExcelSAXTableReader implements TableReader {

  /**
   * 表格标题列.
   */
  private static final int FIELD_TABLE_TITLE = 1;
  /**
   * 字段名.
   */
  private static final int FIELD_NAME_COL = 1;
  /**
   * 字段显示名.
   */
  private static final int FIELD_TITLE_COL = 2;
  /**
   * 字段类型. TableDict中有相关获取的方法.
   */
  private static final int FIELD_TYPE_COL = 3;
  /**
   * 字段长度.
   */
  private static final int FIELD_LENGTH_COL = 4;
  /**
   * 字段默认值.
   */
  private static final int FIELD_DEFAULT_COL = 5;

  private String filename;

  public ExcelSAXTableReader() {
    super();
  }

  public ExcelSAXTableReader(String filename) {
    super();
    this.filename = filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  @Override
  public List<Table> readTables() {
    OPCPackage pkg = null;
    List<Table> list = new LinkedList<Table>();
    List<String> sheetNameList = new ArrayList<String>(20);

    try {
      pkg = OPCPackage.open(filename);
      XSSFReader r = new XSSFReader(pkg);
      SharedStringsTable sst = r.getSharedStringsTable();

      WorkbookDocument doc = WorkbookDocument.Factory.parse(r.getWorkbookData());
      CTSheets sheets = doc.getWorkbook().getSheets();
      List<CTSheet> sheetList = sheets.getSheetList();
      for (CTSheet ctSheet : sheetList) {
        sheetNameList.add(ctSheet.getName());
      }

      int curSheetIndex = 0;
      Iterator<InputStream> sheetsData = r.getSheetsData();
      while (sheetsData.hasNext()) {
        XMLReader parser = fetchSheetParser(sst, sheetNameList.get(curSheetIndex), list);
        InputStream sheet = sheetsData.next();
        InputSource sheetSource = new InputSource(sheet);
        parser.parse(sheetSource);
        sheet.close();
        curSheetIndex++;
      }
      return list;
    } catch (IOException | OpenXML4JException | SAXException | XmlException e) {
      e.printStackTrace();
      throw new RuntimeException("读取失败!");
    } finally {
      try {
        if (pkg != null) {
          pkg.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * XML处理.
   * 
   * @param sst
   * @param sheetName
   * @param list
   * @return
   * @throws SAXException
   */
  public XMLReader fetchSheetParser(SharedStringsTable sst, String sheetName, List<Table> list) throws SAXException {
    XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
    ContentHandler handler = new SheetHandler(sst, sheetName, list);
    parser.setContentHandler(handler);
    return parser;
  }

  /**
   * XML处理类.
   * 
   * @author huangming
   *
   */
  public static class SheetHandler extends DefaultHandler {

    private List<Table> list;
    private Table currentTable;
    private List<TableField> tableFieldList;
    private TableField currentTableField;

    // 行游标
    private int curRow;
    // 列游标
    private int curCell;

    private SharedStringsTable sst;
    private StringBuilder lastContents;

    private boolean nextIsRead;
    private boolean nextIsString;
    private boolean nextIsNumber;

    private String sheetName;

    public SheetHandler(SharedStringsTable sst, String sheetName, List<Table> list) {
      this.sst = sst;
      curRow = 0;
      curCell = 0;
      this.sheetName = sheetName;
      this.list = list;

      // init value
      currentTableField = new TableField();
      lastContents = new StringBuilder(10);
    }

    public List<Table> getList() {
      return list;
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
      switch (name) {
      // c => cell
      case "c":
        parseCell(attributes);
        break;
      // row=> row
      case "row":
        paserRow();
        break;
      default:
      }
      // Clear contents cache
      lastContents.delete(0, lastContents.length());
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
      // Process the last contents as required.
      // Do now, as characters() may be called more than once
      if ((nextIsString || nextIsNumber) && nextIsRead) {

        String content = null;

        if (nextIsString) {
          int idx = Integer.parseInt(lastContents.toString());
          content = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
          nextIsString = false;
        }

        if (nextIsNumber) {
          content = lastContents.toString();
          nextIsNumber = false;
        }

        // v => contents of a cell
        // Output after we've seen the string contents
        if (name.equals("v")) {
          // 此行为字段
          switch (curCell) {
          case FIELD_NAME_COL:
            currentTableField.setFieldName(content);
            break;
          case FIELD_TITLE_COL:
            currentTableField.setFieldTitle(content);
            break;
          case FIELD_TYPE_COL:
            currentTableField.setFieldType(content);
            break;
          case FIELD_LENGTH_COL:
            currentTableField.setFieldLenght(content);
            break;
          case FIELD_DEFAULT_COL:
            currentTableField.setFieldDefault(content);
            break;
          }
        }

        if (curCell == FIELD_TABLE_TITLE) {
          Table newTable = getTable(content);
          if (newTable != null) {
            // 此行为表头
            addTable(newTable);
          }
        }
      }
    }

    /**
     * 读取文本内容.
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
      if ((nextIsString || nextIsNumber) && nextIsRead) {
        lastContents.append(ch, start, length);
      }
    }

    /**
     * 处理列.
     * 
     * @param attributes
     */
    public void parseCell(Attributes attributes) {
      String cellType = attributes.getValue("t");
      if (cellType != null && cellType.equals("s")) {
        nextIsString = true;
      }
      if (cellType == null) {
        nextIsNumber = true;
      }
      curCell++;

    }

    /**
     * 处理行.
     */
    public void paserRow() {
      if (validateTableField(currentTableField)) {
        if (tableFieldList != null) {
          tableFieldList.add(currentTableField);
        }
        currentTableField = new TableField();
      } else {
        // 清空对象， 复用此对象，减少对象创建开销.
        clearTableField(currentTableField);
      }
      curCell = 0;
      curRow++;

      nextIsRead = true;
    }

    /**
     * 增加Table.
     * 
     * @return
     */
    private void addTable(Table table) {
      // 新建立Table对象.
      currentTable = table;
      tableFieldList = new LinkedList<TableField>();
      // 将遍历好的字段存入Table对象.
      currentTable.setFieldList(tableFieldList);
      list.add(currentTable);
      // 清空当前字段
      clearTableField(currentTableField);

      nextIsRead = false;
    }

    /**
     * 获取表格头.
     * 
     * @param sheet
     * @param row
     * @param col
     * @return
     */
    public Table getTable(String cellContent) {
      Table table = null;
      String splitStr = "--";
      int splitIndex = cellContent.indexOf(splitStr);
      if (splitIndex == -1) {
        return null;
      } else {
        String title = cellContent.substring(0, splitIndex);
        String name = cellContent.substring(splitIndex + splitStr.length());
        String groupName = sheetName;
        table = new Table();
        if (ValidateUtil.validateNullOrEmtpy(name) || ValidateUtil.validateNullOrEmtpy(title)) {
          throw new RuntimeException((curRow + 1) + "行附近，无法获取表名和表标题名，请检查!");
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

    public void clearTableField(TableField tableField) {
      tableField.setFieldDefault(null);
      tableField.setFieldTitle(null);
      tableField.setFieldName(null);
      tableField.setFieldType(null);
      tableField.setFieldLenght(null);
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

}
