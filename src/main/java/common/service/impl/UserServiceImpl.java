package common.service.impl;

import common.pojo.User;
import common.service.UserService;

import java.util.Random;
import java.util.UUID;

public class UserServiceImpl implements UserService {

    public User getUserByUserId(Integer id) {
        System.out.println("客户端查询了"+id+"的用户");
        //模拟从数据库中取用户的行为
        Random random = new Random();//种子随机数产生器
        User user = User.builder().userName(UUID.randomUUID().toString())
                .id(id).sex(random.nextBoolean())
                .build();
        return user;
    }
}
