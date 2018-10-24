package com.hjzddata.modular.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hjzddata.modular.system.dao.DictMapper;
import com.hjzddata.modular.system.model.Dict;
import com.hjzddata.modular.system.service.IDictService;
import com.hjzddata.core.common.exception.BizExceptionEnum;
import com.hjzddata.core.exception.HjzdException;
import com.hjzddata.modular.system.dao.DictMapper;
import com.hjzddata.modular.system.model.Dict;
import com.hjzddata.modular.system.service.IDictService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.hjzddata.core.common.constant.factory.MutiStrFactory.*;

@Service
@Transactional
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements IDictService {

    @Resource
    private DictMapper dictMapper;

    @Override
    public void addDict(String dictName, String code, String dictValues) {
        //判断有没有该字典
        List<Dict> dicts = dictMapper.selectList(new EntityWrapper<Dict>().eq("name", dictName).and().eq("pid", 0));
        if (dicts != null && dicts.size() > 0) {
            throw new HjzdException(BizExceptionEnum.DICT_EXISTED);
        }

        //解析dictValues
        List<Map<String, String>> items = parseKeyValue(dictValues);

        //添加字典
        Dict dict = new Dict();
        dict.setName(dictName);
        dict.setCode(code);
        dict.setNum(0);
        dict.setPid(0);
        this.dictMapper.insert(dict);

        //添加字典条目
        for (Map<String, String> item : items) {
            String num = item.get(MUTI_STR_KEY);
            String name = item.get(MUTI_STR_VALUE);
            Dict itemDict = new Dict();
            itemDict.setPid(dict.getId());
            itemDict.setName(name);
            try {
                itemDict.setNum(Integer.valueOf(num));
                itemDict.setCode(num);
            } catch (NumberFormatException e) {
                throw new HjzdException(BizExceptionEnum.DICT_MUST_BE_NUMBER);
            }
            this.dictMapper.insert(itemDict);
        }
    }

    @Override
    public void editDict(Integer dictId, String dictName, String code, String dicts) {
        // add by eric 2018-09-25 start
        this.updateDict(dictId,dictName,code,dicts);
        // add by eric 2018-09-25 end
    }

    @Override
    public void updateDict(Integer dictId,String dictName,String code,String dicts){
        Dict dict = this.selectById(dictId);
        if(dict!=null) { // 父级字典存在
            List<Dict> list = this.selectByPid(dictId);
            // 页面新增部分，插入库; 页面删除，做删除处理，否则做update处理

            //解析dictValues
            List<Map<String, String>> items = parseKeyValue(dicts);

            //修改父级字典
            Dict n_dict = new Dict();
            n_dict.setId(dictId);
            n_dict.setName(dictName);
            n_dict.setCode(code);
            n_dict.setNum(0);
            n_dict.setPid(0);
            this.updateById(n_dict);

            // 临时list 过渡使用
            List<Dict> tlist =  new ArrayList<Dict>();

            // 如果页面比数据表中数据多，一般为新增
            if(items.size()>list.size()){
                for(int i=list.size();i<items.size();i++){
                    list.add(null);
                }
            }else if(items.size()<list.size()){ // 页面比数据表中少，一般为删除
               // 多出来的N条删除
               for(int i=items.size();i<list.size();i++){
                  tlist.add(list.get(i));
               }
            }else{ // 一样多，只为修改，无删减、无新增
            }

            for (int i=0;i<items.size();i++) {
                Map<String, String> item  = items.get(i);
                String num = item.get(MUTI_STR_KEY);
                String name = item.get(MUTI_STR_VALUE);

                Dict itemDict = new Dict();

                itemDict.setPid(n_dict.getId());
                itemDict.setName(name);
                try {
                    itemDict.setNum(Integer.valueOf(num));
                    itemDict.setCode(num);
                } catch (NumberFormatException e) {
                    throw new HjzdException(BizExceptionEnum.DICT_MUST_BE_NUMBER);
                }
                if (list.get(i)!=null){
                   itemDict.setId(list.get(i).getId());
                   this.updateById(itemDict); // 更新
                }else{
                   this.dictMapper.insert(itemDict); // 新增
                }
            }

            if(tlist.size()>0){ // 多余部分，直接物理删除
                for(Dict _dict:   tlist){
                   this.deleteById(_dict.getId());
                }
            }
        }else{
            throw new HjzdException(BizExceptionEnum.DICT_EXISTED);
        }
    }

    @Override
    public void delteDict(Integer dictId) {
        //删除这个字典的子词典
        Wrapper<Dict> dictEntityWrapper = new EntityWrapper<>();
        dictEntityWrapper = dictEntityWrapper.eq("pid", dictId);
        dictMapper.delete(dictEntityWrapper);

        //删除这个词典
        dictMapper.deleteById(dictId);
    }

    @Override
    public List<Dict> selectByCode(String code) {
        return this.baseMapper.selectByCode(code);
    }

    @Override
    public List<Map<String, Object>> list(String conditiion) {
        return this.baseMapper.list(conditiion);
    }

    @Override
    public List<Dict> selectByParentCode(String code) {
        return this.baseMapper.selectByParentCode(code);
    }

    @Override
    public List<Dict> selectByPid(Integer value) {
        return this.baseMapper.selectByPid(value);
    }

    @Override
    public List<Map<String, Object>> getDict(Page<Dict> page, String condition) {
        return this.baseMapper.getDict(page, condition);
    }

    @Override
    public List<Dict> selectAllByCode(Integer product_id, Integer level,String code) {
        return this.baseMapper.selectAllByCode(product_id, level, code);
    }
}
