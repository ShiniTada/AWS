package com.epam.esm.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

  /**
   * Load file in the S3 and return access url
   *
   * @param multipartFile - file to load
   * @return url of loaded file
   */
  String uploadImage(MultipartFile multipartFile, String uri);

  /**
   * Get file bytes by file name
   *
   * @param name - file name
   * @return file bytes
   */
  byte[] getImageBytesByName(String name, String uri) throws IOException;

  byte[] getRandomImageBytes() throws IOException;
}
