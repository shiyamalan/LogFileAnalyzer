package se.cambio.loganalyzer.dao.entities;

import java.util.List;

public class Issue extends Entity
{
  public String type;
  
  public Issue(int id,String type,int status)
  {
    super(id,status);
    this.type = type;
  }
  
  public Issue()
  {
  }

  @Override
  public void assignValues()
  {
    String pairs[] = this.attributes_value_pair.split(",");
    this.id = Integer.parseInt(pairs[0]);
    this.type = pairs[1];
    this.status = Integer.parseInt(pairs[2]);
    
  }
  
  public int getID(List<Issue> issues,String type)
  {
    int id = -1;
    for(Issue issue:issues)
    {
      if(issue.type.equals(type))
        return issue.id;
    }
    
    return id;
  }
}
