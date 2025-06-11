package com.subro.blog.controllers;


import com.subro.blog.payloads.ApiResponse;
import com.subro.blog.payloads.CategoryDto;
import com.subro.blog.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @PostMapping("/")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody @Valid CategoryDto categoryDto){
        CategoryDto createCategoryDto = this.categoryService.createCategory(categoryDto);
        return new ResponseEntity<CategoryDto>(createCategoryDto, HttpStatus.CREATED);
    }

   @PutMapping("/{categoryId}")
   public ResponseEntity<CategoryDto> updateCategory(@RequestBody @Valid CategoryDto categoryDto, @PathVariable Integer categoryId){
       CategoryDto updateCategoryDto = this.categoryService.updateCategory(categoryDto, categoryId);
       return new ResponseEntity<>(updateCategoryDto, HttpStatus.OK);
   }

   @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer categoryId){
        this.categoryService.deleteCategory(categoryId);
        return new ResponseEntity<ApiResponse>(new ApiResponse("category is deleted successfully!!", true ), HttpStatus.OK);
   }

   @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Integer categoryId){
       CategoryDto categoryDtoById = this.categoryService.getCategoryById(categoryId);
       return new ResponseEntity<>(categoryDtoById, HttpStatus.OK);
   }

   @GetMapping("/")
    public ResponseEntity<List<CategoryDto>> getAllCategories(){
       List<CategoryDto> allCategoriesDto = this.categoryService.getAllCategories();
       return new ResponseEntity<>(allCategoriesDto, HttpStatus.OK);
   }
}
