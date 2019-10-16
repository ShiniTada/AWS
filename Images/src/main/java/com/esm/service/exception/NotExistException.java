package com.epam.esm.service.exception;

public class NotExistException extends ServiceException {

  public NotExistException() {}

  public NotExistException(String message) {
    super(message);
  }

  public NotExistException(String message, Throwable cause) {
    super(message, cause);
  }

  public NotExistException(Throwable cause) {
    super(cause);
  }
}
