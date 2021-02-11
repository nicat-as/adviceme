package com.uniso.equso.service.impl;

import com.uniso.equso.dao.repository.CategoryEntityRepository;
import com.uniso.equso.mapper.CategoryMapper;
import com.uniso.equso.model.categories.CategoryDto;
import com.uniso.equso.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryEntityRepository categoryEntityRepository;

    @Override
    public List<CategoryDto> getCategories() {
        var categoryEntity = categoryEntityRepository.findAll();
        return categoryEntity.stream()
                .map(CategoryMapper.INSTANCE::entityToCategoryDto)
                .collect(Collectors.toList());
    }
}
