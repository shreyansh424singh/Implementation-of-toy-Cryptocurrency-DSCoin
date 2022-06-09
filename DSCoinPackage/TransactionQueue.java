package DSCoinPackage;
import java.util.*;

public class TransactionQueue {

  public Transaction firstTransaction;
  public Transaction lastTransaction;
  public int numTransactions;
  public List<Transaction> q = new ArrayList<Transaction>();

  public void AddTransactions (Transaction transaction) {
    numTransactions++;
    Transaction a = transaction;  
    q.add(a);
    lastTransaction = a;
    if(numTransactions == 1)  firstTransaction = a;
  }
  
  public Transaction RemoveTransaction () throws EmptyQueueException {
    Transaction a = new Transaction();
    if(firstTransaction == null && lastTransaction == null) throw new EmptyQueueException();
    else{
      numTransactions--;
      a = firstTransaction;
      if(q.size()==1) 
        firstTransaction = null;
      else  
        firstTransaction = q.get(1);
      q.remove(0);
    }
    return a;
  }

  public int size() {
    return q.size();
  }
}
