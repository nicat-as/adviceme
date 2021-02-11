package com.uniso.equso.service;

import com.uniso.equso.dao.entities.CategoryEntity;
import com.uniso.equso.model.categories.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getCategories();
}
