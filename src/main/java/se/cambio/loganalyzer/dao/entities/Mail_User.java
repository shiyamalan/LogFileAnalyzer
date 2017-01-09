package se.cambio.loganalyzer.dao.entities;

public class Mail_User extends Entity
{
  public String mail_id;
  
  public String user_name;
  
  
  @Override
  public void assignValues()
  {
    String pairs[] = this.attributes_value_pair.split(",");
    this.id = Integer.parseInt(pairs[0]);
    this.mail_id = pairs[1];
    this.user_name = pairs[2];
    this.status = Integer.parseInt(pairs[3]);
  }
}
