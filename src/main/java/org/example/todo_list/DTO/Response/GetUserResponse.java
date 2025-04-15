package org.example.todo_list.DTO.Response;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GetUserResponse {
    @NotNull
    private String username;
    @NotNull
    private String avatarUrl;
}
