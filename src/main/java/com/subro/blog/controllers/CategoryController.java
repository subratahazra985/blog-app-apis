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
    /**
     * Handles the HTTP POST request for creating a new category.
     *
     * @param categoryDto The DTO containing the details of the category to be created.
     * @return A ResponseEntity containing the created CategoryDto and HTTP status CREATED.
     */
    @PostMapping("/")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody @Valid CategoryDto categoryDto){
        CategoryDto createCategoryDto = this.categoryService.createCategory(categoryDto);
        return new ResponseEntity<CategoryDto>(createCategoryDto, HttpStatus.CREATED);
    }

   /**
     * Handles the HTTP PUT request for updating a category.
     *
     * @param categoryDto The DTO containing the details of the category to be updated.
     * @param categoryId   The id of the category to be updated.
     * @return A ResponseEntity containing the updated CategoryDto and HTTP status OK.
     */
   @PutMapping("/{categoryId}")
   public ResponseEntity<CategoryDto> updateCategory(@RequestBody @Valid CategoryDto categoryDto, @PathVariable Integer categoryId){
       CategoryDto updateCategoryDto = this.categoryService.updateCategory(categoryDto, categoryId);
       return new ResponseEntity<>(updateCategoryDto, HttpStatus.OK);
   }

   /**
     * Handles the HTTP DELETE request for deleting a category.
     *
     * @param categoryId   The id of the category to be deleted.
     * @return A ResponseEntity containing the ApiResponse with success message and HTTP status OK.
     */
   @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer categoryId){
        this.categoryService.deleteCategory(categoryId);
        return new ResponseEntity<ApiResponse>(new ApiResponse("category is deleted successfully!!", true ), HttpStatus.OK);
   }

   /**
     * Handles the HTTP GET request for retrieving a category by id.
     *
     * @param categoryId The id of the category to be retrieved.
     * @return A ResponseEntity containing the CategoryDto of the retrieved category and HTTP status OK.
     */
   @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Integer categoryId){
       CategoryDto categoryDtoById = this.categoryService.getCategoryById(categoryId);
       return new ResponseEntity<>(categoryDtoById, HttpStatus.OK);
   }

   /**
     * Handles the HTTP GET request for retrieving all categories.
     *
     * @return A ResponseEntity containing the list of CategoryDto of all categories and HTTP status OK.
     */
   @GetMapping("/")
    public ResponseEntity<List<CategoryDto>> getAllCategories(){
       List<CategoryDto> allCategoriesDto = this.categoryService.getAllCategories();
       return new ResponseEntity<>(allCategoriesDto, HttpStatus.OK);
   }
}
