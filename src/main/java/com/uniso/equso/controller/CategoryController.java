package com.uniso.equso.controller;

import com.uniso.equso.model.Wrapper;
import com.uniso.equso.model.categories.CategoryDto;
import com.uniso.equso.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Wrapper<List<CategoryDto>>> getCategories() {
        Wrapper<List<CategoryDto>> result = new Wrapper<>();
        result.setData(categoryService.getCategories());
        return ResponseEntity.ok(result);
    }
}
