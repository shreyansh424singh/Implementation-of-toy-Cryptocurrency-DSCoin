package DSCoinPackage;

import HelperClasses.CRF;

public class BlockChain_Malicious {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock[] lastBlocksList;

  public static boolean checkTransactionBlock (TransactionBlock tB) {
    CRF obj = new CRF(64);
    String ld; int i;
    if(tB.previous == null) ld = start_string;
    else ld = tB.previous.dgst;

    if(!tB.dgst.substring(0, 4).equals("0000")) return false;
    if(!tB.dgst.equals(obj.Fn(ld + "#" + tB.trsummary + "#" + tB.nonce))) return false;
    
    if(!tB.trsummary.equals(tB.Tree.Build(tB.trarray))) return false;

    for(i=0; i<tB.trarray.length; i++)
      if(!tB.checkTransaction(tB.trarray[i])) return false;

    return true;
  }

  public TransactionBlock FindLongestValidChain () {
    int fl = 0, l, i, j;
    TransactionBlock a, b=null, c=null;
    for(i=0; i<lastBlocksList.length; i++){                    
      l=0;
      for(a = lastBlocksList[i]; a!=null; a=a.previous){
        if(checkTransactionBlock(a)){
          l++;
          if(l==1)  b = a;
        }
        else{
          l=0; b = null;
        }
      }
      if(l>fl){
        fl = l; c = b;
      }
    }          
    return c;
  }

  public void InsertBlock_Malicious (TransactionBlock newBlock) {
    TransactionBlock lb = this.FindLongestValidChain();
    CRF obj = new CRF(64);
    String ld;
    if(lb != null) {
      ld = lb.dgst;
      newBlock.previous = lb;
    }
    else {
      ld = start_string;
      newBlock.previous = null;
    }
    for(int i=1000000001; i>0 ; i++){
      String s = obj.Fn(ld + "#" + newBlock.trsummary + "#" + i);
      if(s.substring(0, 4).equals("0000")){
        newBlock.nonce = "" + i;
        newBlock.dgst = s;
        break;
      }
    }
    int i, f=0;
    for(i=0; i<lastBlocksList.length; i++){                    
      if(lastBlocksList[i] == null) {f=1; break;}
      if(lastBlocksList[i].nonce.equals(lb.nonce)) break;
    }
    if(f == 1) lastBlocksList[i] = newBlock;
    else if(i != lastBlocksList.length) lastBlocksList[i] = newBlock;
    else lastBlocksList[i-1] = newBlock;
  }
}
