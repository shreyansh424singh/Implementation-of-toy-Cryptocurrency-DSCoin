package DSCoinPackage;

import java.util.*;
import HelperClasses.Pair;

public class Members
 {

  public String UID;
  public List<Pair<String, TransactionBlock>> mycoins;
  public Transaction[] in_process_trans;

  public void initiateCoinsend(String destUID, DSCoin_Honest DSobj) throws MissingTransactionException{
    Pair<String, TransactionBlock> p = mycoins.get(0);
    mycoins.remove(0);
    Transaction tobj = new Transaction();
    Members mem = new Members();                                     
    for(int i=0; i<DSobj.memberlist.length; i++){
      if(destUID.equals(DSobj.memberlist[i].UID)) tobj.Destination = DSobj.memberlist[i];
    }
    tobj.coinID = p.first;
    tobj.Source = this;
    for(int i=0; i<100; i++)                                       
      if(in_process_trans[i]==null){
        in_process_trans[i] = tobj; break;
      }
    DSobj.pendingTransactions.AddTransactions(tobj);

    //for coinsrc block
    TransactionBlock tB = DSobj.bChain.lastBlock;
    int fl=0;
    for(; tB!=null ; tB = tB.previous){
      for(int i=0; i<tB.trarray.length ; i++){
        if(tobj.coinID.equals(tB.trarray[i].coinID)){
          fl=1; break;
        }
      }
      if(fl == 1) break;
    }
    if(tB == null) throw new MissingTransactionException();
    tobj.coinsrc_block = tB;
  }


  public Pair<List<Pair<String, String>>, List<Pair<String, String>>> finalizeCoinsend (Transaction tobj, DSCoin_Honest DSObj) throws MissingTransactionException {
    TransactionBlock tB = DSObj.bChain.lastBlock;
    int fl=0;
    for(; tB!=null ; tB = tB.previous){
      for(int i=0; i<tB.trarray.length ; i++){
        if(tobj.coinID.equals(tB.trarray[i].coinID)){
          fl=1; break;
        }
      }
      if(fl == 1) break;
    }
    if(tB == null) throw new MissingTransactionException();


    int cID = Integer.parseInt(tobj.coinID);
    List<Pair<String, String>> l1 = tB.Tree.QueryDocument(cID, tB.trarray);

    //list 2 of pair
    List<Pair<String, String>> l2 = new ArrayList<Pair<String, String>>();
    TransactionBlock tb1 = DSObj.bChain.lastBlock;
    for(; !tb1.trsummary.equals(tB.trsummary); tb1 = tb1.previous){
      Pair<String, String> p2 = new Pair<String, String>(tb1.dgst, tb1.previous.dgst + "#" + tb1.trsummary + "#" + tb1.nonce);
      l2.add(p2);  
    }
    Pair<String, String> p2 = new Pair<String, String>(tb1.dgst, tb1.previous.dgst + "#" + tb1.trsummary + "#" + tb1.nonce);
    l2.add(p2);
    Collections.reverse(l2);
    String s;
    if(tB.previous != null) s = tB.previous.dgst;
    else s = "DSCoin";
    Pair<String, String> p1 = new Pair<String, String>(s,  null);
    l2.add(0, p1);

    Pair<List<Pair<String, String>>, List<Pair<String, String>>> p11 = new Pair<List<Pair<String, String>>, List<Pair<String, String>>>(l1, l2);

    //remove from in_process_trans
    for(int i=0; i<100; i++){
      if(in_process_trans[i] == null) break;
      if(tobj.coinID.equals(in_process_trans[i].coinID)){
        while(in_process_trans[i+1]!=null){
          in_process_trans[i] = in_process_trans[i+1];
          i++;
        }
        in_process_trans[i]=null;
        break;
      }
    }

    //check fake coins
    for(int i=0; i<DSObj.memberlist.length; i++){
      for(int j=0; j<DSObj.memberlist[i].mycoins.size(); j++){
        if(tobj.coinID.equals(DSObj.memberlist[i].mycoins.get(j).first)) return p11;
      }
    }

    Pair<String, TransactionBlock> p = new Pair<String, TransactionBlock>(tobj.coinID, tB);
    addcoin(tobj.Destination, p);
    
    return p11;
  }

  public void MineCoin(DSCoin_Honest DSObj) throws EmptyQueueException{
    int i, j; 
    Transaction[] t = new Transaction[DSObj.bChain.tr_count];
    for(i=0; i < DSObj.bChain.tr_count-1; ){
      for(j=0; j<i; j++){
        if(DSObj.pendingTransactions.firstTransaction.coinID.equals(t[j].coinID)) break;
      }
      if(j==i){
        t[i] = DSObj.pendingTransactions.firstTransaction; i++;
      }
      DSObj.pendingTransactions.RemoveTransaction();
    }
    Transaction a = new Transaction();
    int las = Integer.parseInt(DSObj.latestCoinID) + 1;
    String temp = ""+las;
    DSObj.latestCoinID = temp;
    a.coinID = DSObj.latestCoinID;
    a.Source = null;
    a.coinsrc_block = null;
    a.Destination = this;                 

    t[i] = a;
    TransactionBlock tB = new TransactionBlock(t);
    DSObj.bChain.InsertBlock_Honest(tB);

    Pair<String, TransactionBlock> p = new Pair<String, TransactionBlock>(a.coinID, tB);
    addcoin(this, p);
  }  

  public void MineCoin(DSCoin_Malicious DSObj) throws EmptyQueueException{
    int i, j; 
    Transaction[] t = new Transaction[DSObj.bChain.tr_count];
    for(i=0; i < DSObj.bChain.tr_count-1; ){
      for(j=0; j<i; j++){
        if(DSObj.pendingTransactions.firstTransaction.coinID.equals(t[j].coinID)) break;
      }
      if(j==i){
        t[i] = DSObj.pendingTransactions.firstTransaction; i++;
      }
      DSObj.pendingTransactions.RemoveTransaction();
    }
    Transaction a = new Transaction();
    int las = Integer.parseInt(DSObj.latestCoinID) + 1;
    String temp = ""+las;
    DSObj.latestCoinID = temp;
    a.coinID = DSObj.latestCoinID;
    a.Source = null;
    a.coinsrc_block = null;
    a.Destination = this;                     

    t[i] = a;
    TransactionBlock tB = new TransactionBlock(t);
    DSObj.bChain.InsertBlock_Malicious(tB);

    Pair<String, TransactionBlock> p = new Pair<String, TransactionBlock>(a.coinID, tB);
    addcoin(this, p);
  }  

  public void addcoin(Members m, Pair<String, TransactionBlock> p){
    int n = Integer.parseInt(p.first);
    for(int i=0; i<m.mycoins.size(); i++){
      int t = Integer.parseInt(m.mycoins.get(i).first);
      if(t>n){
        m.mycoins.add(i, p);
        return;
      }
    }
    m.mycoins.add(p);
    return;
  }
}
