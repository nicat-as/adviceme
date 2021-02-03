package com.uniso.equso.mapper;

import com.uniso.equso.dao.entities.CategoryEntity;
import com.uniso.equso.model.categories.CategoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class CategoryMapper {

    public static final CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    public abstract CategoryDto entityToCategoryDto(CategoryEntity entity);
}
