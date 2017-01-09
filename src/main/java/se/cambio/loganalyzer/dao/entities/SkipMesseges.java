package se.cambio.loganalyzer.dao.entities;

public class SkipMesseges extends Entity
{
    public String message;
    
    public SkipMesseges(int id,String message,int status)
    {
      super(id,status);
      this.message = message;
    }
    
    public SkipMesseges()
    {
    }

    @Override
    public void assignValues()
    {
      String pairs[] = this.attributes_value_pair.split(",");
      this.id = Integer.parseInt(pairs[0]);
      this.message = pairs[2];
      this.status = Integer.parseInt(pairs[3]);
      
    }
   
}
