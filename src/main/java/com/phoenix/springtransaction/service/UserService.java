package com.phoenix.springtransaction.service;

import com.phoenix.springtransaction.UserEntity;
import com.phoenix.springtransaction.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    /**
     * 在方法中注入自己，通过self调用事务方法可成功
     */
    @Autowired
    private UserService self;
    public int createUserWrong1(String name) {
        try {
            this.createUserPrivate(new UserEntity(name));
        } catch (Exception ex) {
            log.error("create user failed bacause {}", ex.getMessage());
        }
        return userRepository.findByName(name).size();
    }

    public int createUserWrong2(String name) {
        try {
            this.createUserPublic(new UserEntity(name));
        } catch (Exception ex) {
            log.error("create user failed because {}", ex.getMessage());
        }
        return userRepository.findByName(name).size();
    }

    //通过注入的self调用事务标记方法，可成功
    public int createUserRight1(String name) {
        try {
            self.createUserPublic(new UserEntity(name));
        } catch (Exception ex) {
            log.error("create user failed because {}", ex.getMessage());
        }
        return userRepository.findByName(name).size();
    }


//    @Transactional
//  被标记Transactional的方法必须是可以重写的  Methods annotated with '@Transactional' must be overridable

    /**
     * 这里给出 @Transactional 生效原则 1，除非特殊配置（比如使用 AspectJ 静态织入实现 AOP），否则只有定义在 public 方法上的
     * @Transactional 才能生效
     * @param entity
     */
    private void createUserPrivate(UserEntity entity) {
        userRepository.save(entity);
        if (entity.getName().contains("test")) {
            throw  new RuntimeException("invalid username");
        }
    }

    /**
     * 测试发现，调用新的 createUserWrong2 方法事务同样不生效。这里，我给出 @Transactional 生效原则 2，必须通过代理过的类从外部调用目标方法才能生效。
     * 此处为内部调用createUserPublic方法
     * @param entity
     */
    @Transactional
    public void createUserPublic(UserEntity entity) {
        userRepository.save(entity);
        if (entity.getName().contains("test")) {
            throw  new RuntimeException("invalid username");
        }
    }

    //根据用户查用户数
    public int getUserCount(String name) {
        return userRepository.findByName(name).size();
    }







}
