package com.epam.esm.controller.exception;

public class AmazonAccessIsDeniedException extends RuntimeException {

  public AmazonAccessIsDeniedException() {}

  public AmazonAccessIsDeniedException(String message) {
    super(message);
  }

  public AmazonAccessIsDeniedException(String message, Throwable cause) {
    super(message, cause);
  }

  public AmazonAccessIsDeniedException(Throwable cause) {
    super(cause);
  }
}
