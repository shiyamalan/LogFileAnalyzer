package se.cambio.loganalyzer.dao.service;

import java.util.List;

import se.cambio.loganalyzer.dao.entities.Entity;

public interface Selectable
{
  List<Entity> findAll(String tableName);
  
  List<Entity> findById(String id,String tableName);
  
  List<Entity> findAllForIntegervalue(String tableName,String fieldName,String value);
  
  //int getIDFor(List<Entity> valueList);
}
