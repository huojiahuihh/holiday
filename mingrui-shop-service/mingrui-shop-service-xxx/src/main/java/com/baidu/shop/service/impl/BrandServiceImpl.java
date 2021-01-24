package com.baidu.shop.service.impl;

import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.BrandDTO;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.entity.CategoryBrandEntity;
import com.baidu.shop.mapper.BrandMapper;
import com.baidu.shop.mapper.CategoryBrandMapper;
import com.baidu.shop.service.BrandService;
import com.baidu.shop.utils.BaiduBeanUtil;
import com.baidu.shop.utils.ObjectUtil;
import com.baidu.shop.utils.PinyinUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import org.codehaus.jackson.map.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BrandServiceImpl extends BaseApiService implements BrandService {

    @Resource
    private BrandMapper brandMapper;

    @Resource
    private CategoryBrandMapper categoryBrandMapper;

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

    @Override
    public Result<JsonObject> saveBrand(BrandDTO brandDTO) {

        //判断是否为空
        if (!ObjectUtil.isEmpty(brandDTO.getCategories())){
            return this.setResultError("分类Id不能为空");
        }
        //新增返回主键
        BrandEntity brandEntity = BaiduBeanUtil.copyProperties(brandDTO, BrandEntity.class);

        //处理品牌首字母
        brandEntity.setLetter(PinyinUtil.getUpperCase(String.valueOf(brandEntity.getName().toCharArray()[0]),false).toCharArray()[0]);
        brandMapper.insertSelective(brandEntity);

        //维护中间表数据
        //判断分类集合字符串是否包含,
        //判断如果有 ,  就说明新增多条数据分割开来进行新增
        this.insertCreateBrand(brandDTO.getCategories(),brandEntity.getId());
        return this.setResultSuccess("新增成功");
    }

    //修改
    @Override
    public Result<JsonObject> editBrand(BrandDTO brandDTO) {
        BrandEntity brandEntity = BaiduBeanUtil.copyProperties(brandDTO, BrandEntity.class);

        brandEntity.setLetter(PinyinUtil.getUpperCase(String.valueOf(brandEntity.getName().toCharArray()[0]),false).toCharArray()[0]);
        brandMapper.updateByPrimaryKeySelective(brandEntity);

        //通过BrandId删除中间表的数据\
        this.deleteCreateBrandById(brandEntity.getId());
        return this.setResultSuccess();
    }

    @Override
    public Result<JsonObject> delBrand(@NotNull Integer id) {
        //删除品牌
        brandMapper.deleteByPrimaryKey(id);

        //通过brandId删除中间表的数据
        this.deleteCreateBrandById(id);
        return this.setResultSuccess("");
    }


    //提出来公共的部分 使代码更简洁
    private void insertCreateBrand(String categories,Integer brandId){
        //批量新增 \ 新增
        if (org.apache.commons.lang.StringUtils.isEmpty(categories)) throw new RuntimeException();
        //判断 如果有 , 就说明新增多条数据 分割开来进行新增
        if (categories.contains(",")){
            categoryBrandMapper.insertList(
                    Arrays.asList(categories.split(","))
                            .parallelStream()
                            .map(str -> new CategoryBrandEntity(Integer.valueOf(str),brandId))
                            .collect(Collectors.toList())
            );
            //否则就是新增一条数据 普通的新增
        }else {
            CategoryBrandEntity entity = new CategoryBrandEntity(Integer.valueOf(categories),brandId);
            categoryBrandMapper.insertSelective(entity);
        }
    }


    //提出来的公共部分 删除中间表的数聚
    private void deleteCreateBrandById(Integer brandId){
        //通过BrandId删除中间表的数据
        Example example = new Example(CategoryBrandEntity.class);
        example.createCriteria().andEqualTo("brandId",brandId);
        categoryBrandMapper.deleteByExample(example);
    }
}
