package se.cambio.loganalyzer.dao.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import se.cambio.loganalyzer.dao.config.DatabaseConnector;
import se.cambio.loganalyzer.dao.config.Table;
import se.cambio.loganalyzer.dao.config.TableInitialization;
import se.cambio.loganalyzer.dao.entities.Entity;
import se.cambio.loganalyzer.dao.entities.LogMonitor;

public class FindingService implements Selectable
{

  public String SELECT_QUERY = "SELECT * FROM $TABLENAME ";

  //public String SELECT_BY_ID_QUERY = SELECT_QUERY + " WHERE id = '$ID'";

  public String SELECT_GENERIC_QUERY = SELECT_QUERY + " WHERE $FIELD = '$VALUE'";
  
  private Entity serviceInstanceType = null;

  public FindingService(Entity serviceInstanceType)
  {
    this.serviceInstanceType  = serviceInstanceType;
  }
  
  
  @Override
  public  List<Entity> findAll(String tableName)
  {
    String tmpQry = SELECT_QUERY;
    tmpQry = tmpQry.replace("$TABLENAME", tableName);
    List<Entity> entities = new ArrayList<Entity>();
    ResultSet rs;
    try
    {
      Statement statement = DatabaseConnector.getInstance().getConnection().createStatement();
      rs = statement.executeQuery(tmpQry);
    
      while (rs.next())
      {
        Entity aEntity = getEntity(rs, tableName);
        entities.add(aEntity);
      }

      rs.close();
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    catch (CloneNotSupportedException e)
    {
      e.printStackTrace();
    }
    return entities;
  }

  private Entity getEntity(ResultSet rs, String entityName) throws SQLException, CloneNotSupportedException
  {
    Entity entity = (Entity)serviceInstanceType.clone();
    entity.attributes_value_pair  = "";
    for (String attribute : getTable(entityName).attributes)
    {
        String tmpValue = "";
        tmpValue = rs.getString(attribute);
        
        if(tmpValue == null)
          tmpValue = Integer.toString(rs.getInt(attribute));
        entity.attributes_value_pair +=  tmpValue.trim() + ",";
    }
    return getCorrectEntity(entity.attributes_value_pair,entity);
  }

  private Entity getCorrectEntity(String attributes_value_pair,Entity entity)
  {
      entity.assignValues();
      return entity;
  }

  private Table getTable(String tableName)
  {
     TableInitialization.init();
     return TableInitialization.getTables().get(tableName);
  }

  @Override
  public List<Entity> findById(String id, String tableName)
  {
   
    return null;
  }
  
  public static void main(String args[])
  {
    FindingService service = new FindingService(new LogMonitor());
    service.findAll("log_monitor");
  }


  @Override
  public List<Entity> findAllForIntegervalue(String tableName, String fieldName, String value)
  {
    SELECT_QUERY  = SELECT_GENERIC_QUERY;
    SELECT_QUERY = SELECT_QUERY.replace("$FIELD", fieldName);
    SELECT_QUERY = SELECT_QUERY.replace("$VALUE", value);
    return findAll(tableName);
  }






}
