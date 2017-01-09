package se.cambio.loganalyzer.dao.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import se.cambio.loganalyzer.common.DataManagement;
import se.cambio.loganalyzer.dao.config.DatabaseConnector;
import se.cambio.loganalyzer.dao.entities.Entity;
import se.cambio.loganalyzer.dao.entities.LogHistory;
import se.cambio.loganalyzer.entities.ErrorLog;
import se.cambio.loganalyzer.file.FileContentReader;

public class FindingLogHistory extends FindingService
{

  public List<ErrorLog> logs;
  public FindingLogHistory(Entity serviceInstanceType)
  {
    super(serviceInstanceType);

    logs = new ArrayList<ErrorLog>();
  }

  public List<LogHistory> findAll(String table1, String table2, String table3)
  {
    //    return findAll(table1 + " NATURAL JOIN " + table2);
    String query = "SELECT * FROM " + table1 + " as H ," + table2 + " as M , " + table3
        + " as I where H.log_id = M.id AND H.status = '0' AND I.id = H.issue_id";

    try
    {
      Statement stmnt = DatabaseConnector.getInstance().getConnection().createStatement();
      ResultSet rs = stmnt.executeQuery(query);
      while (rs.next())
      {
          ErrorLog errorLog = new ErrorLog();
          
          String error_lines = rs.getString("error_msg");
          List<String> particularErrorLinse = getErrorLineList(error_lines);///have to do
          errorLog.errorDescriptions = particularErrorLinse;
          errorLog.errorName = rs.getString("type");
          errorLog.history.sent_time = rs.getDate("sent_time");
          errorLog.errorID = Integer.toString(rs.getInt("id"));//errorLog.errorName + LogLine.getErrorDate(file_contents.indexOf(content), file_contents);
          errorLog.error_count = rs.getInt("times_occured");
          logs.add(errorLog);
          DataManagement.appendToMapUnSynchronized(errorLog, rs.getString("location").trim() +rs.getString("file_name_prefix").trim() +"." + FileContentReader.FILE_EXTENSION );
          errorLog = new ErrorLog();
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return null;

  }
  
  private List<String> getErrorLineList(String error_lines)
  {
    List<String> lines = new ArrayList<String>();
    
    String errors[] = error_lines.split("\n");
    for(String error:errors)
    {
      lines.add(error);
    }
    return lines;
  }
}
