package com.jbro.chat.model.dao;

import com.jbro.chat.model.vo.ChatVO;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatDAOImpl implements ChatDAO {

    private final SqlSessionTemplate sqlSession;

    @Override
    public int insertChat(ChatVO chat) {
        // "chatMapper"는 XML의 namespace와 맞춰야 합니다.
        return sqlSession.insert("chatMapper.insertChat", chat);
    }

    @Override
    public List<ChatVO> selectAllHistory() {
        return sqlSession.selectList("chatMapper.selectAllHistory");
    }
}