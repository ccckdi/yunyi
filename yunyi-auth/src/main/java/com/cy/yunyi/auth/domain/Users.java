package com.cy.yunyi.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: chx
 * @Description:
 * @DateTime: 2021/11/10 21:09
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    private Integer id;
    private String username;
    private String password;
    private String SecurityUser;
}
