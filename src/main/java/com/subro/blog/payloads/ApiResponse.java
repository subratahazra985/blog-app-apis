package com.subro.blog.payloads;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {
    private String message;
    private boolean success;

}
