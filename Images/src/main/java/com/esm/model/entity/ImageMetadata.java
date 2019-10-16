package com.epam.esm.model.entity;

import java.time.LocalDate;
import java.util.Objects;

public class ImageMetadata {

  private String name;

  private String path;

  private long size;

  private LocalDate creationDate;

  public ImageMetadata() {}

  public ImageMetadata(String name, String path, long size, LocalDate creationDate) {
    this.name = name;
    this.path = path;
    this.size = size;
    this.creationDate = creationDate;
  }

  public ImageMetadata(String name, String path, long size) {
    this(name, path, size, null);
  }

  public String getName() {
    return name;
  }

  public String getPath() {
    return path;
  }

  public long getSize() {
    return size;
  }

  public void setCreationDate(LocalDate creationDate) {
    this.creationDate = creationDate;
  }

  public LocalDate getCreationDate() {
    return creationDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImageMetadata that = (ImageMetadata) o;
    return size == that.size
        && Objects.equals(name, that.name)
        && Objects.equals(path, that.path)
        && Objects.equals(creationDate, that.creationDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, path, size, creationDate);
  }

  @Override
  public String toString() {
    return "ImageMetadata{"
        + "name='"
        + name
        + '\''
        + ", path='"
        + path
        + '\''
        + ", size="
        + size
        + ", creationDate="
        + creationDate
        + '}';
  }
}
