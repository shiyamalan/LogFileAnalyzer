package se.cambio.loganalyzer.mail;

public class MailScheduler
{
  Runnable myThreadObj;

  public MailScheduler(Runnable t)
  {
    this.myThreadObj = t;
    new Thread(myThreadObj).start();
  }

  public MailScheduler()
  {
  }


  public void schedule()
  {
    new MailScheduler(new HistoricalMailSender());
  }

}
