package com.jbro.admin.model.dao;

import com.jbro.admin.model.dto.AdminUserDto.UserItem;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class AdminUserDaoImpl implements AdminUserDao {

    private final SqlSession sqlSession;
    private static final String NS = "com.jbro.admin.adminUserMapper.";

    public AdminUserDaoImpl(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public List<UserItem> selectUsers(Map<String, Object> params) {
        return sqlSession.selectList(NS + "selectUsers", params);
    }

    @Override
    public int selectUserCount(Map<String, Object> params) {
        return sqlSession.selectOne(NS + "selectUserCount", params);
    }
    @Override
    public void updateUserStatus(Map<String, Object> params) {
        sqlSession.update(NS + "updateUserStatus", params);
    }
}