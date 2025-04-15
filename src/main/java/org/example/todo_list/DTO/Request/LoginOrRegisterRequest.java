package org.example.todo_list.DTO.Request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/*登录或者注册的 DTO*/

@Data
@RequiredArgsConstructor
public class LoginOrRegisterRequest {
    @NotNull
    private String username;
    @NotNull
    private String password;

}
