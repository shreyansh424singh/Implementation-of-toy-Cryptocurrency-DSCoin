package DSCoinPackage;

import HelperClasses.CRF;

public class BlockChain_Honest {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock lastBlock;

  public void InsertBlock_Honest(TransactionBlock newBlock) {
    CRF obj = new CRF(64);
    String ld;
    if(lastBlock != null) {
      ld = lastBlock.dgst;
      newBlock.previous = lastBlock;
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
    lastBlock = newBlock;
  }
}
