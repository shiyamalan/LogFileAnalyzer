package se.cambio.loganalyzer.dao.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import se.cambio.loganalyzer.dao.config.DatabaseConnector;

public class InsertingService
{

  private static final String INSERT_QUERY_ORG = "INSERT INTO $TABLE_NAME ($COLUMNS) VALUES ($ATT_VALUES)";

  private static String INSERT_QUERY = INSERT_QUERY_ORG;

  private static String SELECT_MAX = "SELECT MAX(id) as id FROM $TABLE_NAME";

  public int insertTo(String tableName, Map<String, Object> attribute_value_pair)
  {
    INSERT_QUERY = INSERT_QUERY_ORG;
    int id = -1;
    try
    {
      INSERT_QUERY = getInsertQuery(tableName, attribute_value_pair);
      Statement stmnt = DatabaseConnector.getInstance().getConnection().createStatement();
      stmnt.executeUpdate(INSERT_QUERY);

      SELECT_MAX = SELECT_MAX.replace("$TABLE_NAME", tableName);
      ResultSet rs = stmnt.executeQuery(SELECT_MAX);

      while (rs.next())
      {
        id = rs.getInt("id");

      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return id;
    }
    return id;
  }

  private String getInsertQuery(String tableName, Map<String, Object> attribute_value_pair)
  {
    INSERT_QUERY = INSERT_QUERY.replace("$TABLE_NAME", tableName);

    for (Map.Entry<String, Object> entrySet : attribute_value_pair.entrySet())
    {
      String atribute = entrySet.getKey();
      Object valueObj = entrySet.getValue();
      if (valueObj instanceof String)
        INSERT_QUERY = INSERT_QUERY.replace("$ATT_VALUES", "'" + valueObj.toString() + "'" + ", $ATT_VALUES");
      else if (valueObj instanceof Integer)
        INSERT_QUERY = INSERT_QUERY.replace("$ATT_VALUES",
            "" + Integer.parseInt(valueObj.toString()) + "" + ", $ATT_VALUES");
      INSERT_QUERY = INSERT_QUERY.replace("$COLUMNS", atribute + ", $COLUMNS");

    }
    INSERT_QUERY = INSERT_QUERY.replace(", $COLUMNS", "");
    INSERT_QUERY = INSERT_QUERY.replace(", $ATT_VALUES", "");
    return INSERT_QUERY;
  }
}
