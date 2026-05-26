package com.jbro.plan.model.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbro.plan.model.vo.PlanVo;

@Repository
public class PlanDaoImpl implements PlanDao {
	
    @Autowired
    private SqlSessionTemplate sqlSession;
    private static final String NAMESPACE = "PlanMapper."; 

    @Override
    public List<PlanVo> selectPublicPlanList() {
        return sqlSession.selectList(NAMESPACE + "selectPublicPlanList");
    }
    
    @Override
    public PlanVo selectPlanDetail(Long id) {
        return sqlSession.selectOne(NAMESPACE + "selectPlanDetail", id);
    }
}
