package cn.hm.quickbo.dbtable.reader;



public abstract class FileTableReader implements TableReader{

  protected String filepath;
  
  public String getFilepath() {
    return filepath;
  }

  public void setFilepath(String filepath) {
    this.filepath = filepath;
  }
  
}
