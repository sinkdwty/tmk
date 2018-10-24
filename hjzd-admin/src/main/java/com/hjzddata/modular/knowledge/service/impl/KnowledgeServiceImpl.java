package com.hjzddata.modular.knowledge.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.modular.knowledge.model.Knowledge;
import com.hjzddata.modular.knowledge.dao.KnowledgeMapper;
import com.hjzddata.modular.knowledge.service.IKnowledgeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 知识表 服务实现类
 * </p>
 *
 * @author hjzd
 * @since 2018-07-10
 */
@Service
public class KnowledgeServiceImpl extends ServiceImpl<KnowledgeMapper, Knowledge> implements IKnowledgeService {

    @Override
    public List<Map<String, Object>> selectKnowledges(Page<Knowledge> page, String key_word, String column, Integer category) {
        return this.baseMapper.selectKnowledges(page, key_word, column,category);
    }

    @Override
    public List<Map> knowledgeExistence (String name,Integer id) {
        return this.baseMapper.knowledgeExistence(name,id);
    }
}
