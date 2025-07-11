package com.subro.blog.services.impl;

import com.subro.blog.entities.Category;
import com.subro.blog.exceptions.ResourceNotFoundException;
import com.subro.blog.payloads.CategoryDto;
import com.subro.blog.repositories.CategoryRepository;
import com.subro.blog.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    /**
     * Creates a new category.
     *
     * @param categoryDto The DTO containing the details of the category to be created.
     * @return A ResponseEntity containing the created CategoryDto and HTTP status CREATED.
     */
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = this.modelMapper.map(categoryDto, Category.class);
        Category savedCategory = this.categoryRepository.save(category);
        return this.modelMapper.map(savedCategory, CategoryDto.class);
    }

    /**
     * Updates an existing category identified by its ID with the new details provided.
     *
     * @param categoryDto The DTO containing the new details of the category to be updated.
     * @param categoryId  The ID of the category to be updated.
     * @return A CategoryDto containing the updated details of the category.
     * @throws ResourceNotFoundException if the category with the specified ID does not exist.
     */
    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {
        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));
        category.setCategoryTitle(categoryDto.getCategoryTitle());
        category.setCategoryDescription(categoryDto.getCategoryDescription());
        Category updatedCategory = categoryRepository.save(category);
        return this.modelMapper.map(updatedCategory, CategoryDto.class);
    }

    /**
     * Deletes a category identified by its ID.
     *
     * @param categoryId the ID of the category to be deleted.
     * @throws ResourceNotFoundException if the category with the specified ID does not exist.
     */
    @Override
    public void deleteCategory(Integer categoryId) {
        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));
        this.categoryRepository.delete(category);
    }

    /**
     * Retrieves a category identified by its ID.
     *
     * @param categoryId The ID of the category to be retrieved.
     * @return A CategoryDto containing the details of the category.
     * @throws ResourceNotFoundException if the category with the specified ID does not exist.
     */
    @Override
    public CategoryDto getCategoryById(Integer categoryId) {
        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));
        return this.modelMapper.map(category, CategoryDto.class);
    }

    /**
     * Retrieves a list of all categories.
     *
     * @return A list of CategoryDto objects representing all categories.
     */
    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = this.categoryRepository.findAll();
        return categories.stream().map(category -> this.modelMapper.map(category, CategoryDto.class)).collect(Collectors.toList());
    }
}
