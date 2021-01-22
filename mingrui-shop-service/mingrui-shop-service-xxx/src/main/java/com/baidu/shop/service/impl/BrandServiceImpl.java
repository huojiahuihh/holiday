package com.baidu.shop.service.impl;

import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.BrandDTO;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.mapper.BrandMapper;
import com.baidu.shop.service.BrandService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.codehaus.jackson.map.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class BrandServiceImpl extends BaseApiService implements BrandService {

    @Resource
    private BrandMapper brandMapper;

    //查询
    @Override
    public Result<PageInfo<BrandEntity>> getBrandInfo(BrandDTO brandDTO) {

        //myBatis分页插件 和 mybatis执行器
        PageHelper.startPage(brandDTO.getPage(),brandDTO.getRows());

        //进行排序
        if (!StringUtils.isEmpty(brandDTO.getSort())){
            PageHelper.orderBy(brandDTO.getOrderBy());
        }

        //查询
        BrandEntity brandEntity = new BrandEntity();
        BeanUtils.copyProperties(brandDTO,brandEntity);

        Example example = new Example(BrandEntity.class);
        //搜索框那的范围搜索
        example.createCriteria().andLike("name","%" + brandEntity.getName() + "%");
        //分页
        List<BrandEntity> brandEntities = brandMapper.selectByExample(example);
        PageInfo<BrandEntity> pageInfo = new PageInfo<>(brandEntities);
        return this.setResultSuccess(pageInfo);
    }
}
