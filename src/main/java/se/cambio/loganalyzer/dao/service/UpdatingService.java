package se.cambio.loganalyzer.dao.service;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import se.cambio.loganalyzer.dao.config.DatabaseConnector;

public class UpdatingService implements Updateable
{
  @SuppressWarnings("unused")
  private static String UPDATE_ALL_QUERY = "UPDATE ";

  private static String UPDATE_QUERY = "UPDATE $TABLE_NAME SET $COLUMN = '$VALUE' WHERE $WHCOLUMN='$WHVALUE'";

  @Override
  public void update(String tableName, String filedName)
  {

  }

  @Override
  public void updateAll(String tableName, List<String> filedList)
  {
  }

  @Override
  public void updatePKData(String tableName, String pid, String fieldName, String value)
  {
    String tmpQry = UPDATE_QUERY;
    tmpQry = tmpQry.replace("$TABLE_NAME", tableName);

    String column_value_pair = fieldName + "=" + "'" + value.trim() + "'";
    tmpQry = tmpQry.replace("$COLUMN = '$VALUE'", column_value_pair);

    tmpQry = tmpQry.replace("$WHCOLUMN", "id");

    tmpQry = tmpQry.replace("$WHVALUE", pid);

    try
    {
      Statement stmnt = DatabaseConnector.getInstance().getConnection().createStatement();
      stmnt.executeUpdate(tmpQry);
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }

  @Override
  public void updatePKData(String tableName, String pid, String fieldName, int value)
  {
    updatePKData(tableName, pid, fieldName, Integer.toString(value));

  }

}
