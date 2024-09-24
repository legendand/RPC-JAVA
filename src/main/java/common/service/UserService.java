package common.service;

import common.pojo.User;

public interface UserService {
    User getUserByUserId(Integer id);

    //返回插入的user的id
    Integer insertUserId(User user);
}
