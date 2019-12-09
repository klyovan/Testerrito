package com.netcracker.testerritto.exceptions;

public class CategoryServiceException extends Exception {
  public final static byte _FAIL_TO_GET = 1;
  public final static byte _FAIL_TO_CREATE = 2;
  public final static byte _FAIL_TO_UPDATE = 3;
  public final static byte _FAIL_TO_DELETE = 4;
  private byte errorCode;

  public CategoryServiceException(byte errorCode) {
    this.errorCode = errorCode;
  }

  public byte getErrorCode() {
    return errorCode;
  }


}
