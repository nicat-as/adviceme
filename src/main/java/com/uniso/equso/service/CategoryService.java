package com.uniso.equso.service;

import com.uniso.equso.dao.entities.CategoryEntity;

import java.util.List;

public interface CategoryService {
    List<CategoryEntity> getCategories();
}
