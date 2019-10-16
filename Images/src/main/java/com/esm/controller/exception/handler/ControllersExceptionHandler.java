package com.epam.esm.controller.exception.handler;

import com.epam.esm.controller.exception.AmazonAccessIsDeniedException;
import com.epam.esm.model.repository.exception.AlreadyExistsException;
import com.epam.esm.model.repository.exception.RepositoryException;
import com.epam.esm.service.dto.ExceptionDto;
import com.epam.esm.service.exception.NotExistException;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.exception.UnsupportedExtensionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllersExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger LOGGER_HANDLER =
      LogManager.getLogger(ControllersExceptionHandler.class);

  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(
      NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    LOGGER_HANDLER.warn("No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL());
    ExceptionDto exceptionDto = new ExceptionDto();
    exceptionDto.addMessage(
        "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL());
    return new ResponseEntity<>(exceptionDto, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(AmazonAccessIsDeniedException.class)
  public ResponseEntity<Object> amazonAccessIsDeniedException(AmazonAccessIsDeniedException ex) {
    LOGGER_HANDLER.warn(ex.getMessage());
    ExceptionDto exceptionDto = new ExceptionDto();
    exceptionDto.addMessage(ex.getMessage());
    return new ResponseEntity<>(exceptionDto, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NotExistException.class)
  public ResponseEntity<Object> notExistException(NotExistException ex) {
    LOGGER_HANDLER.warn(ex.getMessage());
    ExceptionDto exceptionDto = new ExceptionDto();
    exceptionDto.addMessage(ex.getMessage());
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return new ResponseEntity<>(exceptionDto, headers, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UnsupportedExtensionException.class)
  public ResponseEntity<Object> unsupportedExtensionException(UnsupportedExtensionException ex) {
    LOGGER_HANDLER.warn(ex.getMessage());
    ExceptionDto exceptionDto = new ExceptionDto();
    exceptionDto.addMessage(ex.getMessage());
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return new ResponseEntity<>(exceptionDto, headers, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
  }

  @ExceptionHandler(ServiceException.class)
  public ResponseEntity<Object> serviceException(ServiceException ex) {
    LOGGER_HANDLER.warn(ex.getMessage());
    ExceptionDto exceptionDto = new ExceptionDto();
    exceptionDto.addMessage(ex.getMessage());
    return new ResponseEntity<>(exceptionDto, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(AlreadyExistsException.class)
  public ResponseEntity<Object> alreadyExistsException(AlreadyExistsException ex) {
    LOGGER_HANDLER.warn(ex.getMessage());
    ExceptionDto exceptionDto = new ExceptionDto();
    exceptionDto.addMessage(ex.getMessage());
    return new ResponseEntity<>(exceptionDto, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(RepositoryException.class)
  public ResponseEntity<Object> repositoryException(RepositoryException ex) {
    LOGGER_HANDLER.warn(ex.getMessage());
    ExceptionDto exceptionDto = new ExceptionDto();
    exceptionDto.addMessage(ex.getMessage());
    return new ResponseEntity<>(exceptionDto, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> globalExceptionHandler(Exception ex) {
    LOGGER_HANDLER.warn(ex.getMessage());
    ExceptionDto exceptionDto = new ExceptionDto();
    exceptionDto.addMessage(ex.getMessage());
    return new ResponseEntity<>(exceptionDto, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
