package cn.hm.quickbo.dbtable.service;

import java.util.List;

import cn.hm.quickbo.dbtable.domain.Table;


public interface TableGenerator {

  /**
   * 保存表.
   * @param list
   */
  public void saveTables(List<Table> list);
  /**
   * 保存表.
   * @param table
   */
  public void saveTable(Table table);
}
