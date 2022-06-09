package DSCoinPackage;
import java.util.*;
import HelperClasses.Pair;

public class Moderator
 {

  public void initializeDSCoin(DSCoin_Honest DSObj, int coinCount) {
    int cc = 0, l = DSObj.memberlist.length; 
    Transaction [] trarr = new Transaction[DSObj.bChain.tr_count];
    TransactionBlock t1 = new TransactionBlock();
    Members Moderator = new Members();
    Moderator.UID = "Moderator";
    for(int i=0; i<(coinCount/DSObj.bChain.tr_count); i++){
      for(int j=0; j<DSObj.bChain.tr_count; j++){
        Transaction t = new Transaction();
        t.Destination = DSObj.memberlist[cc%l];
        int temp = 100000 + cc; cc++;
        t.coinID = "" + temp; 
        t.Source = Moderator;
        trarr[j] = t;
      }
      TransactionBlock t2 = new TransactionBlock(trarr);
      if(i==0) t2.previous = null;
      else t2.previous = t1;
      t1 = t2;
      DSObj.bChain.InsertBlock_Honest(t2);

      cc = cc-DSObj.bChain.tr_count;
      for(int j=0; j<DSObj.bChain.tr_count; j++){
        int temp = 100000 + cc;
        String s = "" + temp;
        Pair<String, TransactionBlock> p = new Pair<String, TransactionBlock>(s, t2);
        DSObj.memberlist[cc%l].mycoins.add(p);
        cc++;
      }
    }
    --cc;
    int op = 100000 + cc;
    DSObj.latestCoinID = ""+op;

  }
    
  public void initializeDSCoin(DSCoin_Malicious DSObj, int coinCount) {
    int cc = 0, l = DSObj.memberlist.length; 
    Transaction [] trarr = new Transaction[DSObj.bChain.tr_count];
    TransactionBlock t1 = new TransactionBlock();
    Members Moderator = new Members();
    Moderator.UID = "Moderator";
    for(int i=0; i<(coinCount/DSObj.bChain.tr_count); i++){
      for(int j=0; j<DSObj.bChain.tr_count; j++){
        Transaction t = new Transaction();
        t.Destination = DSObj.memberlist[cc%l];
        int temp = 100000 + cc; cc++;
        t.coinID = "" + temp; 
        t.Source = Moderator;
        trarr[j] = t;
      }
      TransactionBlock t2 = new TransactionBlock(trarr);
      if(i==0) t2.previous = null;
      else t2.previous = t1;
      t1 = t2;
      DSObj.bChain.InsertBlock_Malicious(t2);
      cc = cc-DSObj.bChain.tr_count;
      for(int j=0; j<DSObj.bChain.tr_count; j++){
        int temp = 100000 + cc;
        String s = "" + temp;
        Pair<String, TransactionBlock> p = new Pair<String, TransactionBlock>(s, t2);
        DSObj.memberlist[cc%l].mycoins.add(p);
        cc++;
      }
    }
    --cc;
    int op = 100000 + cc;
    DSObj.latestCoinID = ""+op;

  }
}
