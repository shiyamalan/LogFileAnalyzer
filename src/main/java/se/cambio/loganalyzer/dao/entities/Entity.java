package se.cambio.loganalyzer.dao.entities;

public abstract class Entity implements Cloneable
{
  public int id;

  public int status;
  
  public String attributes_value_pair = "ID:2,status:1";
  
  public Entity(int id,int status)
  {
    super();
    this.status = status;
    this.id = id;
  }
  
  public Entity()
  {
  }
  
  public abstract void assignValues();
  
//  public int getIDFor(String attributeName,Object value)
//  {
//    int id = 0;
//    
//    return id;
//  }
  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }
}
