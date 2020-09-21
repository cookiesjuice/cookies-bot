package com.github.cookiesjuice.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginInfo {
    private Long id;
    private String password;
}
