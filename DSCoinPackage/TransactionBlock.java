package DSCoinPackage;

import HelperClasses.MerkleTree;
import HelperClasses.CRF;
import HelperClasses.Pair;

public class TransactionBlock {

  public Transaction[] trarray;
  public TransactionBlock previous;
  public MerkleTree Tree;
  public String trsummary;
  public String nonce;
  public String dgst;

  TransactionBlock(){}

  TransactionBlock(Transaction[] t) {
    int tr = t.length;
    trarray = new Transaction[tr];
    for(int i=0; i<tr; i++){
      Transaction a = t[i];
      trarray[i] = a;
    }
    MerkleTree m = new MerkleTree();
    String s = m.Build(trarray);
    Tree = m; 
    trsummary = s;
  }

  public boolean checkTransaction (Transaction t) {
    int tr = this.trarray.length;
    if(t.coinsrc_block == null) return true;
    TransactionBlock a = t.coinsrc_block;
    int i=0, j=0;
    for(i=0; i<tr; i++){
      if(a.trarray[i].coinID.equals(t.coinID)){ 
        if(!a.trarray[i].Destination.UID.equals(t.Source.UID)) return false;
        break;
      }
    }
    if(i==tr) return false;

    // extra
    for(i=0; i<t.Source.mycoins.size(); i++){
      if(t.coinID.equals(t.Source.mycoins.get(i).first)) break;
    }
    if(i == t.Source.mycoins.size()) return false;
    //

    for(i=0; i<tr; i++)
      if(this.trarray[i].coinID.equals(t.coinID)) j++;
    if(j!=1) return false;
    
    for(a = this.previous; !a.dgst.equals(t.coinsrc_block.dgst); a = a.previous){
      for(i=0; i<tr; i++)
        if(a.trarray[i].coinID.equals(t.coinID)) return false;
    }
    return true;
  }
}
