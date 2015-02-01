package cn.hm.psapp.qgform.paser.html;


public class TableCell {
  
  private int colspan;
  private int rowspan;
  private String htmlClass;
  private String htmlStyle;
  private String htmlAlign;
  private String nameHtmlclass;
  private String nameStyle;
  private String name;
  private String nameAlign;
  private String text;
  
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
  
  public String getHtmlClass() {
    return htmlClass;
  }
  
  public void setHtmlClass(String htmlClass) {
    this.htmlClass = htmlClass;
  }
  
  public String getHtmlStyle() {
    return htmlStyle;
  }
  
  public void setHtmlStyle(String htmlStyle) {
    this.htmlStyle = htmlStyle;
  }
  
  public String getHtmlAlign() {
    return htmlAlign;
  }
  
  public void setHtmlAlign(String htmlAlign) {
    this.htmlAlign = htmlAlign;
  }
  
  public String getNameHtmlclass() {
    return nameHtmlclass;
  }
  
  public void setNameHtmlclass(String nameHtmlclass) {
    this.nameHtmlclass = nameHtmlclass;
  }
  
  public String getNameStyle() {
    return nameStyle;
  }
  
  public void setNameStyle(String nameStyle) {
    this.nameStyle = nameStyle;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getNameAlign() {
    return nameAlign;
  }
  
  public void setNameAlign(String nameAlign) {
    this.nameAlign = nameAlign;
  }
  
  public String getText() {
    return text;
  }
  
  public void setText(String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return "TableCell [colspan=" + colspan + ", rowspan=" + rowspan + ", htmlClass=" + htmlClass + ", htmlStyle=" + htmlStyle + ", htmlAlign=" + htmlAlign
            + ", nameHtmlclass=" + nameHtmlclass + ", nameStyle=" + nameStyle + ", name=" + name + ", nameAlign=" + nameAlign + ", text=" + text + "]";
  }
  
  
}
