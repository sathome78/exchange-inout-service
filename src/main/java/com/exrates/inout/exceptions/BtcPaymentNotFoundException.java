package com.exrates.inout.exceptions;

public class BtcPaymentNotFoundException extends RuntimeException {
  
  public BtcPaymentNotFoundException() {
  }
  
  public BtcPaymentNotFoundException(String message) {
    super(message);
  }
  
  public BtcPaymentNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public BtcPaymentNotFoundException(Throwable cause) {
    super(cause);
  }
}
