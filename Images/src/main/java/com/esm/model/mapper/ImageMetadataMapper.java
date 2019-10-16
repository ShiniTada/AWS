package com.epam.esm.model.mapper;

import com.epam.esm.model.entity.ImageMetadata;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ImageMetadataMapper implements RowMapper<ImageMetadata> {

  @Override
  public ImageMetadata mapRow(ResultSet resultSet, int i) throws SQLException {
    int columnNumber = 1;
    String name = resultSet.getString(columnNumber++);
    String path = resultSet.getString(columnNumber++);
    long size = resultSet.getLong(columnNumber);
    return new ImageMetadata(name, path, size);
  }
}
