package com.phoenix.springtransaction.controller;

import com.phoenix.springtransaction.UserEntity;
import com.phoenix.springtransaction.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("transaction")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("wrong1")
    public int wrong1(@RequestParam("name") String name) {
        return userService.createUserWrong1(name);
    }

    //直接调用标注了@Transactional的方法
    @GetMapping("right")
    public void right(@RequestParam("name") String name) {
        userService.createUserPublic(new UserEntity(name));
    }
}
