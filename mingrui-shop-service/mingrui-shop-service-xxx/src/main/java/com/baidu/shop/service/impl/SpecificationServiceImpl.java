package com.baidu.shop.service.impl;

import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SpecGroupDTO;
import com.baidu.shop.dto.SpecParamDTO;
import com.baidu.shop.entity.SpecGroupEntity;
import com.baidu.shop.entity.SpecParamEntity;
import com.baidu.shop.mapper.SpecGroupMapper;
import com.baidu.shop.mapper.SpecParamMapper;
import com.baidu.shop.service.SpecificationService;
import com.baidu.shop.utils.BaiduBeanUtil;
import com.baidu.shop.utils.ObjectUtil;
import com.google.gson.JsonObject;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

public class SpecificationServiceImpl extends BaseApiService implements SpecificationService {

    @Resource
    private SpecGroupMapper specGroupMapper;

    @Resource
    private SpecParamMapper specParamMapper;

    @Override
    public Result<List<SpecGroupEntity>> getSpecGroupInfo(SpecGroupDTO specGroupDTO) {

        Example example = new Example(SpecGroupEntity.class);

        example.createCriteria().andEqualTo("cid", BaiduBeanUtil.copyProperties(specGroupDTO,SpecGroupEntity.class).getCId());

        List<SpecGroupEntity> specGroupEntities = specGroupMapper.selectByExample(example);
        return this.setResultSuccess(specGroupEntities);
    }

    //新增
    @Override
    @Transactional
    public Result<JsonObject> saveGroupInfo(SpecGroupDTO specGroupDTO) {
        specGroupMapper.insertSelective(BaiduBeanUtil.copyProperties(specGroupDTO,SpecGroupEntity.class));
        return this.setResultSuccess();
    }

    //修改
    @Override
    @Transactional
    public Result<JsonObject> editGroupInfo(SpecGroupDTO specGroupDTO) {
        specGroupMapper.updateByPrimaryKeySelective(BaiduBeanUtil.copyProperties(specGroupDTO,SpecGroupEntity.class));
        return this.setResultSuccess();
    }

    //删除
    @Override
    public Result<JsonObject> deleteGroupId(@NotNull Integer id) {

        //删除规格组之前先判断一下当前规格组下是有有其他规格参数
        Example example = new Example(SpecParamEntity.class);
        example.createCriteria().andEqualTo("groupId",id);
        List<SpecParamEntity> specParamEntities = specParamMapper.selectByExample(example);
        if (specParamEntities.size()>0) return this.setResultError("当前规格组下有其他规格,所以不能删除");
        specGroupMapper.deleteByPrimaryKey(id);
        return this.setResultSuccess();
    }

    @Override
    public Result<List<SpecParamEntity>> getSpecParamInfo(SpecParamDTO specParamDTO) {

        SpecParamEntity specParamEntity = BaiduBeanUtil.copyProperties(specParamDTO, SpecParamEntity.class);
        Example example = new Example(SpecGroupEntity.class);
        Example.Criteria criteria = example.createCriteria();

        if (ObjectUtil.isNotNull(specParamEntity.getGroupId())) criteria.andEqualTo("groupId",specParamEntity.getGroupId());

        if (ObjectUtil.isNotNull(specParamEntity.getCid()))
            criteria.andEqualTo("cid",specParamEntity.getCid());

        List<SpecParamEntity> specParamEntities = specParamMapper.selectByExample(example);
        return this.setResultSuccess(specParamEntities);
    }

    //新增
    @Override
    public Result<JsonObject> saveParamInfo(SpecParamDTO specParamDTO) {
        specParamMapper.insertSelective(BaiduBeanUtil.copyProperties(specParamDTO,SpecParamEntity.class));
        return this.setResultSuccess();
    }

    //修改
    @Override
    public Result<JsonObject> editParamInfo(SpecParamDTO specParamDTO) {
        specParamMapper.updateByPrimaryKeySelective(BaiduBeanUtil.copyProperties(specParamDTO,SpecParamEntity.class));
        return this.setResultSuccess();
    }

    //删除
    @Override
    public Result<JsonObject> deleteParamId(@NotNull Integer id) {
        specParamMapper.deleteByPrimaryKey(id);
        return this.setResultSuccess();
    }
}
