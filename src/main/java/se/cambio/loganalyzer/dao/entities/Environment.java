package se.cambio.loganalyzer.dao.entities;

import java.util.List;

public class Environment extends Entity
{
  public String name;
  
  public Environment(int id,String name,int status)
  {
    super(id,status);
    this.name = name;
  }
  public Environment()
  {
  }
  @Override
  public void assignValues()
  {
    String pairs[] = this.attributes_value_pair.split(",");
    this.id = Integer.parseInt(pairs[0]);
    this.name = pairs[1];
    this.status = Integer.parseInt(pairs[2]);
    
  }
  
  public Environment getEnvironment(int id,List<Environment> environments)
  {
    Environment env = null;
    
    for(Environment e:environments)
    {
      if(id == e.id)
      {
        return e;
      }
    }
    return env;
  }
 
}
