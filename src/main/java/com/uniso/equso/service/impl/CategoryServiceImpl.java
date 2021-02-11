package com.uniso.equso.service.impl;

import com.uniso.equso.dao.entities.CategoryEntity;
import com.uniso.equso.dao.repository.CategoryEntityRepository;
import com.uniso.equso.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryEntityRepository categoryEntityRepository;

    @Override
    public List<CategoryEntity> getCategories() {
        return categoryEntityRepository.findAll();
    }
}
