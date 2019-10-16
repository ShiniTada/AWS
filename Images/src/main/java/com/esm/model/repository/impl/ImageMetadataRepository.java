package com.epam.esm.model.repository.impl;

import com.epam.esm.model.entity.ImageMetadata;
import com.epam.esm.model.mapper.ImageMetadataMapper;
import com.epam.esm.model.repository.ImageRepository;
import com.epam.esm.model.repository.exception.AlreadyExistsException;
import com.epam.esm.model.repository.exception.RepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ImageMetadataRepository implements ImageRepository<ImageMetadata> {

  private static final String RETURNING = "RETURNING name, path, size, creation_date;";

  private static final String INSERT_CLOUD_IMAGE =
      "INSERT INTO image_metadata (name, path, size, creation_date) VALUES (?, ?, ?, now()) "
          + RETURNING;

  @Autowired private JdbcTemplate jdbcTemplate;

  @Autowired private ImageMetadataMapper imageMapper;

  @Override
  public ImageMetadata save(ImageMetadata imageMetadata) {
    try {
      return jdbcTemplate.queryForObject(
          INSERT_CLOUD_IMAGE,
          new Object[] {imageMetadata.getName(), imageMetadata.getPath(), imageMetadata.getSize()},
          imageMapper);
    } catch (EmptyResultDataAccessException ex) {
      throw new RepositoryException("Repository exception. Image do not added.");
    } catch (DuplicateKeyException ex) {
      throw new AlreadyExistsException("Image " + imageMetadata.getName() + " has already been added.");
    }
  }
}
