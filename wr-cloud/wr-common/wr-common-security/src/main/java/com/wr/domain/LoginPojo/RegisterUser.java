package com.wr.domain.LoginPojo;


import lombok.Data;

@Data
public class RegisterUser {
    
    private String username;

    private String password;

    private String confirmPassword;

    private String nickName;

    private String mobile;
    private String mailbox;
    private String sex;


}
