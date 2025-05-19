package com.subro.blog.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private int id;

    @NotNull(message = "Name must not be null")
    @NotBlank(message = "Name must not be blanked")
    private String name;

    @Email(message = "Please enter the valid email format")
    @NotBlank(message = "Email must not be blanked")
    @NotNull(message = "Email must not be null")
    private String email;

    @NotNull(message = "Password must not be null")
    @NotBlank(message = "Password must not be blanked")
    @Size(min = 3, max = 10, message = "Password must contain between 3 to 10 characters")
    private String password;

    @NotNull(message = "About must not be null")
    @NotBlank(message = "About must not be blanked")
    private String about;


}
