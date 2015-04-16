package cn.hm.psapp.qgform.paser.html;

import cn.hm.psapp.qgform.Field;

public class TableCell {

  private int colspan;
  private int rowspan;
  private String name;
  private String text;
  private Field field;

  public Field getField() {
    return field;
  }

  public void setField(Field field) {
    this.field = field;
  }

  public int getColspan() {
    return colspan;
  }

  public void setColspan(int colspan) {
    this.colspan = colspan;
  }

  public int getRowspan() {
    return rowspan;
  }

  public void setRowspan(int rowspan) {
    this.rowspan = rowspan;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return "TableCell [colspan=" + colspan + ", rowspan=" + rowspan + ", name=" + name + ", text=" + text + "]";
  }

}
