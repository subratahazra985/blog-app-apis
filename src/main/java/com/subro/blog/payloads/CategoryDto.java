package com.subro.blog.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Integer categoryId;
    @NotEmpty(message = "Category Title must not be null or blank")
    @Size(min = 4)
    private String categoryTitle;
    @Size(min = 10)
    @NotEmpty(message = "Category Description must not be null or blank")
    private String categoryDescription;
}
