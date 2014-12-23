package cn.hm.quickbo.dbtable.reader;

import java.util.List;

import cn.hm.quickbo.dbtable.domain.Table;

/**
 * 表格读取接口.
 * 
 * @author huangming
 *
 */
public interface TableReader {

  /**
   * 读取多个表格.
   * 
   * @return
   */
  public List<Table> readTables();

}
