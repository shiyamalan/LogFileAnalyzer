package se.cambio.loganalyzer.dao.entities;

import java.util.Date;

import se.cambio.loganalyzer.time.TimeProperty;

public class LogHistory extends Entity
{

  public int issue_id;
  public String error_msg;
  public int  time_occured;
  public Date sent_time;
  public LogMonitor logs;
  
  public LogHistory()
  {
    logs = new LogMonitor();
  }
  @Override
  public void assignValues()
  {
    String pairs[] = this.attributes_value_pair.split(",");
    this.id = Integer.parseInt(pairs[0]);
    this.issue_id = Integer.parseInt(pairs[1]);
    this.error_msg = pairs[2];
    this.time_occured = Integer.parseInt(pairs[3]);
    this.sent_time = TimeProperty.validateDateFormat(pairs[4],TimeProperty.TIME_STAMP_FORMAT);
    this.status = Integer.parseInt(pairs[5]);

  }
  
  

}
