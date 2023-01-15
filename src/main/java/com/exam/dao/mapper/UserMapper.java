package com.exam.dao.mapper;

import com.exam.module.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * Mybatis UserMapper
 *
 * @author wangpeng
 */
@Mapper
public interface UserMapper {
    void addUser(User user);

    User selectUserByName(String userName);
}
