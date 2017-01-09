package se.cambio.loganalyzer.dao.service;

import java.util.List;

public interface Updateable
{
  void update(String tableName,String filedName);
  
  void updateAll(String tableName,List<String> filedList);
  
  void updatePKData(String tableName,String pid,String fieldName,String value);
  
  void updatePKData(String tableName,String pid,String fieldName,int value);
}
