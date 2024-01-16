package com.example.library.service;

import com.example.library.model.Image;

import java.util.List;

public interface ImageService {
    List<Image> findProductImages(long id);

    List<Image> findAll();
}
