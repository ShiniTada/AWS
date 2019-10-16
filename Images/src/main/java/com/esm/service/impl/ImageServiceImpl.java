package com.epam.esm.service.impl;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.epam.esm.controller.exception.AmazonAccessIsDeniedException;
import com.epam.esm.model.entity.ImageMetadata;
import com.epam.esm.model.repository.impl.ImageMetadataRepository;
import com.epam.esm.service.ImageService;
import com.epam.esm.service.exception.NotExistException;
import com.epam.esm.service.exception.UnsupportedExtensionException;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Random;

@Service
public class ImageServiceImpl implements ImageService {

  private static final Logger LOGGER = LogManager.getLogger(ImageServiceImpl.class);
  @Autowired ImageMetadataRepository repository;
  private AmazonS3 s3client;

    @Value("${amazonProperties.apiUrl}")
    private String apiUrl;

  @Value("${amazonProperties.bucketName}")
  private String bucketName;

  @Value("${amazonProperties.accessKey}")
  private String accessKey;

  @Value("${amazonProperties.secretKey}")
  private String secretKey;

  private final static String JPG = ".jpg";
  private final static String JPEG = ".jpeg";
  private final static String PNG = ".png";


  @PostConstruct // after constructor
  private void initializeAmazon() {
    AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
    this.s3client = new AmazonS3Client(credentials);
  }

  @Transactional
  public String uploadImage(MultipartFile multipartFile, String uri) {
    String fileUrl = "";
    Optional<File> optionalFile = convertMultiPartToFile(multipartFile);
    if (optionalFile.isPresent()) {
      File file = optionalFile.get();
      checkFileExtension(file.getName());
        fileUrl = apiUrl + "/images/ " + file.getName();
      repository.save(new ImageMetadata(file.getName(), fileUrl, multipartFile.getSize()));
      uploadFileTos3bucket(file.getName(), file);
    }
    return fileUrl;
  }

  private void checkFileExtension(String fileName) {
    if (!(fileName.contains(PNG) || fileName.contains(JPEG) || fileName.contains(JPG))) {
      throw new UnsupportedExtensionException(
          "Unsupported extension. Supported only: " + PNG + " " + JPG + " " + JPEG);
    }
  }

  private String setFileExtension(String fileName, String uri) {
    if (uri.contains(PNG)) {
      return fileName + PNG;
    } else if (uri.contains(JPEG)) {
      return fileName + JPEG;
    } else if (uri.contains(JPG)) {
      return fileName + JPG;
    } else {
      throw new UnsupportedExtensionException(
          "Unsupported extension. Supported only: " + PNG + " " + JPG + " " + JPEG);
    }
  }

  @Override
  public byte[] getImageBytesByName(String fileName, String uri) throws IOException {
    fileName = setFileExtension(fileName, uri);
    S3Object imageBytes;
    try {
      imageBytes = s3client.getObject(new GetObjectRequest(bucketName, fileName));
      return IOUtils.toByteArray(imageBytes.getObjectContent());
    } catch (SdkClientException e) {
      throw new NotExistException("Image " + fileName + " does not exist.");
    }
  }

  @Override
  public byte[] getRandomImageBytes() throws IOException {
    try {
      String imageName = getImageNameByNumber(new Random().nextInt(getImagesCount()));
      S3Object imageBytes = s3client.getObject(new GetObjectRequest(bucketName, imageName));
      return IOUtils.toByteArray(imageBytes.getObjectContent());
    } catch (IllegalArgumentException e) {
      throw new NotExistException("There are no any images in the bucket");
    }
  }

  private Optional<File> convertMultiPartToFile(MultipartFile file) {
    try {
      File convertedFile = new File(file.getOriginalFilename());
      FileOutputStream fos = new FileOutputStream(convertedFile);
      fos.write(file.getBytes());
      fos.close();
      return Optional.of(convertedFile);
    } catch (IOException e) {
      LOGGER.warn("Can't load file " + file.getOriginalFilename());
      return Optional.empty();
    }
  }

  private void uploadFileTos3bucket(String fileName, File file) {
    try {
      s3client.putObject(
          new PutObjectRequest(bucketName, fileName, file)
              .withCannedAcl(
                  CannedAccessControlList
                      .PublicReadWrite)); // anyone who have the file apiUrl can access this file
    } catch (AmazonS3Exception e) {
      LOGGER.error("Amazon access is denied");
      throw new AmazonAccessIsDeniedException("Amazon access is denied");
    }
  }

  private int getImagesCount() {
    ObjectListing listObjects = s3client.listObjects(bucketName);
    return listObjects.getObjectSummaries().size();
  }

  private String getImageNameByNumber(int number) {
    ObjectListing listObjects = s3client.listObjects(bucketName);
    return listObjects.getObjectSummaries().get(number).getKey();
  }
}
