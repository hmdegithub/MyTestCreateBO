package cn.hm.psapp.qgform.paser.html.format;

import java.util.ArrayList;
import java.util.List;

import cn.hm.psapp.qgform.Field;
import cn.hm.psapp.qgform.Form;
import cn.hm.psapp.qgform.Table;
import cn.hm.psapp.qgform.config.ConfigConstant;
import cn.hm.psapp.qgform.paser.html.FieldHtmlFormatter;
import cn.hm.psapp.qgform.paser.html.TableCell;

public class StandardHtmlFormatter implements FieldHtmlFormatter {

  @Override
  public String build(Form form) {

    Table mTable = form.getMainTable();
    List<Field> fieldList = mTable.getFieldList();
    List<TableCell> cellList = new ArrayList<TableCell>(ConfigConstant.CACHE_TABLE_FIELD_SIZE);
    for (Field field : fieldList) {
      if (isShow(field)) {
        cellList.add(matchTableCell(field));
      }
    }
    return builderHtml(cellList);
  }

  /**
   * 过滤字段.
   * 
   * @param field
   * @return
   */
  public boolean isShow(Field field) {
    if (field.getIsDisplay() != Field.YES) {
      return false;
    }

    return !(field.getFieldName().equals("制单人") 
            || field.getFieldName().equals("制单日期"));
  }

  /**
   * 生成HTML.
   * 
   * @param cellList
   * @return
   */
  private String builderHtml(List<TableCell> cellList) {
    int curCol = 0;

    StringBuilder sb = new StringBuilder();

    appendTrS(sb);
    for (int i = 0; cellList.size() > 0; i++) {
      TableCell tableCell = cellList.get(i % cellList.size());

      if (curCol + tableCell.getColspan() + 1 > ConfigConstant.INIT_COL_NUM) {
        for (int j = curCol; j < ConfigConstant.INIT_COL_NUM; j++) {
          appendTdNbsp(sb);
        }
        appendTrE(sb);
        appendTrS(sb);
        curCol = 0;
      }

      // TD的NAME
      appendTdO(sb);
      sb.append(" nowarp=nowarp ");
      if (tableCell.getNameHtmlclass() != null) {
        sb.append(" class=\"").append(tableCell.getNameHtmlclass()).append("\"");
      }
      if (tableCell.getNameStyle() != null) {
        sb.append(" style=\"").append(tableCell.getNameStyle()).append("\"");
      }
      if (tableCell.getNameAlign() != null) {
        sb.append(" align=\"").append(tableCell.getNameAlign()).append("\"");
      }
      sb.append(">");
      sb.append(tableCell.getName());
      appendTdE(sb);

      // TD的内容.
      appendTdO(sb);
      if (tableCell.getHtmlStyle() != null) {
        sb.append(" style=\"").append(tableCell.getHtmlStyle()).append("\"");
      }
      if (tableCell.getHtmlClass() != null) {
        sb.append(" class=\"").append(tableCell.getHtmlClass()).append("\"");
      }
      if (tableCell.getHtmlAlign() != null) {
        sb.append(" align=\"").append(tableCell.getHtmlAlign()).append("\"");
      }
      sb.append(" colspan=\"").append(tableCell.getColspan()).append("\"");
      sb.append(">");
      sb.append(tableCell.getText());
      appendTdE(sb);

      curCol = curCol + tableCell.getColspan() + 1;
      cellList.remove(i % cellList.size());
    }

    for (int j = curCol; j < ConfigConstant.INIT_COL_NUM; j++) {
      appendTdNbsp(sb);
    }
    appendTrE(sb);

    return sb.toString();
  }

  public void appendTdO(StringBuilder sb) {
    sb.append("\t\t<td");
  }
  public void appendTdS(StringBuilder sb) {
    sb.append("\t\t<td>");
  }
  public void appendTdE(StringBuilder sb) {
    sb.append("</td>\n");
  }
  public void appendTrS(StringBuilder sb) {
    sb.append("\t<tr>\n");
  }
  public void appendTrE(StringBuilder sb) {
    sb.append("\t</tr>\n");
  }
  public void appendTdNbsp(StringBuilder sb) {
    sb.append("\t\t<td>&nbsp;</td>\n");
  }

  @Override
  public TableCell matchTableCell(Field field) {
    TableCell cell = new TableCell();
    cell.setName(field.getFieldTitle());
    cell.setText(new StringBuilder().append("[@").append(field.getFieldName()).append("]").toString());
    String fieldLength = field.getFieldLength();
    int indexOf = fieldLength.indexOf(',');
    if (indexOf == -1) {
      int fieldSize = Integer.parseInt(fieldLength);
      if (fieldSize > 100) {
        cell.setColspan(5);
        cell.setHtmlClass("td_data3");
      } else if (fieldSize > 60) {
        cell.setColspan(3);
        cell.setHtmlClass("td_data2");
      } else if (fieldSize > -1) {
        cell.setColspan(1);
        cell.setHtmlClass("td_data");
      }
    } else {
      cell.setColspan(1);
    }

    cell.setNameHtmlclass("td_title");
    cell.setNameAlign("right");
    cell.setHtmlAlign("left");
    return cell;
  }
}
