package com.epam.esm.model.repository;

public interface ImageRepository<T> {

  T save(T t);
}
