package org.stellar.sdk;

import org.apache.commons.codec.DecoderException;
import org.stellar.sdk.xdr.Memo;
import org.stellar.sdk.xdr.MemoType;

/**
 * Represents MEMO_RETURN.
 */
public class MemoReturnHash extends MemoHashAbstract {
  public MemoReturnHash(byte[] bytes) {
    super(bytes);
  }

  public MemoReturnHash(String hexString) throws DecoderException {
    super(hexString);
  }

  @Override
  Memo toXdr() {
    Memo memo = new Memo();
    memo.setDiscriminant(MemoType.MEMO_RETURN);

    org.stellar.sdk.xdr.Hash hash = new org.stellar.sdk.xdr.Hash();
    hash.setHash(bytes);

    memo.setHash(hash);
    return memo;
  }
}
