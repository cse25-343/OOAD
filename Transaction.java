import java.time.LocalDate;

public class Transaction{
   private String transactional;
   private LocalDate date;
   private double amount;
   
  public Transaction(String transational, LocalDate Date, double amount){
   this.transactional =transactional;
   this.date = date;
   this.amount = amount;
   }
   
   public void execute() {
  System.out.println("Transaction executed.");
  }
}