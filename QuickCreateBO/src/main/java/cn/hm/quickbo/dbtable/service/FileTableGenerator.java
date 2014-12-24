package cn.hm.quickbo.dbtable.service;

/**
 * 通过文件创建Table.
 * @author huangming
 *
 */
public interface FileTableGenerator extends TableGenerator {

  /**
   * 开始创建文件.
   * @param filepath
   */
  public void startCreate(String filepath);
}
