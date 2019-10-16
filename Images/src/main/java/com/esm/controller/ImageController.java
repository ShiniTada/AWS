package com.epam.esm.controller;

import com.epam.esm.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("images")
public class ImageController {

  @Autowired private ImageService amazonClientService;

  @PostMapping( produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public void uploadFile(
      @RequestPart(value = "file") MultipartFile file, HttpServletRequest request,  HttpServletResponse response) {
    String imageUrl =  amazonClientService.uploadImage(file, request.getRequestURI());
    response.addHeader("Location", imageUrl);
  }

  @GetMapping(
      value = "/{name}",
      produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
  @ResponseStatus(HttpStatus.OK)
  public byte[] getFile(@PathVariable("name") String name, HttpServletRequest request)
      throws IOException {
    return amazonClientService.getImageBytesByName(name, request.getRequestURI());
  }

  @GetMapping(
      value = "/random",
      produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
  @ResponseStatus(HttpStatus.OK)
  public byte[] getFile() throws IOException {
    return amazonClientService.getRandomImageBytes();
  }
}
